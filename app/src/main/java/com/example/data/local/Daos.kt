package com.example.data.local

import androidx.room.*
import com.example.data.model.DetectionStatus
import com.example.data.model.RemediationStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {
    @Query("SELECT * FROM brand_profiles")
    fun getAllBrands(): Flow<List<BrandProfileEntity>>

    @Query("SELECT * FROM brand_profiles WHERE id = :id")
    suspend fun getBrandById(id: String): BrandProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: BrandProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(brands: List<BrandProfileEntity>)

    @Update
    suspend fun updateBrand(brand: BrandProfileEntity)

    @Query("DELETE FROM brand_profiles WHERE id = :id")
    suspend fun deleteBrandById(id: String)

    @Query("DELETE FROM brand_profiles")
    suspend fun clearAll()
}

@Dao
interface SocialDetectionDao {
    @Query("SELECT * FROM social_detections ORDER BY riskScore DESC")
    fun getAllSocialDetections(): Flow<List<SocialDetectionEntity>>

    @Query("SELECT * FROM social_detections WHERE brandId = :brandId ORDER BY riskScore DESC")
    fun getSocialDetectionsByBrand(brandId: String): Flow<List<SocialDetectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetection(detection: SocialDetectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(detections: List<SocialDetectionEntity>)

    @Query("UPDATE social_detections SET status = :status, analystNotes = :notes, resolutionReason = :reason WHERE id = :id")
    suspend fun updateStatus(id: String, status: DetectionStatus, notes: String, reason: String)

    @Query("DELETE FROM social_detections WHERE brandId = :brandId")
    suspend fun deleteDetectionsByBrand(brandId: String)

    @Query("DELETE FROM social_detections")
    suspend fun clearAll()
}

@Dao
interface AppDetectionDao {
    @Query("SELECT * FROM app_detections ORDER BY riskScore DESC")
    fun getAllAppDetections(): Flow<List<AppDetectionEntity>>

    @Query("SELECT * FROM app_detections WHERE brandId = :brandId ORDER BY riskScore DESC")
    fun getAppDetectionsByBrand(brandId: String): Flow<List<AppDetectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: AppDetectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<AppDetectionEntity>)

    @Query("UPDATE app_detections SET status = :status, analystNotes = :notes, resolutionReason = :reason WHERE id = :id")
    suspend fun updateStatus(id: String, status: DetectionStatus, notes: String, reason: String)

    @Query("DELETE FROM app_detections WHERE brandId = :brandId")
    suspend fun deleteAppsByBrand(brandId: String)

    @Query("DELETE FROM app_detections")
    suspend fun clearAll()
}

@Dao
interface InvestigationCaseDao {
    @Query("SELECT * FROM investigation_cases ORDER BY createdAt DESC")
    fun getAllCases(): Flow<List<InvestigationCaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(caseItem: InvestigationCaseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cases: List<InvestigationCaseEntity>)

    @Query("DELETE FROM investigation_cases")
    suspend fun clearAll()
}

@Dao
interface RemediationTaskDao {
    @Query("SELECT * FROM remediation_tasks ORDER BY submissionDate DESC")
    fun getAllTasks(): Flow<List<RemediationTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: RemediationTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<RemediationTaskEntity>)

    @Query("UPDATE remediation_tasks SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: RemediationStatus)

    @Query("DELETE FROM remediation_tasks")
    suspend fun clearAll()
}

@Dao
interface ThreatDao {
    @Query("SELECT * FROM threat_indicators ORDER BY riskScore DESC")
    fun getAllIndicators(): Flow<List<ThreatIndicatorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndicators(indicators: List<ThreatIndicatorEntity>)

    @Query("SELECT * FROM threat_campaigns")
    fun getAllCampaigns(): Flow<List<ThreatCampaignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaigns(campaigns: List<ThreatCampaignEntity>)

    @Query("DELETE FROM threat_indicators")
    suspend fun clearIndicators()

    @Query("DELETE FROM threat_campaigns")
    suspend fun clearCampaigns()
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationItemEntity>)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("SELECT * FROM audit_logs ORDER BY id DESC LIMIT 50")
    fun getAllAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLogs(logs: List<AuditLogEntity>)

    @Query("DELETE FROM notifications")
    suspend fun clearNotifications()

    @Query("DELETE FROM audit_logs")
    suspend fun clearAuditLogs()
}
