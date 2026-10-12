package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.detection.DetectionEngine
import com.example.data.model.BrandProfile
import com.example.data.model.OfficialAppAsset
import com.example.data.model.OfficialSocialAsset
import com.example.data.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SENTRIX", appName)
    }

    @Test
    fun `test homoglyph and lookalike detection`() {
        val findings = DetectionEngine.detectHomoglyphs("ApexB0nk_Support", "ApexBank")
        assertTrue("Should detect digit 0 homoglyph", findings.isNotEmpty())

        val pattern = DetectionEngine.analyzeLookAlikePattern("@ApexB0nk_Support", "ApexBank")
        assertTrue("Should flag homoglyph or affix pattern", pattern.contains("Homoglyph") || pattern.contains("keyword"))
    }

    @Test
    fun `test official asset safeguard`() {
        val testBrand = BrandProfile(
            id = "test_bank",
            name = "ApexBank",
            industry = "Banking",
            description = "Test",
            officialDomains = listOf("apexbank.com"),
            officialSocials = listOf(
                OfficialSocialAsset("X (Twitter)", "@ApexBank", "https://x.com/ApexBank", true, "1M")
            ),
            officialMobileApps = listOf(
                OfficialAppAsset("ApexBank Mobile", "Android", "com.apexbank.mobile", "Apex Financial Group", "https://play.google.com", 4.9)
            ),
            authorizedPublishers = listOf("Apex Financial Group"),
            brandKeywords = listOf("ApexBank"),
            brandAliases = listOf("Apex"),
            knownLegitimateAssets = listOf("Apex Official"),
            lastUpdated = "2026-10-12"
        )

        // Official handle evaluated against brand profile must receive score 0 and LOW risk
        val (score, level, _) = DetectionEngine.evaluateSocialProfile(
            username = "@ApexBank",
            displayName = "ApexBank",
            bio = "Official bank",
            linkUrl = "https://apexbank.com",
            brandProfile = testBrand
        )

        assertEquals(0, score)
        assertEquals(RiskLevel.LOW, level)
    }

    @Test
    fun `test room database insertion and retrieval`() = kotlinx.coroutines.test.runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = com.example.data.local.SentrixDatabase.getInstance(context)
        val brandDao = db.brandDao()

        val brand = com.example.data.local.BrandProfileEntity(
            id = "test_room_brand",
            name = "TestCorp",
            industry = "Cybersecurity",
            description = "Enterprise testing",
            officialDomains = listOf("testcorp.io"),
            officialSocials = emptyList(),
            officialMobileApps = emptyList(),
            authorizedPublishers = listOf("TestCorp Inc"),
            brandKeywords = listOf("TestCorp"),
            brandAliases = listOf("TC"),
            knownLegitimateAssets = emptyList(),
            lastUpdated = "2026-10-12"
        )

        brandDao.insertBrand(brand)
        val retrieved = brandDao.getBrandById("test_room_brand")
        assertEquals("TestCorp", retrieved?.name)
        assertEquals(listOf("testcorp.io"), retrieved?.officialDomains)
    }
}
