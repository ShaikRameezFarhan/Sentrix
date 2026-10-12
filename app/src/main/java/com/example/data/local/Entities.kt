package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.*

@Entity(tableName = "brand_profiles")
data class BrandProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val industry: String,
    val description: String,
    val officialDomains: List<String>,
    val officialSocials: List<OfficialSocialAsset>,
    val officialMobileApps: List<OfficialAppAsset>,
    val authorizedPublishers: List<String>,
    val brandKeywords: List<String>,
    val brandAliases: List<String>,
    val knownLegitimateAssets: List<String>,
    val lastUpdated: String
)

fun BrandProfileEntity.toDomain() = BrandProfile(
    id = id,
    name = name,
    industry = industry,
    description = description,
    officialDomains = officialDomains,
    officialSocials = officialSocials,
    officialMobileApps = officialMobileApps,
    authorizedPublishers = authorizedPublishers,
    brandKeywords = brandKeywords,
    brandAliases = brandAliases,
    knownLegitimateAssets = knownLegitimateAssets,
    lastUpdated = lastUpdated
)

fun BrandProfile.toEntity() = BrandProfileEntity(
    id = id,
    name = name,
    industry = industry,
    description = description,
    officialDomains = officialDomains,
    officialSocials = officialSocials,
    officialMobileApps = officialMobileApps,
    authorizedPublishers = authorizedPublishers,
    brandKeywords = brandKeywords,
    brandAliases = brandAliases,
    knownLegitimateAssets = knownLegitimateAssets,
    lastUpdated = lastUpdated
)

@Entity(tableName = "social_detections")
data class SocialDetectionEntity(
    @PrimaryKey val id: String,
    val brandId: String,
    val platform: String,
    val profileName: String,
    val username: String,
    val profileUrl: String,
    val avatarDescription: String,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val confidence: Int,
    val status: DetectionStatus,
    val detectedAt: String,
    val detectionReasons: List<String>,
    val riskSignals: List<RiskSignal>,
    val lookAlikePattern: String,
    val suspiciousLinks: List<String>,
    val matchedOfficialHandle: String,
    val isOfficialAsset: Boolean,
    val analystNotes: String,
    val resolutionReason: String
)

fun SocialDetectionEntity.toDomain() = SocialDetection(
    id = id,
    brandId = brandId,
    platform = platform,
    profileName = profileName,
    username = username,
    profileUrl = profileUrl,
    avatarDescription = avatarDescription,
    riskScore = riskScore,
    riskLevel = riskLevel,
    confidence = confidence,
    status = status,
    detectedAt = detectedAt,
    detectionReasons = detectionReasons,
    riskSignals = riskSignals,
    lookAlikePattern = lookAlikePattern,
    suspiciousLinks = suspiciousLinks,
    matchedOfficialHandle = matchedOfficialHandle,
    isOfficialAsset = isOfficialAsset,
    analystNotes = analystNotes,
    resolutionReason = resolutionReason
)

fun SocialDetection.toEntity() = SocialDetectionEntity(
    id = id,
    brandId = brandId,
    platform = platform,
    profileName = profileName,
    username = username,
    profileUrl = profileUrl,
    avatarDescription = avatarDescription,
    riskScore = riskScore,
    riskLevel = riskLevel,
    confidence = confidence,
    status = status,
    detectedAt = detectedAt,
    detectionReasons = detectionReasons,
    riskSignals = riskSignals,
    lookAlikePattern = lookAlikePattern,
    suspiciousLinks = suspiciousLinks,
    matchedOfficialHandle = matchedOfficialHandle,
    isOfficialAsset = isOfficialAsset,
    analystNotes = analystNotes,
    resolutionReason = resolutionReason
)

@Entity(tableName = "app_detections")
data class AppDetectionEntity(
    @PrimaryKey val id: String,
    val brandId: String,
    val appName: String,
    val platform: String,
    val store: String,
    val publisher: String,
    val officialPublisherExpected: String,
    val packageName: String,
    val storeUrl: String,
    val iconDescription: String,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val confidence: Int,
    val status: DetectionStatus,
    val detectedAt: String,
    val nameSimilarity: Int,
    val brandingSimilarity: Int,
    val isPublisherMismatch: Boolean,
    val suspiciousPermissions: List<String>,
    val detectionReasons: List<String>,
    val riskSignals: List<RiskSignal>,
    val matchedOfficialPackage: String,
    val isOfficialAsset: Boolean,
    val analystNotes: String,
    val resolutionReason: String
)

fun AppDetectionEntity.toDomain() = AppDetection(
    id = id,
    brandId = brandId,
    appName = appName,
    platform = platform,
    store = store,
    publisher = publisher,
    officialPublisherExpected = officialPublisherExpected,
    packageName = packageName,
    storeUrl = storeUrl,
    iconDescription = iconDescription,
    riskScore = riskScore,
    riskLevel = riskLevel,
    confidence = confidence,
    status = status,
    detectedAt = detectedAt,
    nameSimilarity = nameSimilarity,
    brandingSimilarity = brandingSimilarity,
    isPublisherMismatch = isPublisherMismatch,
    suspiciousPermissions = suspiciousPermissions,
    detectionReasons = detectionReasons,
    riskSignals = riskSignals,
    matchedOfficialPackage = matchedOfficialPackage,
    isOfficialAsset = isOfficialAsset,
    analystNotes = analystNotes,
    resolutionReason = resolutionReason
)

fun AppDetection.toEntity() = AppDetectionEntity(
    id = id,
    brandId = brandId,
    appName = appName,
    platform = platform,
    store = store,
    publisher = publisher,
    officialPublisherExpected = officialPublisherExpected,
    packageName = packageName,
    storeUrl = storeUrl,
    iconDescription = iconDescription,
    riskScore = riskScore,
    riskLevel = riskLevel,
    confidence = confidence,
    status = status,
    detectedAt = detectedAt,
    nameSimilarity = nameSimilarity,
    brandingSimilarity = brandingSimilarity,
    isPublisherMismatch = isPublisherMismatch,
    suspiciousPermissions = suspiciousPermissions,
    detectionReasons = detectionReasons,
    riskSignals = riskSignals,
    matchedOfficialPackage = matchedOfficialPackage,
    isOfficialAsset = isOfficialAsset,
    analystNotes = analystNotes,
    resolutionReason = resolutionReason
)

@Entity(tableName = "investigation_cases")
data class InvestigationCaseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val threatType: String,
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

fun InvestigationCaseEntity.toDomain() = InvestigationCase(
    id = id,
    title = title,
    threatType = threatType,
    targetBrand = targetBrand,
    riskScore = riskScore,
    riskLevel = riskLevel,
    status = status,
    assignedAnalyst = assignedAnalyst,
    createdAt = createdAt,
    updatedAt = updatedAt,
    evidenceList = evidenceList,
    analystNotes = analystNotes,
    relatedEntityId = relatedEntityId,
    timeline = timeline
)

fun InvestigationCase.toEntity() = InvestigationCaseEntity(
    id = id,
    title = title,
    threatType = threatType,
    targetBrand = targetBrand,
    riskScore = riskScore,
    riskLevel = riskLevel,
    status = status,
    assignedAnalyst = assignedAnalyst,
    createdAt = createdAt,
    updatedAt = updatedAt,
    evidenceList = evidenceList,
    analystNotes = analystNotes,
    relatedEntityId = relatedEntityId,
    timeline = timeline
)

@Entity(tableName = "remediation_tasks")
data class RemediationTaskEntity(
    @PrimaryKey val id: String,
    val caseId: String,
    val targetEntity: String,
    val platform: String,
    val legalNoticeType: String,
    val status: RemediationStatus,
    val submissionDate: String,
    val expectedSla: String,
    val takedownDraftContent: String,
    val evidenceExportUrl: String
)

fun RemediationTaskEntity.toDomain() = RemediationTask(
    id = id,
    caseId = caseId,
    targetEntity = targetEntity,
    platform = platform,
    legalNoticeType = legalNoticeType,
    status = status,
    submissionDate = submissionDate,
    expectedSla = expectedSla,
    takedownDraftContent = takedownDraftContent,
    evidenceExportUrl = evidenceExportUrl
)

fun RemediationTask.toEntity() = RemediationTaskEntity(
    id = id,
    caseId = caseId,
    targetEntity = targetEntity,
    platform = platform,
    legalNoticeType = legalNoticeType,
    status = status,
    submissionDate = submissionDate,
    expectedSla = expectedSla,
    takedownDraftContent = takedownDraftContent,
    evidenceExportUrl = evidenceExportUrl
)

@Entity(tableName = "threat_indicators")
data class ThreatIndicatorEntity(
    @PrimaryKey val id: String,
    val type: String,
    val value: String,
    val riskScore: Int,
    val firstSeen: String,
    val associatedCampaign: String,
    val targetBrand: String,
    val status: String
)

fun ThreatIndicatorEntity.toDomain() = ThreatIndicator(
    id = id,
    type = type,
    value = value,
    riskScore = riskScore,
    firstSeen = firstSeen,
    associatedCampaign = associatedCampaign,
    targetBrand = targetBrand,
    status = status
)

fun ThreatIndicator.toEntity() = ThreatIndicatorEntity(
    id = id,
    type = type,
    value = value,
    riskScore = riskScore,
    firstSeen = firstSeen,
    associatedCampaign = associatedCampaign,
    targetBrand = targetBrand,
    status = status
)

@Entity(tableName = "threat_campaigns")
data class ThreatCampaignEntity(
    @PrimaryKey val id: String,
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

fun ThreatCampaignEntity.toDomain() = ThreatCampaign(
    id = id,
    name = name,
    targetIndustry = targetIndustry,
    threatActor = threatActor,
    severity = severity,
    activeIndicatorsCount = activeIndicatorsCount,
    description = description,
    tactics = tactics,
    affectedBrands = affectedBrands,
    firstDetected = firstDetected
)

fun ThreatCampaign.toEntity() = ThreatCampaignEntity(
    id = id,
    name = name,
    targetIndustry = targetIndustry,
    threatActor = threatActor,
    severity = severity,
    activeIndicatorsCount = activeIndicatorsCount,
    description = description,
    tactics = tactics,
    affectedBrands = affectedBrands,
    firstDetected = firstDetected
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey val id: String,
    val timestamp: String,
    val user: String,
    val action: String,
    val details: String,
    val ipAddress: String
)

fun AuditLogEntity.toDomain() = AuditLog(
    id = id,
    timestamp = timestamp,
    user = user,
    action = action,
    details = details,
    ipAddress = ipAddress
)

fun AuditLog.toEntity() = AuditLogEntity(
    id = id,
    timestamp = timestamp,
    user = user,
    action = action,
    details = details,
    ipAddress = ipAddress
)

@Entity(tableName = "notifications")
data class NotificationItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val riskLevel: RiskLevel,
    val isRead: Boolean,
    val relatedId: String
)

fun NotificationItemEntity.toDomain() = NotificationItem(
    id = id,
    title = title,
    message = message,
    timestamp = timestamp,
    riskLevel = riskLevel,
    isRead = isRead,
    relatedId = relatedId
)

fun NotificationItem.toEntity() = NotificationItemEntity(
    id = id,
    title = title,
    message = message,
    timestamp = timestamp,
    riskLevel = riskLevel,
    isRead = isRead,
    relatedId = relatedId
)
