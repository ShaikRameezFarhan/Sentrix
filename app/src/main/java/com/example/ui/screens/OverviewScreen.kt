package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.PlatformIconBadge
import com.example.ui.components.RiskBadge
import com.example.ui.components.StatCard
import com.example.ui.theme.*

@Composable
fun OverviewScreen(
    brandProfile: BrandProfile,
    socialDetections: List<SocialDetection>,
    appDetections: List<AppDetection>,
    investigations: List<InvestigationCase>,
    remediationTasks: List<RemediationTask>,
    onNavigateToSocial: (String?) -> Unit,
    onNavigateToApps: (String?) -> Unit,
    onNavigateToInvestigations: (String?) -> Unit,
    onNavigateToRemediation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalDetections = socialDetections.size + appDetections.size
    val criticalCount = socialDetections.count { it.riskLevel == RiskLevel.CRITICAL && !it.isOfficialAsset } +
            appDetections.count { it.riskLevel == RiskLevel.CRITICAL && !it.isOfficialAsset }
    val activeInvestigationsCount = investigations.count { it.status == DetectionStatus.INVESTIGATING }
    val activeTakedownsCount = remediationTasks.count { it.status != RemediationStatus.VERIFIED_OFFLINE }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Executive Summary Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCard)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "EXECUTIVE SECURITY OPERATIONS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = VividLime,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = brandProfile.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Text(
                                text = brandProfile.industry,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SafeEmerald.copy(alpha = 0.15f))
                                .border(1.dp, SafeEmerald.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(SafeEmerald)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DEFENSE ACTIVE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SafeEmerald
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Continuous automated risk scanners are actively monitoring official domains, social channels, and major mobile app store repositories for trademark violations, look-alike usernames, and rogue banking Trojans.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // KPI Stat Cards Grid (2x2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Monitored Assets",
                        value = "${brandProfile.officialSocials.size + brandProfile.officialMobileApps.size + brandProfile.officialDomains.size}",
                        subtitle = "Verified Brand Footprint",
                        accentColor = VividLime,
                        icon = Icons.Default.VerifiedUser,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Critical Incidents",
                        value = "$criticalCount",
                        subtitle = "Urgent Triage Required",
                        accentColor = CriticalRed,
                        icon = Icons.Default.Warning,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Active Cases",
                        value = "$activeInvestigationsCount",
                        subtitle = "Tier 3 Analyst Investigation",
                        accentColor = WarningAmber,
                        icon = Icons.Default.Search,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Mean Takedown Time",
                        value = "3.8 hrs",
                        subtitle = "Rapid Platform Enforcement",
                        accentColor = SafeEmerald,
                        icon = Icons.Default.Speed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section Title: Actionable High Priority Threats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "HIGH-PRIORITY THREAT QUEUE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = 0.8.sp
                )

                TextButton(
                    onClick = { onNavigateToSocial(null) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "View All ($totalDetections)",
                        fontSize = 12.sp,
                        color = VividLime,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Top Social Threats
        items(socialDetections.filter { it.riskLevel == RiskLevel.CRITICAL && !it.isOfficialAsset }.take(2)) { threat ->
            ThreatSummaryCard(
                type = "SOCIAL IMPERSONATION",
                title = threat.profileName,
                subtitle = "${threat.username} (${threat.platform})",
                platform = threat.platform,
                riskScore = threat.riskScore,
                riskLevel = threat.riskLevel,
                lookAlike = threat.lookAlikePattern,
                onClick = { onNavigateToSocial(threat.id) }
            )
        }

        // Top App Store Threats
        items(appDetections.filter { it.riskLevel == RiskLevel.CRITICAL && !it.isOfficialAsset }.take(2)) { app ->
            ThreatSummaryCard(
                type = "ROGUE MOBILE APP",
                title = app.appName,
                subtitle = "Publisher: ${app.publisher} (${app.store})",
                platform = app.store,
                riskScore = app.riskScore,
                riskLevel = app.riskLevel,
                lookAlike = "Publisher Mismatch + Dangerous Permissions",
                onClick = { onNavigateToApps(app.id) }
            )
        }

        // Quick Remediation Pipeline Preview
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToRemediation() }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SafeEmerald.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = SafeEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Platform Remediation & Takedowns",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "$activeTakedownsCount active DMCA/trademark enforcement requests in review",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Navigate",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThreatSummaryCard(
    type: String,
    title: String,
    subtitle: String,
    platform: String,
    riskScore: Int,
    riskLevel: RiskLevel,
    lookAlike: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp)
            .testTag("threat_summary_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PlatformIconBadge(platform = platform)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = type,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VividLime,
                        letterSpacing = 0.8.sp
                    )
                }
                RiskBadge(riskLevel = riskLevel, riskScore = riskScore)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceDark)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Pattern: $lookAlike",
                    fontSize = 10.sp,
                    color = WarningAmber
                )
            }
        }
    }
}
