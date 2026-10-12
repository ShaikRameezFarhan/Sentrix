package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RemediationStatus
import com.example.data.model.RemediationTask
import com.example.ui.theme.*

@Composable
fun RemediationScreen(
    tasks: List<RemediationTask>,
    onUpdateRemediationStatus: (String, RemediationStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTask by remember { mutableStateOf(tasks.firstOrNull()) }

    LaunchedEffect(tasks) {
        if (selectedTask != null) {
            val updated = tasks.find { it.id == selectedTask?.id }
            if (updated != null) selectedTask = updated
        } else {
            selectedTask = tasks.firstOrNull()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Module Header
        Column {
            Text(
                text = "REMEDIATION & TAKEDOWN OPERATIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan,
                letterSpacing = 1.sp
            )
            Text(
                text = "Platform Enforcement & Legal Notices",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tasks List
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tasks.forEach { task ->
                val isSelected = selectedTask?.id == task.id
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) VividLime else SurfaceCard)
                        .border(1.dp, if (isSelected) VividLime else SurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { selectedTask = task }
                        .padding(10.dp)
                ) {
                    Column {
                        Text(task.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) PrimaryDark else TextWhite)
                        Text(task.platform, fontSize = 10.sp, color = if (isSelected) PrimaryDark.copy(alpha = 0.8f) else TextMuted)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTask != null) {
            val t = selectedTask!!
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Header Info
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
                                Text(t.legalNoticeType, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SafeEmerald.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(t.status.name.replace("_", " "), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Target Entity: ${t.targetEntity}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text("Platform: ${t.platform} • SLA: ${t.expectedSla}", fontSize = 11.sp, color = TextMuted)
                            Text("Dispatched: ${t.submissionDate} via SENTRIX API Connector", fontSize = 10.sp, color = TextSubtle)
                        }
                    }
                }

                // Formal Legal Draft Box
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "FORMAL LEGAL TAKEDOWN DOCUMENT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningAmber
                                )

                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Takedown Notice", t.takedownDraftContent)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Notice copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = t.takedownDraftContent,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextMuted,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Workflow Transition Controls
                item {
                    Text(
                        text = "TAKEDOWN ENFORCEMENT PIPELINE:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onUpdateRemediationStatus(t.id, RemediationStatus.PLATFORM_REMOVED)
                                Toast.makeText(context, "Platform confirmed removal!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SafeEmerald),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("confirm_removal_btn")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Confirm Removed", fontSize = 11.sp, color = Color.White)
                        }

                        Button(
                            onClick = {
                                onUpdateRemediationStatus(t.id, RemediationStatus.VERIFIED_OFFLINE)
                                Toast.makeText(context, "Verified offline across global DNS!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("verify_offline_btn")
                        ) {
                            Icon(Icons.Default.DoneAll, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Verify Offline", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                        }
                    }
                }
            }
        }
    }
}
