package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDetection
import com.example.data.model.BrandProfile
import com.example.data.model.SocialDetection
import com.example.ui.components.StatCard
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(
    brandProfile: BrandProfile,
    socialDetections: List<SocialDetection>,
    appDetections: List<AppDetection>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Module Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ANALYTICS & EXECUTIVE REPORTING",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Risk Trends & SLA Metrics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Executive Security Audit exported (PDF)", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                    modifier = Modifier.testTag("export_report_btn")
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export Report", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                }
            }
        }

        // Metrics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Mean Time to Resolve",
                        value = "3.8 Hours",
                        subtitle = "Down from 48h benchmark",
                        accentColor = SafeEmerald,
                        icon = Icons.Default.Timer,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "False Positive Rate",
                        value = "2.4 / 100",
                        subtitle = "Safeguard Protected",
                        accentColor = ElectricCyan,
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Platform Success",
                        value = "95 / 100",
                        subtitle = "Takedown confirmed",
                        accentColor = WarningAmber,
                        icon = Icons.Default.Verified,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Brand Risk Reduction",
                        value = "-87 pts",
                        subtitle = "Month-over-month",
                        accentColor = VividLime,
                        icon = Icons.AutoMirrored.Filled.TrendingDown,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Platform Distribution Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "INCIDENT DISTRIBUTION BY REPOSITORY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    listOf(
                        "X (Twitter)" to 38,
                        "Google Play Store" to 24,
                        "Instagram" to 16,
                        "Telegram Channels" to 12,
                        "3rd-Party APK Mirrors" to 6,
                        "Apple App Store" to 4
                    ).forEach { (platform, count) ->
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(platform, fontSize = 11.sp, color = TextWhite)
                                Text("$count cases", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VividLime)
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { count / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (platform.contains("Play") || platform.contains("APK")) CriticalRed else VividLime,
                                trackColor = DarkNavyTrack,
                            )
                        }
                    }
                }
            }
        }

        // Executive Audit Certification
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = SafeEmerald, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SECURITY OPERATIONS AUDIT COMPLIANCE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafeEmerald
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "SENTRIX provides SOC 2 Type II and ISO 27001 verifiable audit logs for all digital risk protection operations. All automated and analyst actions are cryptographically signed.",
                        fontSize = 11.sp,
                        color = TextMuted,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
