package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.*
import org.json.JSONArray
import org.json.JSONObject

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        val list = mutableListOf<String>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            list.add(array.getString(i))
        }
        return list
    }

    @TypeConverter
    fun fromOfficialSocialList(value: List<OfficialSocialAsset>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach {
            val obj = JSONObject().apply {
                put("platform", it.platform)
                put("handle", it.handle)
                put("profileUrl", it.profileUrl)
                put("isVerified", it.isVerified)
                put("followerCount", it.followerCount)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toOfficialSocialList(value: String?): List<OfficialSocialAsset> {
        if (value.isNullOrBlank()) return emptyList()
        val list = mutableListOf<OfficialSocialAsset>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                OfficialSocialAsset(
                    platform = obj.getString("platform"),
                    handle = obj.getString("handle"),
                    profileUrl = obj.getString("profileUrl"),
                    isVerified = obj.optBoolean("isVerified", true),
                    followerCount = obj.optString("followerCount", "0")
                )
            )
        }
        return list
    }

    @TypeConverter
    fun fromOfficialAppList(value: List<OfficialAppAsset>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach {
            val obj = JSONObject().apply {
                put("name", it.name)
                put("platform", it.platform)
                put("appId", it.appId)
                put("publisher", it.publisher)
                put("storeUrl", it.storeUrl)
                put("minRating", it.minRating)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toOfficialAppList(value: String?): List<OfficialAppAsset> {
        if (value.isNullOrBlank()) return emptyList()
        val list = mutableListOf<OfficialAppAsset>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                OfficialAppAsset(
                    name = obj.getString("name"),
                    platform = obj.getString("platform"),
                    appId = obj.getString("appId"),
                    publisher = obj.getString("publisher"),
                    storeUrl = obj.getString("storeUrl"),
                    minRating = obj.optDouble("minRating", 4.5)
                )
            )
        }
        return list
    }

    @TypeConverter
    fun fromRiskSignalList(value: List<RiskSignal>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach {
            val obj = JSONObject().apply {
                put("category", it.category)
                put("score", it.score)
                put("description", it.description)
                put("isCritical", it.isCritical)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toRiskSignalList(value: String?): List<RiskSignal> {
        if (value.isNullOrBlank()) return emptyList()
        val list = mutableListOf<RiskSignal>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                RiskSignal(
                    category = obj.getString("category"),
                    score = obj.getInt("score"),
                    description = obj.getString("description"),
                    isCritical = obj.optBoolean("isCritical", false)
                )
            )
        }
        return list
    }

    @TypeConverter
    fun fromTimelineEventList(value: List<CaseTimelineEvent>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach {
            val obj = JSONObject().apply {
                put("timestamp", it.timestamp)
                put("actor", it.actor)
                put("title", it.title)
                put("detail", it.detail)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toTimelineEventList(value: String?): List<CaseTimelineEvent> {
        if (value.isNullOrBlank()) return emptyList()
        val list = mutableListOf<CaseTimelineEvent>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                CaseTimelineEvent(
                    timestamp = obj.getString("timestamp"),
                    actor = obj.getString("actor"),
                    title = obj.getString("title"),
                    detail = obj.getString("detail")
                )
            )
        }
        return list
    }

    @TypeConverter
    fun fromRiskLevel(value: RiskLevel?): String = value?.name ?: RiskLevel.LOW.name

    @TypeConverter
    fun toRiskLevel(value: String?): RiskLevel = try {
        RiskLevel.valueOf(value ?: "LOW")
    } catch (e: Exception) {
        RiskLevel.LOW
    }

    @TypeConverter
    fun fromDetectionStatus(value: DetectionStatus?): String = value?.name ?: DetectionStatus.NEW.name

    @TypeConverter
    fun toDetectionStatus(value: String?): DetectionStatus = try {
        DetectionStatus.valueOf(value ?: "NEW")
    } catch (e: Exception) {
        DetectionStatus.NEW
    }

    @TypeConverter
    fun fromRemediationStatus(value: RemediationStatus?): String = value?.name ?: RemediationStatus.DRAFTED.name

    @TypeConverter
    fun toRemediationStatus(value: String?): RemediationStatus = try {
        RemediationStatus.valueOf(value ?: "DRAFTED")
    } catch (e: Exception) {
        RemediationStatus.DRAFTED
    }
}
