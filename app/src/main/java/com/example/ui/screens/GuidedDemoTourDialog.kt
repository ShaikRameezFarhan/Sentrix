package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun GuidedDemoTourDialog(
    onNavigateToSection: (String) -> Unit,
    onResetDemo: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(VividLimeDark)
                            .border(1.dp, VividLime, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = VividLime, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SENTRIX — Team CIPHER",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }
                Text(
                    text = "Kampus.VC Digital Risk Protection Hackathon Walkthrough",
                    fontSize = 11.sp,
                    color = ElectricCyan
                )
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.heightIn(max = 420.dp)
            ) {
                item {
                    Text(
                        text = "Welcome Judges! SENTRIX is an enterprise B2B Digital Risk Protection SaaS platform that safeguards organizations against social media brand impersonation, look-alike typo squatting, and fraudulent rogue mobile applications.",
                        fontSize = 12.sp,
                        color = TextWhite,
                        lineHeight = 16.sp
                    )
                }

                item {
                    TourCard(
                        title = "1. Social Media Impersonation",
                        description = "Inspect fake profiles, homoglyph character substitutions ('0' for 'o'), deceptive keyword compounding ('_support', '_giveaway'), and side-by-side verification against official brand assets.",
                        actionLabel = "Open Social Monitoring",
                        onClick = {
                            onNavigateToSection("social")
                            onDismiss()
                        }
                    )
                }

                item {
                    TourCard(
                        title = "2. App Store & Mobile Trojans",
                        description = "Detect rogue Android & iOS applications, developer/publisher mismatches, and banking Trojan capabilities (SMS interception & screen overlay permissions).",
                        actionLabel = "Open App Store Monitoring",
                        onClick = {
                            onNavigateToSection("apps")
                            onDismiss()
                        }
                    )
                }

                item {
                    TourCard(
                        title = "3. Brand Profile & Whitelist Anchors",
                        description = "See how companies register legitimate assets, official handles, packages, and authorized developers. Note that verified assets are NEVER falsely flagged as threats.",
                        actionLabel = "Open Brand Profile",
                        onClick = {
                            onNavigateToSection("brand")
                            onDismiss()
                        }
                    )
                }

                item {
                    TourCard(
                        title = "4. IRIS AI Security Copilot",
                        description = "Interact with Team CIPHER's autonomous neural investigator. Ask IRIS to explain risk score mathematics, forensic telemetry, and generate legally-binding takedown notices.",
                        actionLabel = "Open IRIS Copilot",
                        onClick = {
                            onNavigateToSection("iris")
                            onDismiss()
                        }
                    )
                }

                item {
                    TourCard(
                        title = "5. Multi-Industry Organizations",
                        description = "Test multi-tenant capabilities across 4 realistic seeded brands: ApexBank (Fintech), ShopNova (E-Commerce), CloudMatrix (SaaS), and PulseWear (Health IoT).",
                        actionLabel = "Switch Tenants in Top Bar",
                        onClick = {
                            onNavigateToSection("overview")
                            onDismiss()
                        }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark)
            ) {
                Text("Start Evaluation", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onResetDemo()
                    onDismiss()
                }
            ) {
                Text("Reset Demo Data", fontSize = 12.sp, color = WarningAmber)
            }
        },
        containerColor = SurfaceCard,
        tonalElevation = 8.dp
    )
}

@Composable
private fun TourCard(
    title: String,
    description: String,
    actionLabel: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(actionLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(12.dp))
            }
        }
    }
}
