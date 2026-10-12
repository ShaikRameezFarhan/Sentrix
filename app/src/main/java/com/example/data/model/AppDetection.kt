package com.example.data.model

data class AppDetection(
    val id: String,
    val brandId: String,
    val appName: String,
    val platform: String,          // "Android", "iOS"
    val store: String,             // "Google Play", "Apple App Store", "APKPure", "Aptoide"
    val publisher: String,
    val officialPublisherExpected: String,
    val packageName: String,
    val storeUrl: String,
    val iconDescription: String,
    val riskScore: Int,            // 0 - 100
    val riskLevel: RiskLevel,
    val confidence: Int,           // Range: 0 to 100 index
    val status: DetectionStatus,
    val detectedAt: String,
    val nameSimilarity: Int,       // Range: 0 to 100 score
    val brandingSimilarity: Int,   // Range: 0 to 100 score
    val isPublisherMismatch: Boolean,
    val suspiciousPermissions: List<String>,
    val detectionReasons: List<String>,
    val riskSignals: List<RiskSignal>,
    val matchedOfficialPackage: String,
    val isOfficialAsset: Boolean = false,
    val analystNotes: String = "",
    val resolutionReason: String = ""
)
