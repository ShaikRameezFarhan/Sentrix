package com.example.data.repository

import android.content.Context
import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SentrixRepository(context: Context) {

    private val db = SentrixDatabase.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val brandDao = db.brandDao()
    private val socialDao = db.socialDetectionDao()
    private val appDao = db.appDetectionDao()
    private val caseDao = db.investigationCaseDao()
    private val remediationDao = db.remediationTaskDao()
    private val threatDao = db.threatDao()
    private val notifDao = db.notificationDao()

    private val _selectedOrgId = MutableStateFlow("org_apex")
    val selectedOrgId: StateFlow<String> = _selectedOrgId.asStateFlow()

    val organizations: StateFlow<List<BrandProfile>> = brandDao.getAllBrands()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val socialDetections: StateFlow<List<SocialDetection>> = socialDao.getAllSocialDetections()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val appDetections: StateFlow<List<AppDetection>> = appDao.getAllAppDetections()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val investigationCases: StateFlow<List<InvestigationCase>> = caseDao.getAllCases()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val remediationTasks: StateFlow<List<RemediationTask>> = remediationDao.getAllTasks()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val threatIndicators: StateFlow<List<ThreatIndicator>> = threatDao.getAllIndicators()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val threatCampaigns: StateFlow<List<ThreatCampaign>> = threatDao.getAllCampaigns()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val notifications: StateFlow<List<NotificationItem>> = notifDao.getAllNotifications()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val auditLogs: StateFlow<List<AuditLog>> = notifDao.getAllAuditLogs()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    init {
        scope.launch {
            // Check if initial seed is required
            val existingBrands = brandDao.getBrandById("org_apex")
            if (existingBrands == null) {
                seedInitialDatabase()
            }
        }
    }

    private fun currentTime(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())
    }

    fun selectOrganization(orgId: String) {
        _selectedOrgId.value = orgId
        addAuditLog("Organization Switched", "User switched active monitoring tenant to '$orgId'")
    }

    fun getActiveBrandProfile(): BrandProfile {
        return organizations.value.firstOrNull { it.id == _selectedOrgId.value }
            ?: defaultFallbackBrand()
    }

    fun updateBrandProfile(updated: BrandProfile) {
        scope.launch {
            brandDao.updateBrand(updated.toEntity())
            addAuditLog("Brand Profile Updated", "Modified official assets, domains, and keywords for ${updated.name}")
        }
    }

    fun updateSocialDetectionStatus(
        detectionId: String,
        newStatus: DetectionStatus,
        analystNotes: String = "",
        resolutionReason: String = ""
    ) {
        scope.launch {
            socialDao.updateStatus(detectionId, newStatus, analystNotes, resolutionReason)
            addAuditLog("Social Threat Triage", "Updated status of detection $detectionId to $newStatus")
        }
    }

    fun updateAppDetectionStatus(
        detectionId: String,
        newStatus: DetectionStatus,
        analystNotes: String = "",
        resolutionReason: String = ""
    ) {
        scope.launch {
            appDao.updateStatus(detectionId, newStatus, analystNotes, resolutionReason)
            addAuditLog("App Store Threat Triage", "Updated status of app detection $detectionId to $newStatus")
        }
    }

    fun addRemediationTask(task: RemediationTask) {
        scope.launch {
            remediationDao.insertTask(task.toEntity())
            addAuditLog("Takedown Notice Generated", "Initiated ${task.legalNoticeType} targeting ${task.targetEntity}")
        }
    }

    fun updateRemediationStatus(taskId: String, newStatus: RemediationStatus) {
        scope.launch {
            remediationDao.updateStatus(taskId, newStatus)
            addAuditLog("Takedown Status Updated", "Remediation $taskId transitioned to $newStatus")
        }
    }

    fun markAllNotificationsRead() {
        scope.launch {
            notifDao.markAllAsRead()
        }
    }

    fun addAuditLog(action: String, details: String) {
        scope.launch {
            val log = AuditLog(
                id = "log_${System.currentTimeMillis()}",
                timestamp = currentTime(),
                user = "Lead Analyst (CIPHER-SOC)",
                action = action,
                details = details,
                ipAddress = "10.14.88.241 (Corporate VPN)"
            )
            notifDao.insertAuditLog(log.toEntity())
        }
    }

    fun resetToInitialDemoData() {
        scope.launch {
            brandDao.clearAll()
            socialDao.clearAll()
            appDao.clearAll()
            caseDao.clearAll()
            remediationDao.clearAll()
            threatDao.clearIndicators()
            threatDao.clearCampaigns()
            notifDao.clearNotifications()
            notifDao.clearAuditLogs()

            seedInitialDatabase()
        }
    }

    private suspend fun seedInitialDatabase() {
        // 1. Multi-Industry Brand Profiles
        val apexBank = BrandProfile(
            id = "org_apex",
            name = "ApexBank",
            industry = "Fintech & Tier-1 Digital Banking",
            description = "ApexBank is a premier digital-first financial institution offering consumer banking, wealth management, and commercial payment gateways.",
            officialDomains = listOf("apexbank.com", "apex-auth.com", "apexbank.io"),
            officialSocials = listOf(
                OfficialSocialAsset("X (Twitter)", "@ApexBank", "https://x.com/ApexBank", true, "1.4M"),
                OfficialSocialAsset("Instagram", "@apexbank_official", "https://instagram.com/apexbank_official", true, "890K"),
                OfficialSocialAsset("LinkedIn", "Apex Financial Group", "https://linkedin.com/company/apex-bank", true, "320K"),
                OfficialSocialAsset("Facebook", "ApexBank Official", "https://facebook.com/ApexBank", true, "2.1M"),
                OfficialSocialAsset("Telegram", "@ApexBankNews", "https://t.me/ApexBankNews", true, "140K")
            ),
            officialMobileApps = listOf(
                OfficialAppAsset("ApexBank Mobile Banking", "Android", "com.apexbank.mobile", "Apex Financial Group Inc.", "https://play.google.com/store/apps/details?id=com.apexbank.mobile", 4.8),
                OfficialAppAsset("ApexBank: Secure Finance", "iOS", "id1492048201", "Apex Financial Group Inc.", "https://apps.apple.com/app/apexbank/id1492048201", 4.9)
            ),
            authorizedPublishers = listOf("Apex Financial Group Inc.", "Apex Digital Technologies Corp."),
            brandKeywords = listOf("ApexBank", "Apex Pay", "Apex Wealth", "Apex Capital", "Apex Cards"),
            brandAliases = listOf("Apex", "Apex Bank", "Apex Financial", "ApexBank Corp"),
            knownLegitimateAssets = listOf("ApexBank Verified Bot", "Apex Support Desk UK", "Apex Partner API"),
            lastUpdated = "2026-10-12 06:30"
        )

        val shopNova = BrandProfile(
            id = "org_shopnova",
            name = "ShopNova",
            industry = "E-Commerce & Global Marketplace",
            description = "ShopNova is an international consumer marketplace connecting millions of verified merchants and buyers across 40 countries.",
            officialDomains = listOf("shopnova.com", "shopnova-pay.com", "nova-delivery.io"),
            officialSocials = listOf(
                OfficialSocialAsset("Instagram", "@shopnova_global", "https://instagram.com/shopnova_global", true, "3.8M"),
                OfficialSocialAsset("TikTok", "@shopnova_deals", "https://tiktok.com/@shopnova_deals", true, "5.2M"),
                OfficialSocialAsset("X (Twitter)", "@ShopNovaCare", "https://x.com/ShopNovaCare", true, "450K")
            ),
            officialMobileApps = listOf(
                OfficialAppAsset("ShopNova: Online Shopping", "Android", "com.shopnova.marketplace", "ShopNova Global Retail Ltd", "https://play.google.com/store/apps/details?id=com.shopnova.marketplace", 4.7),
                OfficialAppAsset("ShopNova Marketplace", "iOS", "id1529301931", "ShopNova Global Retail Ltd", "https://apps.apple.com/app/shopnova/id1529301931", 4.8)
            ),
            authorizedPublishers = listOf("ShopNova Global Retail Ltd", "Nova Interactive Pte"),
            brandKeywords = listOf("ShopNova", "NovaPay", "NovaExpress", "NovaFlash"),
            brandAliases = listOf("Nova Store", "Shop Nova", "Nova Deals"),
            knownLegitimateAssets = listOf("ShopNova Influencer Portal", "Nova Seller Central"),
            lastUpdated = "2026-10-11 14:15"
        )

        val cloudMatrix = BrandProfile(
            id = "org_cloudmatrix",
            name = "CloudMatrix",
            industry = "Enterprise SaaS & Cloud Infrastructure",
            description = "CloudMatrix delivers zero-trust cloud orchestration, CI/CD observability, and managed Kubernetes clusters for Fortune 500 engineering teams.",
            officialDomains = listOf("cloudmatrix.io", "cloudmatrix-auth.net", "matrixops.dev"),
            officialSocials = listOf(
                OfficialSocialAsset("LinkedIn", "CloudMatrix Enterprise", "https://linkedin.com/company/cloudmatrix", true, "180K"),
                OfficialSocialAsset("X (Twitter)", "@CloudMatrixHQ", "https://x.com/CloudMatrixHQ", true, "95K")
            ),
            officialMobileApps = listOf(
                OfficialAppAsset("CloudMatrix Mobile Ops", "Android", "io.cloudmatrix.ops", "CloudMatrix Systems Inc.", "https://play.google.com/store/apps/details?id=io.cloudmatrix.ops", 4.6)
            ),
            authorizedPublishers = listOf("CloudMatrix Systems Inc."),
            brandKeywords = listOf("CloudMatrix", "MatrixOps", "MatrixCloud", "CM Cluster"),
            brandAliases = listOf("Cloud Matrix", "Matrix Cloud", "CMSys"),
            knownLegitimateAssets = listOf("CloudMatrix Status Page", "Matrix Community Slack"),
            lastUpdated = "2026-10-10 09:40"
        )

        val pulseWear = BrandProfile(
            id = "org_pulsewear",
            name = "PulseWear",
            industry = "Consumer Health & Smart Devices",
            description = "PulseWear manufactures clinical-grade continuous biometrics wearables, ECG watches, and athletic recovery fitness rings.",
            officialDomains = listOf("pulsewear.health", "pulsewear-sync.com"),
            officialSocials = listOf(
                OfficialSocialAsset("Instagram", "@pulsewear_health", "https://instagram.com/pulsewear_health", true, "720K"),
                OfficialSocialAsset("X (Twitter)", "@PulseWearHelp", "https://x.com/PulseWearHelp", true, "85K")
            ),
            officialMobileApps = listOf(
                OfficialAppAsset("PulseWear Health & ECG", "Android", "health.pulsewear.companion", "PulseWear Biometrics Corp.", "https://play.google.com/store/apps/details?id=health.pulsewear.companion", 4.8),
                OfficialAppAsset("PulseWear Companion", "iOS", "id1620392019", "PulseWear Biometrics Corp.", "https://apps.apple.com/app/pulsewear/id1620392019", 4.9)
            ),
            authorizedPublishers = listOf("PulseWear Biometrics Corp."),
            brandKeywords = listOf("PulseWear", "PulseRing", "PulseECG", "BioSync"),
            brandAliases = listOf("Pulse Wear", "PulseHealth"),
            knownLegitimateAssets = listOf("PulseWear Clinical Research Lab"),
            lastUpdated = "2026-10-09 11:20"
        )

        brandDao.insertAll(listOf(apexBank.toEntity(), shopNova.toEntity(), cloudMatrix.toEntity(), pulseWear.toEntity()))

        // 2. Realistic Social Media Detections
        val socialList = listOf(
            SocialDetection(
                id = "soc_101",
                brandId = "org_apex",
                platform = "X (Twitter)",
                profileName = "ApexBank Customer Care",
                username = "@ApexB0nk_Support",
                profileUrl = "https://x.com/ApexB0nk_Support",
                avatarDescription = "Copied official ApexBank cyan crest with slight compression artifacts",
                riskScore = 96,
                riskLevel = RiskLevel.CRITICAL,
                confidence = 98,
                status = DetectionStatus.NEW,
                detectedAt = "2026-10-12 06:14",
                detectionReasons = listOf(
                    "Homoglyph substitution: '0' replacing 'a' in 'ApexB0nk'",
                    "Inserted deceptive suffix '_Support'",
                    "Active phishing link targeting banking credentials",
                    "Directly replying to genuine ApexBank customer complaints"
                ),
                riskSignals = listOf(
                    RiskSignal("Homoglyph Attack", 35, "Digit '0' used to spoof brand character 'a'", true),
                    RiskSignal("Phishing Redirection", 30, "Bio points to 'hxxps://apex-security-help[.]site/login'", true),
                    RiskSignal("Deceptive Suffix", 20, "Uses '_Support' to impersonate official assistance desk"),
                    RiskSignal("Unauthorized Logo Copying", 11, "High visual match to registered trademark mark")
                ),
                lookAlikePattern = "Character replacement ('0' -> 'o') + inserted '_Support'",
                suspiciousLinks = listOf("https://apex-security-help.site/login", "https://t.me/ApexBankUrgentHelp"),
                matchedOfficialHandle = "@ApexBank",
                isOfficialAsset = false,
                analystNotes = "Active credential harvester observed in public replies. Ready for immediate suspension request."
            ),
            SocialDetection(
                id = "soc_102",
                brandId = "org_apex",
                platform = "Instagram",
                profileName = "ApexBank Loyalty & Bonus",
                username = "@apexbank_official_giveaway",
                profileUrl = "https://instagram.com/apexbank_official_giveaway",
                avatarDescription = "Official brand logo overlaid with gold 'VIP Bonus' banner",
                riskScore = 88,
                riskLevel = RiskLevel.CRITICAL,
                confidence = 94,
                status = DetectionStatus.INVESTIGATING,
                detectedAt = "2026-10-12 04:45",
                detectionReasons = listOf(
                    "Keyword compounding: 'official' + 'giveaway'",
                    "Promoting cryptocurrency investment scam in company name",
                    "Off-domain telegram link harvesting banking 2FA codes"
                ),
                riskSignals = listOf(
                    RiskSignal("Fraudulent Giveaway Scheme", 35, "Promises 500 USD cash rewards for linking bank accounts", true),
                    RiskSignal("Off-Domain Link", 28, "Bio directs to unofficial Telegram bot @ApexCashClaimBot", true),
                    RiskSignal("High Name Similarity", 25, "Complete inclusion of brand name 'apexbank'")
                ),
                lookAlikePattern = "Keyword compounding ('_official_giveaway') + brand encapsulation",
                suspiciousLinks = listOf("https://apexbonus-rewards.xyz/claim"),
                matchedOfficialHandle = "@apexbank_official",
                isOfficialAsset = false,
                analystNotes = "Evidence gathered; 12 customer reports received in SOC queue."
            ),
            SocialDetection(
                id = "soc_103",
                brandId = "org_apex",
                platform = "Telegram",
                profileName = "ApexBank VIP Community & Alerts",
                username = "@ApexBank_VIP_Desk",
                profileUrl = "https://t.me/ApexBank_VIP_Desk",
                avatarDescription = "Official corporate headquarters photo and blue shield badge",
                riskScore = 74,
                riskLevel = RiskLevel.HIGH,
                confidence = 90,
                status = DetectionStatus.CONFIRMED,
                detectedAt = "2026-10-11 21:30",
                detectionReasons = listOf(
                    "Unregistered Telegram channel impersonating official banking desk",
                    "Channel administrator soliciting account numbers via direct message"
                ),
                riskSignals = listOf(
                    RiskSignal("Social Engineering Channel", 40, "Public channel soliciting customer KYC documents", true),
                    RiskSignal("Name Collision", 20, "Uses official brand prefix with '_VIP_Desk'"),
                    RiskSignal("Brand Impersonation", 14, "Contains exact copy of bank privacy policy in channel bio")
                ),
                lookAlikePattern = "Punctuation spacing ('_') + injected 'VIP_Desk'",
                suspiciousLinks = listOf("https://t.me/joinchat/ApexBankFastVerification"),
                matchedOfficialHandle = "@ApexBankNews",
                isOfficialAsset = false,
                analystNotes = "Escalated to Telegram trust & safety team with notarized trademark certificate."
            ),
            SocialDetection(
                id = "soc_104",
                brandId = "org_apex",
                platform = "LinkedIn",
                profileName = "Apex Capital HR & Recruitment",
                username = "apex-bank-careers-portal",
                profileUrl = "https://linkedin.com/company/apex-bank-careers-portal",
                avatarDescription = "Grey-tinted ApexBank monogram logo",
                riskScore = 65,
                riskLevel = RiskLevel.HIGH,
                confidence = 86,
                status = DetectionStatus.NEW,
                detectedAt = "2026-10-11 18:22",
                detectionReasons = listOf(
                    "Fake recruitment page posting fraudulent remote job offers",
                    "Requires applicant to submit banking credentials for 'payroll setup'"
                ),
                riskSignals = listOf(
                    RiskSignal("Employment Fraud", 35, "Directing job applicants to phishing questionnaire"),
                    RiskSignal("Unauthorized Company Page", 20, "Unlinked to verified Apex Financial Group holding"),
                    RiskSignal("Domain Mismatch", 10, "Email contact is 'careers@apex-talent-review.org'")
                ),
                lookAlikePattern = "Hyphenated domain mimicry ('apex-bank-careers-portal')",
                suspiciousLinks = listOf("https://apex-talent-review.org/application"),
                matchedOfficialHandle = "Apex Financial Group",
                isOfficialAsset = false,
                analystNotes = "Found by automated LinkedIn crawler; 4 fraudulent job listings detected."
            ),
            SocialDetection(
                id = "soc_105",
                brandId = "org_apex",
                platform = "X (Twitter)",
                profileName = "ApexBank (Official Verified)",
                username = "@ApexBank",
                profileUrl = "https://x.com/ApexBank",
                avatarDescription = "Official vector crest on electric cyan background",
                riskScore = 0,
                riskLevel = RiskLevel.LOW,
                confidence = 100,
                status = DetectionStatus.RESOLVED,
                detectedAt = "2026-10-10 00:00",
                detectionReasons = listOf(
                    "SAFEGUARD: Official registered brand asset in Brand Profile",
                    "Cryptographic domain linkage confirmed",
                    "Gold verified enterprise checkmark active"
                ),
                riskSignals = emptyList(),
                lookAlikePattern = "None (Authentic Asset)",
                suspiciousLinks = emptyList(),
                matchedOfficialHandle = "@ApexBank",
                isOfficialAsset = true,
                analystNotes = "Legitimate official account. Protected by zero-trust safeguard filter."
            ),
            SocialDetection(
                id = "soc_106",
                brandId = "org_apex",
                platform = "Instagram",
                profileName = "Apex Mountain Climbing Expeditions",
                username = "@apex_outdoor_gear",
                profileUrl = "https://instagram.com/apex_outdoor_gear",
                avatarDescription = "Mountain peak silhouette with hiking boot icon",
                riskScore = 18,
                riskLevel = RiskLevel.LOW,
                confidence = 92,
                status = DetectionStatus.FALSE_POSITIVE,
                detectedAt = "2026-10-11 10:15",
                detectionReasons = listOf(
                    "Benign trademark coexistence: Outdoor sports equipment brand",
                    "No financial services keywords or logos detected",
                    "Legitimate registered trademark in class 25 (Apparel)"
                ),
                riskSignals = listOf(
                    RiskSignal("Incidental Word Overlap", 18, "Shares English noun 'Apex' without financial context")
                ),
                lookAlikePattern = "Generic dictionary word usage",
                suspiciousLinks = emptyList(),
                matchedOfficialHandle = "@apexbank_official",
                isOfficialAsset = false,
                analystNotes = "Analyst Elena marked as False Positive. Distinct commercial sector."
            ),
            SocialDetection(
                id = "soc_201",
                brandId = "org_shopnova",
                platform = "Instagram",
                profileName = "ShopNova Flash Clearance Sale",
                username = "@shopn0va_clearance",
                profileUrl = "https://instagram.com/shopn0va_clearance",
                avatarDescription = "ShopNova orange shopping bag with 'FLASH SALE' badge",
                riskScore = 94,
                riskLevel = RiskLevel.CRITICAL,
                confidence = 97,
                status = DetectionStatus.NEW,
                detectedAt = "2026-10-12 05:50",
                detectionReasons = listOf(
                    "Homoglyph: '0' for 'o' in '@shopn0va_clearance'",
                    "Counterfeit luxury goods flash sale scam",
                    "Phishing checkout portal stealing credit cards"
                ),
                riskSignals = listOf(
                    RiskSignal("Payment Hijacking", 40, "Checkout iframe forwards card CVV to rogue C2 server", true),
                    RiskSignal("Homoglyph Obfuscation", 30, "Digit '0' used to evade standard keyword filters"),
                    RiskSignal("Trademark Infringement", 24, "Uses ShopNova patented color scheme and brand assets")
                ),
                lookAlikePattern = "Homoglyph ('0' -> 'o') + inserted '_clearance'",
                suspiciousLinks = listOf("https://shopn0va-clearance-sale.shop/checkout"),
                matchedOfficialHandle = "@shopnova_global",
                isOfficialAsset = false,
                analystNotes = "High priority takedown prepared for Meta Rights Manager."
            )
        )
        socialDao.insertAll(socialList.map { it.toEntity() })

        // 3. Realistic App Store Detections
        val appList = listOf(
            AppDetection(
                id = "app_101",
                brandId = "org_apex",
                appName = "ApexBank: Fast Mobile Loans & Pay",
                platform = "Android",
                store = "Google Play",
                publisher = "Apex Global Financial Services Ltd",
                officialPublisherExpected = "Apex Financial Group Inc.",
                packageName = "com.apexbank.loans.fastpay",
                storeUrl = "https://play.google.com/store/apps/details?id=com.apexbank.loans.fastpay",
                iconDescription = "Official ApexBank blue shield logo slightly altered with green loan arrow",
                riskScore = 95,
                riskLevel = RiskLevel.CRITICAL,
                confidence = 96,
                status = DetectionStatus.NEW,
                detectedAt = "2026-10-12 05:20",
                nameSimilarity = 88,
                brandingSimilarity = 92,
                isPublisherMismatch = true,
                suspiciousPermissions = listOf(
                    "android.permission.READ_SMS (Interception of bank OTP codes)",
                    "android.permission.RECEIVE_SMS (Silent OTP theft)",
                    "android.permission.SYSTEM_ALERT_WINDOW (Phishing overlay attack on legitimate bank app)",
                    "android.permission.READ_CONTACTS (Extortion list exfiltration)"
                ),
                detectionReasons = listOf(
                    "Developer mismatch: 'Apex Global Financial Services Ltd' is NOT authorized publisher",
                    "Requests SMS interception and screen overlay permissions characteristic of banking Trojans",
                    "Impersonates official ApexBank brand identity to conduct predatory lending fraud"
                ),
                riskSignals = listOf(
                    RiskSignal("Developer Identity Fraud", 35, "Publisher name crafted to confuse consumers", true),
                    RiskSignal("Trojan Capabilities", 35, "Requests SYSTEM_ALERT_WINDOW & READ_SMS for overlay attacks", true),
                    RiskSignal("High Branding Similarity", 15, "Substantial visual and icon similarity to legitimate application"),
                    RiskSignal("Package Namespace Mimicry", 10, "Uses 'com.apexbank.loans' to simulate official repo")
                ),
                matchedOfficialPackage = "com.apexbank.mobile",
                isOfficialAsset = false,
                analystNotes = "Critical trojan. Takedown notice drafted with Google Play Security team."
            ),
            AppDetection(
                id = "app_102",
                brandId = "org_apex",
                appName = "Apex Pay - Wallet & Crypto",
                platform = "iOS",
                store = "Apple App Store",
                publisher = "Apex Shenzhen Tech Co Ltd",
                officialPublisherExpected = "Apex Financial Group Inc.",
                packageName = "com.apextech.apexpay.wallet",
                storeUrl = "https://apps.apple.com/app/apex-pay-wallet/id1698201402",
                iconDescription = "ApexBank navy background with stylized cyan 'P' symbol",
                riskScore = 86,
                riskLevel = RiskLevel.CRITICAL,
                confidence = 91,
                status = DetectionStatus.INVESTIGATING,
                detectedAt = "2026-10-11 23:10",
                nameSimilarity = 82,
                brandingSimilarity = 84,
                isPublisherMismatch = true,
                suspiciousPermissions = listOf(
                    "FaceID Authentication bypass attempt",
                    "Clipboard read on background transition (Mnemonic seed phrase theft)"
                ),
                detectionReasons = listOf(
                    "Developer mismatch: Foreign shell company masquerading as ApexBank affiliate",
                    "Steals crypto seed phrases and directs customer funds to unauthorized smart contract"
                ),
                riskSignals = listOf(
                    RiskSignal("Unauthorized Trademark Use", 35, "Uses 'Apex Pay' registered trademark without licensing", true),
                    RiskSignal("Developer Mismatch", 30, "Publisher does not match verified Apple Developer Account"),
                    RiskSignal("Credential Harvesting", 21, "Intercepts clipboard data for recovery phrases")
                ),
                matchedOfficialPackage = "id1492048201",
                isOfficialAsset = false,
                analystNotes = "Apple Copyright & Fraud dispute submitted. Awaiting Apple Legal response."
            ),
            AppDetection(
                id = "app_103",
                brandId = "org_apex",
                appName = "ApexBank Mobile APK v4.8 (Mod VIP)",
                platform = "Android",
                store = "APKPure / 3rd-Party Repos",
                publisher = "ShadowDrop Security Research Team",
                officialPublisherExpected = "Apex Financial Group Inc.",
                packageName = "com.apexbank.mobile.patched",
                storeUrl = "https://apkpure.com/apexbank-mobile/com.apexbank.mobile.patched",
                iconDescription = "Pixel-for-pixel cloned copy of official ApexBank mobile app icon",
                riskScore = 98,
                riskLevel = RiskLevel.CRITICAL,
                confidence = 99,
                status = DetectionStatus.CONFIRMED,
                detectedAt = "2026-10-11 19:40",
                nameSimilarity = 96,
                brandingSimilarity = 100,
                isPublisherMismatch = true,
                suspiciousPermissions = listOf(
                    "android.permission.REQUEST_INSTALL_PACKAGES (Rogue dropper)",
                    "android.permission.RECORD_AUDIO (Surveillance)",
                    "android.permission.SYSTEM_ALERT_WINDOW (Full screen banking overlay)"
                ),
                detectionReasons = listOf(
                    "Trojanized APK of authentic banking application containing Spyware.Cerberus payload",
                    "Tampered SHA-256 digital certificate signature",
                    "Host distributed on rogue APK download mirrors"
                ),
                riskSignals = listOf(
                    RiskSignal("Tampered Binary / Mod", 40, "Certificate mismatch: Signed with untrusted debug key", true),
                    RiskSignal("Malicious Command & Control", 35, "Network calls to known malicious IP 194.38.20.12", true),
                    RiskSignal("Exact Brand Spoofing", 23, "Complete reproduction of application naming and metadata")
                ),
                matchedOfficialPackage = "com.apexbank.mobile",
                isOfficialAsset = false,
                analystNotes = "Host registrar abuse ticket filed to take down direct download links."
            ),
            AppDetection(
                id = "app_104",
                brandId = "org_apex",
                appName = "ApexBank Mobile Banking",
                platform = "Android",
                store = "Google Play",
                publisher = "Apex Financial Group Inc.",
                officialPublisherExpected = "Apex Financial Group Inc.",
                packageName = "com.apexbank.mobile",
                storeUrl = "https://play.google.com/store/apps/details?id=com.apexbank.mobile",
                iconDescription = "Official certified vector crest in electric cyan",
                riskScore = 0,
                riskLevel = RiskLevel.LOW,
                confidence = 100,
                status = DetectionStatus.RESOLVED,
                detectedAt = "2026-10-10 00:00",
                nameSimilarity = 100,
                brandingSimilarity = 100,
                isPublisherMismatch = false,
                suspiciousPermissions = emptyList(),
                detectionReasons = listOf(
                    "SAFEGUARD: Official registered production mobile application",
                    "Cryptographic key signature verified against enterprise keystore",
                    "Google Play verified finance developer badge"
                ),
                riskSignals = emptyList(),
                matchedOfficialPackage = "com.apexbank.mobile",
                isOfficialAsset = true,
                analystNotes = "Authentic official application. Safeguarded by zero-trust validation rules."
            ),
            AppDetection(
                id = "app_105",
                brandId = "org_apex",
                appName = "Apex Fitness & Gym Companion",
                platform = "Android",
                store = "Google Play",
                publisher = "Apex Health & Wellness LLC",
                officialPublisherExpected = "Apex Financial Group Inc.",
                packageName = "com.apex.workout.tracker",
                storeUrl = "https://play.google.com/store/apps/details?id=com.apex.workout.tracker",
                iconDescription = "Barbell icon on red background",
                riskScore = 15,
                riskLevel = RiskLevel.LOW,
                confidence = 94,
                status = DetectionStatus.FALSE_POSITIVE,
                detectedAt = "2026-10-11 12:00",
                nameSimilarity = 38,
                brandingSimilarity = 10,
                isPublisherMismatch = true,
                suspiciousPermissions = listOf("android.permission.ACTIVITY_RECOGNITION"),
                detectionReasons = listOf(
                    "Legitimate gym workout application sharing prefix 'Apex'",
                    "Zero financial intent or deceptive banking terminology"
                ),
                riskSignals = listOf(
                    RiskSignal("Non-Infringing Category", 15, "Health & Fitness store category; no customer confusion")
                ),
                matchedOfficialPackage = "com.apexbank.mobile",
                isOfficialAsset = false,
                analystNotes = "Cleared by analyst Marcus. Benign fitness app."
            ),
            AppDetection(
                id = "app_201",
                brandId = "org_shopnova",
                appName = "ShopNova: Shopping & Mega Discounts",
                platform = "Android",
                store = "Aptoide / Unofficial Stores",
                publisher = "Nova Discounts Studio",
                officialPublisherExpected = "ShopNova Global Retail Ltd",
                packageName = "com.nova.discounts.store",
                storeUrl = "https://aptoide.com/shopnova-discounts/com.nova.discounts.store",
                iconDescription = "ShopNova logo modified with neon discount sparkles",
                riskScore = 91,
                riskLevel = RiskLevel.CRITICAL,
                confidence = 95,
                status = DetectionStatus.NEW,
                detectedAt = "2026-10-12 04:10",
                nameSimilarity = 90,
                brandingSimilarity = 88,
                isPublisherMismatch = true,
                suspiciousPermissions = listOf(
                    "android.permission.RECEIVE_SMS",
                    "android.permission.INTERNET (Unauthorized payment gateway redirection)"
                ),
                detectionReasons = listOf(
                    "Publisher mismatch: Unauthorized developer 'Nova Discounts Studio'",
                    "Fake shopping portal that captures payment card credentials upon mock checkout"
                ),
                riskSignals = listOf(
                    RiskSignal("Payment Interception", 38, "Redirects payment iframe to fake credit card processor", true),
                    RiskSignal("Publisher Spoofing", 33, "Not in authorized publisher registry: 'ShopNova Global Retail Ltd'"),
                    RiskSignal("Unauthorized Asset Reproduction", 20, "Steals official product catalog and trademarks")
                ),
                matchedOfficialPackage = "com.shopnova.marketplace",
                isOfficialAsset = false,
                analystNotes = "Aptoide store DMCA abuse notice dispatched."
            )
        )
        appDao.insertAll(appList.map { it.toEntity() })

        // 4. Threat Indicators (IOCs)
        val indicators = listOf(
            ThreatIndicator("ioc_1", "Phishing Domain", "hxxps://apex-security-help[.]site", 96, "2026-10-12 06:14", "Operation ApexPhish", "ApexBank", "Active"),
            ThreatIndicator("ioc_2", "Rogue APK Hash", "SHA256: 4a9f3b20e1189c4458f001decae193928410293a", 98, "2026-10-11 19:40", "Operation ApexPhish", "ApexBank", "Active"),
            ThreatIndicator("ioc_3", "Impersonator Handle", "@ApexB0nk_Support", 96, "2026-10-12 06:14", "Operation ApexPhish", "ApexBank", "Active"),
            ThreatIndicator("ioc_4", "C2 IP Server", "194.38.20.12 (Host: Bulletproof RU)", 94, "2026-10-11 19:40", "Operation ApexPhish", "ApexBank", "Active"),
            ThreatIndicator("ioc_5", "Phishing Domain", "hxxps://shopn0va-clearance-sale[.]shop", 94, "2026-10-12 05:50", "Nova Outlet Ring", "ShopNova", "Active"),
            ThreatIndicator("ioc_6", "Credential Drop", "hxxps://apex-talent-review[.]org", 65, "2026-10-11 18:22", "Apex Fake HR Scheme", "ApexBank", "Active")
        )
        threatDao.insertIndicators(indicators.map { it.toEntity() })

        // 5. Threat Campaigns
        val campaigns = listOf(
            ThreatCampaign(
                id = "camp_1",
                name = "Operation ApexPhish: Coordinated Social & Trojan Ring",
                targetIndustry = "Fintech & Banking",
                threatActor = "FIN-79 Affiliated Syndicate",
                severity = RiskLevel.CRITICAL,
                activeIndicatorsCount = 8,
                description = "Synchronized digital impersonation campaign combining look-alike X (Twitter) support accounts, SMS phishing redirects, and a rogue Android APK with SMS interception capabilities to bypass MFA on ApexBank accounts.",
                tactics = listOf("Homoglyph typosquatting", "Social reply hijacking", "Overlay Android Trojan", "MFA interception"),
                affectedBrands = listOf("ApexBank", "Apex Pay"),
                firstDetected = "2026-10-09"
            ),
            ThreatCampaign(
                id = "camp_2",
                name = "ShopNova Fake Clearance & Outlet Ring",
                targetIndustry = "E-Commerce",
                threatActor = "SilentCartel Cyber Syndicate",
                severity = RiskLevel.HIGH,
                activeIndicatorsCount = 5,
                description = "Widespread consumer scam spawning dozens of fake Instagram flash-sale profiles and rogue mobile checkout apps harvesting CVV data from unsuspecting shoppers seeking holiday discounts.",
                tactics = listOf("Flash sale lures", "Card skimming iframes", "Rogue APK downloads"),
                affectedBrands = listOf("ShopNova"),
                firstDetected = "2026-10-10"
            )
        )
        threatDao.insertCampaigns(campaigns.map { it.toEntity() })

        // 6. Investigation Cases
        val cases = listOf(
            InvestigationCase(
                id = "CASE-2026-081",
                title = "Critical Social & Rogue APK Impersonation Cluster (ApexBank)",
                threatType = "Multi-Vector Brand Impersonation",
                targetBrand = "ApexBank",
                riskScore = 96,
                riskLevel = RiskLevel.CRITICAL,
                status = DetectionStatus.INVESTIGATING,
                assignedAnalyst = "Elena Rostova (Senior Threat Analyst)",
                createdAt = "2026-10-12 06:20",
                updatedAt = "2026-10-12 07:15",
                evidenceList = listOf(
                    "Captured profile screenshot of @ApexB0nk_Support with deceptive support bio",
                    "Network packet capture showing data exfiltration to 194.38.20.12",
                    "Decompiled Android manifest highlighting dangerous SMS overlay permissions",
                    "WHOIS record for apex-security-help.site registered via privacy proxy 2 days ago"
                ),
                analystNotes = listOf(
                    "06:30 - Initial alert triaged. Verified that @ApexB0nk_Support is replying to customer tweets.",
                    "06:55 - Extracted payload URL: reveals automated phishing kit mimicking ApexBank Online Banking.",
                    "07:15 - Prepared platform takedown packages for X Trust & Safety and Google Play Security."
                ),
                relatedEntityId = "soc_101",
                timeline = listOf(
                    CaseTimelineEvent("2026-10-12 06:14", "SENTRIX Detection Engine", "Threat Identified", "Flagged @ApexB0nk_Support with Risk Score 96 (Homoglyph + Phishing link)"),
                    CaseTimelineEvent("2026-10-12 06:20", "System Rule", "Case Created", "Auto-generated investigation CASE-2026-081 due to Critical severity"),
                    CaseTimelineEvent("2026-10-12 06:30", "Elena Rostova", "Analyst Assigned", "Case assigned to Tier 3 Threat Intelligence Lead"),
                    CaseTimelineEvent("2026-10-12 07:10", "IRIS AI Copilot", "Takedown Generated", "IRIS synthesized legal takedown request with trademark certificates")
                )
            ),
            InvestigationCase(
                id = "CASE-2026-080",
                title = "Unauthorized App Store Listing 'ApexBank Fast Loans'",
                threatType = "Rogue Mobile Application",
                targetBrand = "ApexBank",
                riskScore = 95,
                riskLevel = RiskLevel.CRITICAL,
                status = DetectionStatus.INVESTIGATING,
                assignedAnalyst = "Marcus Vance (Mobile Security Specialist)",
                createdAt = "2026-10-12 05:30",
                updatedAt = "2026-10-12 06:45",
                evidenceList = listOf(
                    "Google Play package com.apexbank.loans.fastpay metadata dump",
                    "Developer 'Apex Global Financial Services Ltd' corporate registry mismatch",
                    "Static APK analysis: requests READ_SMS and SYSTEM_ALERT_WINDOW"
                ),
                analystNotes = listOf(
                    "App masquerades as official credit product. 1,000+ downloads reported.",
                    "Dispatched emergency DMCA notice to Google Legal team."
                ),
                relatedEntityId = "app_101",
                timeline = listOf(
                    CaseTimelineEvent("2026-10-12 05:20", "SENTRIX Mobile Crawler", "App Detected", "Detected rogue listing on Google Play Store"),
                    CaseTimelineEvent("2026-10-12 05:35", "Marcus Vance", "Static Analysis Completed", "Confirmed malicious overlay Trojan permissions")
                )
            )
        )
        caseDao.insertAll(cases.map { it.toEntity() })

        // 7. Remediation Tasks (Takedowns)
        val tasks = listOf(
            RemediationTask(
                id = "TKD-901",
                caseId = "CASE-2026-081",
                targetEntity = "@ApexB0nk_Support (X / Twitter)",
                platform = "X (Twitter)",
                legalNoticeType = "Platform Impersonation & Trademark Violation",
                status = RemediationStatus.SUBMITTED_TO_PLATFORM,
                submissionDate = "2026-10-12 07:15",
                expectedSla = "Within 4 Hours",
                takedownDraftContent = """
FORMAL NOTICE OF TRADEMARK INFRINGEMENT & BRAND IMPERSONATION

To: X Trust & Safety / Legal Department
Date: October 12, 2026
Complainant: Apex Financial Group Inc. (c/o Team CIPHER / SENTRIX DRP)

Notice is hereby provided that the account @ApexB0nk_Support is willfully infringing upon registered trademarks of Apex Financial Group Inc. (USPTO Reg #5,892,110) and engaging in deceptive credential harvesting targeting banking customers.

1. INFRINGING ASSET:
   - Handle: @ApexB0nk_Support
   - URL: https://x.com/ApexB0nk_Support
   - Infringement Type: Character substitution / Homoglyph attack ('0' for 'o'), unauthorized use of corporate insignia, malicious phishing URL distribution (hxxps://apex-security-help[.]site).

2. LEGITIMATE TRADEMARK OWNER:
   - Entity: Apex Financial Group Inc.
   - Official Account: @ApexBank (Verified Enterprise Gold)
   - Official Domain: https://apexbank.com

We hereby request the immediate suspension and removal of @ApexB0nk_Support to prevent ongoing consumer financial fraud.

Submitted via SENTRIX Digital Risk Protection Platform.
                """.trimIndent(),
                evidenceExportUrl = "https://sentrix.security/exports/evidence-TKD-901.pdf"
            ),
            RemediationTask(
                id = "TKD-902",
                caseId = "CASE-2026-080",
                targetEntity = "com.apexbank.loans.fastpay (Google Play Store)",
                platform = "Google Play",
                legalNoticeType = "Google Play Security Takedown & Trademark Infringement",
                status = RemediationStatus.IN_REVIEW,
                submissionDate = "2026-10-12 06:45",
                expectedSla = "Within 6 Hours",
                takedownDraftContent = """
APPLICATION TAKEDOWN DEMAND UNDER GOOGLE DEVELOPER PROGRAM POLICIES

To: Google Play Security & Intellectual Property Team
Case Reference: TKD-902 / DRP-APEX-080

1. INFRINGING APPLICATION:
   - App Title: ApexBank: Fast Mobile Loans & Pay
   - Package Name: com.apexbank.loans.fastpay
   - Store Listing: https://play.google.com/store/apps/details?id=com.apexbank.loans.fastpay
   - Listed Developer: Apex Global Financial Services Ltd (UNAUTHORIZED)

2. POLICY VIOLATIONS:
   - Impersonation Policy (Section 4.1): App falsely claims affiliation with Apex Financial Group Inc.
   - Financial Services Policy: Operates deceptive banking services without regulatory charter.
   - Malware / Dangerous Permissions: Deceptive use of SYSTEM_ALERT_WINDOW for phishing overlays.

Demand is made for immediate delisting and revocation of developer credentials.

Certified by: SENTRIX Legal Remediations Engine
                """.trimIndent(),
                evidenceExportUrl = "https://sentrix.security/exports/evidence-TKD-902.pdf"
            )
        )
        remediationDao.insertAll(tasks.map { it.toEntity() })

        // 8. Notifications
        val notifs = listOf(
            NotificationItem(
                id = "notif_1",
                title = "Critical Social Threat Detected",
                message = "New impersonation profile @ApexB0nk_Support detected with Risk Score 96.",
                timestamp = "12 min ago",
                riskLevel = RiskLevel.CRITICAL,
                isRead = false,
                relatedId = "soc_101"
            ),
            NotificationItem(
                id = "notif_2",
                title = "Rogue App Store Listing Flagged",
                message = "Fake banking app 'ApexBank: Fast Mobile Loans' detected on Google Play with SMS overlay trojan signals.",
                timestamp = "1 hr ago",
                riskLevel = RiskLevel.CRITICAL,
                isRead = false,
                relatedId = "app_101"
            ),
            NotificationItem(
                id = "notif_3",
                title = "Campaign Correlated",
                message = "Operation ApexPhish linked 8 indicators across Twitter, Telegram, and APK mirrors.",
                timestamp = "2 hrs ago",
                riskLevel = RiskLevel.HIGH,
                isRead = true,
                relatedId = "camp_1"
            ),
            NotificationItem(
                id = "notif_4",
                title = "Takedown Status Update",
                message = "Takedown TKD-901 submitted to X Trust & Safety. SLA response expected within 4 hours.",
                timestamp = "3 hrs ago",
                riskLevel = RiskLevel.MEDIUM,
                isRead = true,
                relatedId = "TKD-901"
            )
        )
        notifDao.insertNotifications(notifs.map { it.toEntity() })

        // 9. Initial Audit Trail
        val initialLogs = listOf(
            AuditLog("log_0", currentTime(), "System Sentinel", "Engine Initialized", "SENTRIX continuous risk scanner initialized with 4 enterprise brand tenants in SQLite Room Database", "127.0.0.1"),
            AuditLog("log_1", "2026-10-12 06:14", "Detection Engine", "Social Threat Detected", "Flagged look-alike profile @ApexB0nk_Support (Score 96)", "10.14.88.241"),
            AuditLog("log_2", "2026-10-12 06:30", "Elena Rostova", "Investigation Assigned", "Created case CASE-2026-081 for multi-vector threat", "10.14.88.241"),
            AuditLog("log_3", "2026-10-12 07:15", "IRIS AI Sentinel", "Takedown Submitted", "Generated and submitted TKD-901 to platform abuse portal", "10.14.88.241")
        )
        notifDao.insertAuditLogs(initialLogs.map { it.toEntity() })
    }

    private fun defaultFallbackBrand() = BrandProfile(
        id = "org_apex",
        name = "ApexBank",
        industry = "Fintech & Tier-1 Digital Banking",
        description = "ApexBank is a premier digital-first financial institution.",
        officialDomains = listOf("apexbank.com"),
        officialSocials = emptyList(),
        officialMobileApps = emptyList(),
        authorizedPublishers = listOf("Apex Financial Group Inc."),
        brandKeywords = listOf("ApexBank"),
        brandAliases = listOf("Apex"),
        knownLegitimateAssets = emptyList(),
        lastUpdated = "2026-10-12"
    )
}
