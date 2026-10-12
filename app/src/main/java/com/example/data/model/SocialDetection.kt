package com.example.data.model

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class DetectionStatus {
    NEW,
    INVESTIGATING,
    CONFIRMED,
    FALSE_POSITIVE,
    RESOLVED,
    DISMISSED
}

data class RiskSignal(
    val category: String, // e.g. "Name Similarity", "Homoglyph Attack", "Suspicious Link", "Domain Spoofing"
    val score: Int,      // 0-100 contribution
    val description: String,
    val isCritical: Boolean = false
)

data class SocialDetection(
    val id: String,
    val brandId: String,
    val platform: String,           // "X (Twitter)", "Instagram", "LinkedIn", "Facebook", "TikTok", "Telegram"
    val profileName: String,
    val username: String,
    val profileUrl: String,
    val avatarDescription: String,
    val riskScore: Int,             // 0 - 100
    val riskLevel: RiskLevel,
    val confidence: Int,            // Range: 0 to 100 index
    val status: DetectionStatus,
    val detectedAt: String,
    val detectionReasons: List<String>,
    val riskSignals: List<RiskSignal>,
    val lookAlikePattern: String,    // e.g. "Character substitution: '0' for 'o', inserted suffix '_support'"
    val suspiciousLinks: List<String>,
    val matchedOfficialHandle: String,
    val isOfficialAsset: Boolean = false, // Safeguard for verified demo accounts
    val analystNotes: String = "",
    val resolutionReason: String = ""
)
