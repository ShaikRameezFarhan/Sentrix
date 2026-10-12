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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InvestigationCase
import com.example.ui.components.RiskBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun InvestigationsScreen(
    cases: List<InvestigationCase>,
    initialSelectedCaseId: String? = null,
    modifier: Modifier = Modifier
) {
    var activeCase by remember {
        mutableStateOf(cases.find { it.id == initialSelectedCaseId } ?: cases.firstOrNull())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Module Header
        Column {
            Text(
                text = "CASE MANAGEMENT & INVESTIGATIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan,
                letterSpacing = 1.sp
            )
            Text(
                text = "Incident Dossiers & Forensic Timelines",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Cases Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            cases.forEach { c ->
                val isSelected = activeCase?.id == c.id
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) VividLime else SurfaceCard)
                        .border(1.dp, if (isSelected) VividLime else SurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { activeCase = c }
                        .padding(10.dp)
                ) {
                    Column {
                        Text(c.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) PrimaryDark else TextWhite)
                        Text(c.targetBrand, fontSize = 10.sp, color = if (isSelected) PrimaryDark.copy(alpha = 0.8f) else TextMuted)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (activeCase != null) {
            val c = activeCase!!
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Case Title Box
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(c.id, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                                RiskBadge(riskLevel = c.riskLevel, riskScore = c.riskScore)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(c.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Lead Assigned: ${c.assignedAnalyst}", fontSize = 11.sp, color = TextMuted)
                            Text("Target Organization: ${c.targetBrand} • Created ${c.createdAt}", fontSize = 10.sp, color = TextSubtle)
                        }
                    }
                }

                // Evidence Locker Box
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Folder, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("FORENSIC EVIDENCE LOCKER (${c.evidenceList.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            c.evidenceList.forEach { ev ->
                                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 2.dp)) {
                                    Icon(Icons.Default.AttachFile, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(ev, fontSize = 11.sp, color = TextWhite)
                                }
                            }
                        }
                    }
                }

                // Case Timeline Section
                item {
                    Text("INCIDENT CHRONOLOGY & AUDIT TRAIL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan, letterSpacing = 0.8.sp)
                }

                items(c.timeline) { event ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceDark)
                            .border(0.5.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(event.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                Text(event.timestamp, fontSize = 9.sp, color = TextSubtle)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Actor: ${event.actor}", fontSize = 10.sp, color = ElectricCyan)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(event.detail, fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}
