package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ThreatCampaign
import com.example.data.model.ThreatIndicator
import com.example.ui.components.RiskBadge
import com.example.ui.theme.*

@Composable
fun ThreatIntelligenceScreen(
    campaigns: List<ThreatCampaign>,
    indicators: List<ThreatIndicator>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Campaigns, 1: Indicators (IOCs)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Module Header
        Column {
            Text(
                text = "THREAT INTELLIGENCE & CORRELATION",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan,
                letterSpacing = 1.sp
            )
            Text(
                text = "Campaign Clustering & Infrastructure IOCs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceCard,
            contentColor = ElectricCyan
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Coordinated Campaigns (${campaigns.size})", fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Threat Indicators / IOCs (${indicators.size})", fontSize = 12.sp) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(campaigns) { camp ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = camp.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    modifier = Modifier.weight(1f)
                                )
                                RiskBadge(riskLevel = camp.severity)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Threat Actor: ${camp.threatActor} • Sector: ${camp.targetIndustry}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = camp.description,
                                fontSize = 12.sp,
                                color = TextWhite,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "TACTICS & MITRE ATT&CK TECHNIQUES:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarningAmber
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                camp.tactics.forEach { tactic ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SurfaceDark)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(tactic, fontSize = 9.sp, color = ElectricCyan)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(indicators) { ioc ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(VividLimeDark)
                                            .border(0.5.dp, VividLime, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(ioc.type, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VividLime)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Campaign: ${ioc.associatedCampaign}", fontSize = 10.sp, color = TextSubtle)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ioc.value,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (ioc.riskScore >= 80) DangerCoral else TextWhite
                                )
                                Text("First observed: ${ioc.firstSeen} • Status: ${ioc.status}", fontSize = 10.sp, color = TextMuted)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CriticalRedBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("SCORE: ${ioc.riskScore}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF859B))
                            }
                        }
                    }
                }
            }
        }
    }
}
