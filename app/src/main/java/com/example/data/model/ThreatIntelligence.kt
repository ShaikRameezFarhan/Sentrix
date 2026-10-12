package com.example.data.model

data class ThreatIndicator(
    val id: String,
    val type: String, // "Phishing Domain", "Rogue APK Hash", "Impersonator Handle", "C2 IP"
    val value: String,
    val riskScore: Int,
    val firstSeen: String,
    val associatedCampaign: String,
    val targetBrand: String,
    val status: String // "Active", "Mitigated", "Sinkholed"
)

data class ThreatCampaign(
    val id: String,
    val name: String,
    val targetIndustry: String,
    val threatActor: String,
    val severity: RiskLevel,
    val activeIndicatorsCount: Int,
    val description: String,
    val tactics: List<String>,
    val affectedBrands: List<String>,
    val firstDetected: String
)

data class CaseTimelineEvent(
    val timestamp: String,
    val actor: String,
    val title: String,
    val detail: String
)

data class InvestigationCase(
    val id: String,
    val title: String,
    val threatType: String, // "Social Impersonation", "Rogue Mobile App", "Brand Phishing Ring"
    val targetBrand: String,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val status: DetectionStatus,
    val assignedAnalyst: String,
    val createdAt: String,
    val updatedAt: String,
    val evidenceList: List<String>,
    val analystNotes: List<String>,
    val relatedEntityId: String,
    val timeline: List<CaseTimelineEvent>
)

enum class RemediationStatus {
    DRAFTED,
    SUBMITTED_TO_PLATFORM,
    IN_REVIEW,
    HOST_ACKNOWLEDGED,
    PLATFORM_REMOVED,
    VERIFIED_OFFLINE
}

data class RemediationTask(
    val id: String,
    val caseId: String,
    val targetEntity: String,
    val platform: String,
    val legalNoticeType: String, // "DMCA Copyright Notice", "Trademark Infringement Notice", "Google Play Security Takedown", "App Store Counterfeit Report"
    val status: RemediationStatus,
    val submissionDate: String,
    val expectedSla: String,
    val takedownDraftContent: String,
    val evidenceExportUrl: String
)

data class AuditLog(
    val id: String,
    val timestamp: String,
    val user: String,
    val action: String,
    val details: String,
    val ipAddress: String
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val riskLevel: RiskLevel,
    val isRead: Boolean = false,
    val relatedId: String = ""
)
