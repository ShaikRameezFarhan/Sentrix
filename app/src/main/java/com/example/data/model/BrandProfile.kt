package com.example.data.model

data class BrandProfile(
    val id: String,
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

data class OfficialSocialAsset(
    val platform: String,
    val handle: String,
    val profileUrl: String,
    val isVerified: Boolean = true,
    val followerCount: String
)

data class OfficialAppAsset(
    val name: String,
    val platform: String, // "Android", "iOS"
    val appId: String,    // e.g. "com.apexbank.mobile" or "id1492048201"
    val publisher: String,
    val storeUrl: String,
    val minRating: Double = 4.8
)
