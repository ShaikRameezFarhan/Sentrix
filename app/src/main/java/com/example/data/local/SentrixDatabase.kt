package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        BrandProfileEntity::class,
        SocialDetectionEntity::class,
        AppDetectionEntity::class,
        InvestigationCaseEntity::class,
        RemediationTaskEntity::class,
        ThreatIndicatorEntity::class,
        ThreatCampaignEntity::class,
        AuditLogEntity::class,
        NotificationItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SentrixDatabase : RoomDatabase() {

    abstract fun brandDao(): BrandDao
    abstract fun socialDetectionDao(): SocialDetectionDao
    abstract fun appDetectionDao(): AppDetectionDao
    abstract fun investigationCaseDao(): InvestigationCaseDao
    abstract fun remediationTaskDao(): RemediationTaskDao
    abstract fun threatDao(): ThreatDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: SentrixDatabase? = null

        fun getInstance(context: Context): SentrixDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SentrixDatabase::class.java,
                    "sentrix_drp.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
