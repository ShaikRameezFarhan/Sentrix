package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDetection
import com.example.data.model.BrandProfile
import com.example.data.model.SocialDetection
import com.example.ui.theme.*

@Composable
fun SocialSideBySideComparisonCard(
    detection: SocialDetection,
    brandProfile: BrandProfile,
    modifier: Modifier = Modifier
) {
    val officialAsset = brandProfile.officialSocials.find { it.platform == detection.platform }
        ?: brandProfile.officialSocials.firstOrNull()

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = WarningAmber,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "SIDE-BY-SIDE ASSET VERIFICATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = WarningAmber,
                letterSpacing = 0.8.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Suspicious Profile Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CriticalRedBg.copy(alpha = 0.4f))
                    .border(1.dp, CriticalRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(CriticalRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "FLAGGED SUSPECT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF859B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                ComparisonItem(label = "Handle", value = detection.username, isAlert = true)
                ComparisonItem(label = "Display Name", value = detection.profileName, isAlert = false)
                ComparisonItem(label = "Platform", value = detection.platform, isAlert = false)
                ComparisonItem(label = "Verification", value = "UNVERIFIED / FAKE", isAlert = true)
                ComparisonItem(label = "Bio Links", value = detection.suspiciousLinks.firstOrNull() ?: "None", isAlert = true)
            }

            // Authentic Brand Profile Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SafeEmeraldBg.copy(alpha = 0.3f))
                    .border(1.dp, SafeEmerald.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SafeEmerald,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "AUTHENTIC BRAND ASSET",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafeEmerald
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                ComparisonItem(label = "Handle", value = officialAsset?.handle ?: "@${brandProfile.name}", isAlert = false)
                ComparisonItem(label = "Display Name", value = brandProfile.name, isAlert = false)
                ComparisonItem(label = "Platform", value = officialAsset?.platform ?: detection.platform, isAlert = false)
                ComparisonItem(label = "Verification", value = "VERIFIED (Enterprise)", isAlert = false, customColor = SafeEmerald)
                ComparisonItem(label = "Domain", value = brandProfile.officialDomains.firstOrNull() ?: "apexbank.com", isAlert = false)
            }
        }
    }
}

@Composable
fun AppSideBySideComparisonCard(
    detection: AppDetection,
    brandProfile: BrandProfile,
    modifier: Modifier = Modifier
) {
    val officialApp = brandProfile.officialMobileApps.find { it.platform == detection.platform }
        ?: brandProfile.officialMobileApps.firstOrNull()

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = WarningAmber,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "SIDE-BY-SIDE APPLICATION AUDIT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = WarningAmber,
                letterSpacing = 0.8.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Suspicious App Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CriticalRedBg.copy(alpha = 0.4f))
                    .border(1.dp, CriticalRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(CriticalRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "FLAGGED APPLICATION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF859B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                ComparisonItem(label = "App Name", value = detection.appName, isAlert = false)
                ComparisonItem(label = "Publisher", value = detection.publisher, isAlert = detection.isPublisherMismatch)
                ComparisonItem(label = "Package ID", value = detection.packageName, isAlert = true)
                ComparisonItem(label = "Store", value = detection.store, isAlert = false)
                ComparisonItem(label = "Risk Permissions", value = "${detection.suspiciousPermissions.size} Dangerous Flags", isAlert = true)
            }

            // Authentic App Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SafeEmeraldBg.copy(alpha = 0.3f))
                    .border(1.dp, SafeEmerald.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SafeEmerald,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "AUTHENTIC REGISTERED APP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafeEmerald
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                ComparisonItem(label = "App Name", value = officialApp?.name ?: "${brandProfile.name} Official", isAlert = false)
                ComparisonItem(label = "Publisher", value = brandProfile.authorizedPublishers.firstOrNull() ?: "Authorized", isAlert = false, customColor = SafeEmerald)
                ComparisonItem(label = "Package ID", value = officialApp?.appId ?: "com.${brandProfile.name.lowercase()}.app", isAlert = false)
                ComparisonItem(label = "Store", value = officialApp?.platform ?: "Certified Store", isAlert = false)
                ComparisonItem(label = "Permissions", value = "Standard Whitelist", isAlert = false, customColor = SafeEmerald)
            }
        }
    }
}

@Composable
private fun ComparisonItem(
    label: String,
    value: String,
    isAlert: Boolean,
    customColor: Color? = null
) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            color = TextSubtle,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = customColor ?: if (isAlert) CriticalRed else TextWhite,
            maxLines = 2
        )
    }
}
