package com.example.data.detection

import com.example.data.model.BrandProfile
import com.example.data.model.RiskLevel
import com.example.data.model.RiskSignal
import kotlin.math.max
import kotlin.math.min

object DetectionEngine {

    // Common character substitutions used by threat actors
    private val HOMOGLYPH_MAP = mapOf(
        '0' to 'o',
        '1' to 'l',
        '3' to 'e',
        '4' to 'a',
        '5' to 's',
        '@' to 'a',
        '$' to 's'
    )

    private val SUSPICIOUS_AFFIXES = listOf(
        "support", "helpdesk", "help", "claim", "claims", "bonus", "reward",
        "official", "verify", "verification", "secure", "security", "login",
        "portal", "auth", "crypto", "care", "service", "app", "apk"
    )

    fun calculateLevenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1].lowercaseChar() == s2[j - 1].lowercaseChar()) 0 else 1
                dp[i][j] = min(
                    min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[s1.length][s2.length]
    }

    fun calculateNormalizedSimilarity(target: String, reference: String): Int {
        val cleanTarget = target.lowercase().trim()
        val cleanRef = reference.lowercase().trim()
        if (cleanTarget == cleanRef) return 100

        val dist = calculateLevenshteinDistance(cleanTarget, cleanRef)
        val maxLen = max(cleanTarget.length, cleanRef.length)
        if (maxLen == 0) return 100
        val sim = (1.0 - (dist.toDouble() / maxLen.toDouble())) * 100
        return sim.toInt().coerceIn(0, 100)
    }

    /**
     * Detects homoglyph substitutions like '0' for 'o', '1' for 'l'
     */
    fun detectHomoglyphs(candidate: String, brandWord: String): List<String> {
        val findings = mutableListOf<String>()
        val normalizedCandidate = StringBuilder()
        var hasSubstitutions = false

        for (ch in candidate.lowercase()) {
            if (HOMOGLYPH_MAP.containsKey(ch)) {
                hasSubstitutions = true
                normalizedCandidate.append(HOMOGLYPH_MAP[ch])
                findings.add("Character replacement '$ch' used in place of '${HOMOGLYPH_MAP[ch]}'")
            } else {
                normalizedCandidate.append(ch)
            }
        }

        if (hasSubstitutions && normalizedCandidate.toString().contains(brandWord.lowercase())) {
            findings.add(0, "High-probability homoglyph attack targeting brand '$brandWord'")
        }
        return findings
    }

    /**
     * Detects inserted prefixes, suffixes, punctuation, and deceptive keywords
     */
    fun analyzeLookAlikePattern(candidateUsername: String, brandName: String): String {
        val clean = candidateUsername.lowercase().replace("@", "")
        val brandLower = brandName.lowercase()

        // 1. Exact match safeguard
        if (clean == brandLower) {
            return "Exact brand identity"
        }

        val patterns = mutableListOf<String>()

        // 2. Check homoglyphs
        val homoglyphs = detectHomoglyphs(clean, brandLower)
        if (homoglyphs.isNotEmpty()) {
            patterns.add("Homoglyph substitutions detected: ${homoglyphs.first()}")
        }

        // 3. Check punctuation delimiters
        if (clean.contains("-") || clean.contains("_") || clean.contains(".")) {
            patterns.add("Punctuation injection ('_', '-', '.')")
        }

        // 4. Check deceptive affixes
        for (affix in SUSPICIOUS_AFFIXES) {
            if (clean.contains(affix)) {
                patterns.add("Deceptive keyword insertion: '+$affix'")
                break
            }
        }

        // 5. Check transposed or duplicated characters
        if (clean.length > brandLower.length && clean.contains(brandLower)) {
            patterns.add("Brand name encapsulation")
        }

        return if (patterns.isNotEmpty()) patterns.joinToString(" | ") else "General phonetic/lexical variation"
    }

    /**
     * Evaluates a social profile against the official brand profile
     */
    fun evaluateSocialProfile(
        username: String,
        displayName: String,
        bio: String,
        linkUrl: String,
        brandProfile: BrandProfile
    ): Triple<Int, RiskLevel, List<RiskSignal>> {
        val signals = mutableListOf<RiskSignal>()

        // Safeguard: Check if this is an official registered asset
        val isOfficial = brandProfile.officialSocials.any {
            it.handle.equals(username, ignoreCase = true) ||
            it.profileUrl.equals(linkUrl, ignoreCase = true)
        }
        if (isOfficial) {
            return Triple(0, RiskLevel.LOW, emptyList())
        }

        var totalRisk = 0

        // 1. Username similarity
        val brandKeyword = brandProfile.name
        val nameSim = calculateNormalizedSimilarity(username.replace("@", ""), brandKeyword)
        if (nameSim > 70) {
            val score = min(35, (nameSim * 0.35).toInt())
            signals.add(
                RiskSignal(
                    category = "Username Look-Alike",
                    score = score,
                    description = "$nameSim / 100 lexical similarity to official brand '$brandKeyword'"
                )
            )
            totalRisk += score
        }

        // 2. Homoglyph check
        val homoglyphs = detectHomoglyphs(username, brandKeyword)
        if (homoglyphs.isNotEmpty()) {
            signals.add(
                RiskSignal(
                    category = "Homoglyph Attack",
                    score = 25,
                    description = homoglyphs.first(),
                    isCritical = true
                )
            )
            totalRisk += 25
        }

        // 3. Keyword / Affix insertion
        val lookAlikePattern = analyzeLookAlikePattern(username, brandKeyword)
        if (lookAlikePattern.contains("Deceptive keyword")) {
            signals.add(
                RiskSignal(
                    category = "Deceptive Intent",
                    score = 20,
                    description = "Contains support/official/claim terminology common in social fraud"
                )
            )
            totalRisk += 20
        }

        // 4. Bio / Brand description copying
        val bioMatches = brandProfile.brandKeywords.count { bio.contains(it, ignoreCase = true) }
        if (bioMatches >= 2) {
            signals.add(
                RiskSignal(
                    category = "Brand Content Scraping",
                    score = 15,
                    description = "Profile bio reproduces $bioMatches trademark brand keywords"
                )
            )
            totalRisk += 15
        }

        // 5. Suspicious / Phishing Links
        val isOfficialDomain = brandProfile.officialDomains.any { linkUrl.contains(it, ignoreCase = true) }
        if (linkUrl.isNotBlank() && !isOfficialDomain) {
            signals.add(
                RiskSignal(
                    category = "Off-Domain Redirection",
                    score = 25,
                    description = "Redirects users to non-whitelisted domain: $linkUrl",
                    isCritical = true
                )
            )
            totalRisk += 25
        }

        val finalScore = totalRisk.coerceIn(0, 100)
        val level = when {
            finalScore >= 80 -> RiskLevel.CRITICAL
            finalScore >= 60 -> RiskLevel.HIGH
            finalScore >= 30 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        return Triple(finalScore, level, signals)
    }

    /**
     * Evaluates a mobile app against the official brand profile
     */
    fun evaluateMobileApp(
        appName: String,
        packageName: String,
        publisher: String,
        permissions: List<String>,
        brandProfile: BrandProfile
    ): Triple<Int, RiskLevel, List<RiskSignal>> {
        val signals = mutableListOf<RiskSignal>()

        // Safeguard: Check if official app
        val isOfficial = brandProfile.officialMobileApps.any {
            it.appId.equals(packageName, ignoreCase = true) &&
            brandProfile.authorizedPublishers.any { authPub -> authPub.equals(publisher, ignoreCase = true) }
        }
        if (isOfficial) {
            return Triple(0, RiskLevel.LOW, emptyList())
        }

        var totalRisk = 0

        // 1. App name similarity
        val nameSim = calculateNormalizedSimilarity(appName, brandProfile.name)
        if (nameSim >= 65) {
            val score = min(30, (nameSim * 0.3).toInt())
            signals.add(
                RiskSignal(
                    category = "Look-Alike App Title",
                    score = score,
                    description = "$nameSim / 100 title similarity to '${brandProfile.name}'"
                )
            )
            totalRisk += score
        }

        // 2. Developer / Publisher Mismatch (Crucial signal)
        val isAuthorizedDev = brandProfile.authorizedPublishers.any {
            it.equals(publisher, ignoreCase = true)
        }
        if (!isAuthorizedDev) {
            signals.add(
                RiskSignal(
                    category = "Publisher Mismatch",
                    score = 35,
                    description = "Publisher '$publisher' is NOT in authorized publishers: ${brandProfile.authorizedPublishers.joinToString()}",
                    isCritical = true
                )
            )
            totalRisk += 35
        }

        // 3. Risky / Banking trojan permissions
        val dangerousPerms = listOf(
            "android.permission.READ_SMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.SYSTEM_ALERT_WINDOW",
            "android.permission.REQUEST_INSTALL_PACKAGES"
        )
        val flaggedPerms = permissions.filter { dangerousPerms.contains(it) }
        if (flaggedPerms.isNotEmpty()) {
            signals.add(
                RiskSignal(
                    category = "High-Risk Android Permissions",
                    score = 25,
                    description = "Requests overlay / SMS interception capabilities: ${flaggedPerms.map { it.substringAfterLast(".") }.joinToString()}",
                    isCritical = true
                )
            )
            totalRisk += 25
        }

        // 4. Package naming mimicry
        if (packageName.contains(brandProfile.name.lowercase()) && !packageName.startsWith("com.${brandProfile.name.lowercase()}")) {
            signals.add(
                RiskSignal(
                    category = "Package Spoofing",
                    score = 15,
                    description = "Package identifier '$packageName' mimics brand namespace"
                )
            )
            totalRisk += 15
        }

        val finalScore = totalRisk.coerceIn(0, 100)
        val level = when {
            finalScore >= 80 -> RiskLevel.CRITICAL
            finalScore >= 60 -> RiskLevel.HIGH
            finalScore >= 30 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        return Triple(finalScore, level, signals)
    }
}
