package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.PlatformIconBadge
import com.example.ui.components.RiskBadge
import com.example.ui.components.SocialSideBySideComparisonCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun SocialMonitoringScreen(
    brandProfile: BrandProfile,
    detections: List<SocialDetection>,
    initialSelectedId: String? = null,
    onUpdateStatus: (String, DetectionStatus, String) -> Unit,
    onOpenIrisWithThreat: (SocialDetection) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedPlatformFilter by remember { mutableStateOf("All") }
    var selectedRiskFilter by remember { mutableStateOf("All") }
    var activeDetailDetection by remember {
        mutableStateOf(detections.find { it.id == initialSelectedId } ?: detections.firstOrNull())
    }
    var showStatusDialog by remember { mutableStateOf(false) }
    var selectedNewStatus by remember { mutableStateOf(DetectionStatus.INVESTIGATING) }
    var analystNoteInput by remember { mutableStateOf("") }

    // Synchronize detail with detection state
    LaunchedEffect(detections, initialSelectedId) {
        if (initialSelectedId != null) {
            val found = detections.find { it.id == initialSelectedId }
            if (found != null) activeDetailDetection = found
        } else if (activeDetailDetection != null) {
            val found = detections.find { it.id == activeDetailDetection?.id }
            if (found != null) activeDetailDetection = found
        }
    }

    val filteredList = detections.filter { item ->
        val matchesSearch = item.profileName.contains(searchQuery, ignoreCase = true) ||
                item.username.contains(searchQuery, ignoreCase = true) ||
                item.detectionReasons.any { it.contains(searchQuery, ignoreCase = true) }

        val matchesPlatform = selectedPlatformFilter == "All" ||
                item.platform.contains(selectedPlatformFilter, ignoreCase = true)

        val matchesRisk = when (selectedRiskFilter) {
            "Critical" -> item.riskLevel == RiskLevel.CRITICAL
            "High" -> item.riskLevel == RiskLevel.HIGH
            "Medium" -> item.riskLevel == RiskLevel.MEDIUM
            "Low" -> item.riskLevel == RiskLevel.LOW
            else -> true
        }

        matchesSearch && matchesPlatform && matchesRisk
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Module Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SOCIAL MEDIA MONITORING",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SafeEmeraldBg)
                            .border(0.5.dp, SafeEmerald, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ACTIVE MONITORING",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafeEmerald
                        )
                    }
                }
                Text(
                    text = "Look-Alike & Impersonation Detection",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
            }

            Text(
                text = "${filteredList.size} of ${detections.size} Found",
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by handle, keywords, or phishing link...", fontSize = 12.sp, color = TextSubtle) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = TextMuted, modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("social_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceCard,
                unfocusedContainerColor = SurfaceCard,
                focusedBorderColor = ElectricCyan,
                unfocusedBorderColor = SurfaceBorder,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Platform Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("All", "Twitter", "Instagram", "LinkedIn", "Telegram", "TikTok").forEach { platform ->
                FilterChip(
                    selected = selectedPlatformFilter == platform,
                    onClick = { selectedPlatformFilter = platform },
                    label = { Text(platform, fontSize = 11.sp, fontWeight = if (selectedPlatformFilter == platform) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VividLime,
                        selectedLabelColor = PrimaryDark,
                        containerColor = SurfaceCard,
                        labelColor = TextMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedPlatformFilter == platform,
                        borderColor = SurfaceBorder,
                        selectedBorderColor = VividLime
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Main Content Area: List of detections
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredList) { detection ->
                SocialDetectionRow(
                    detection = detection,
                    isSelected = activeDetailDetection?.id == detection.id,
                    onClick = { activeDetailDetection = detection }
                )
            }
        }

        // Active Threat Detail Sheet / Inspection View
        if (activeDetailDetection != null) {
            val item = activeDetailDetection!!
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCard)
                    .border(1.dp, if (item.isOfficialAsset) SafeEmerald else VividLime, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlatformIconBadge(platform = item.platform)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = item.profileName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "${item.username} • Detected ${item.detectedAt}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        if (item.isOfficialAsset) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SafeEmeraldBg)
                                    .border(1.dp, SafeEmerald, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SafeEmerald, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("SAFEGUARDED ASSET", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
                                }
                            }
                        } else {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                StatusBadge(status = item.status)
                                RiskBadge(riskLevel = item.riskLevel, riskScore = item.riskScore)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Look-Alike & Typo Attack Breakdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceDark)
                            .border(0.5.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Search, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LOOK-ALIKE PATTERN & REASONS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningAmber
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.lookAlikePattern,
                                fontSize = 11.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            item.detectionReasons.forEach { reason ->
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    modifier = Modifier.padding(vertical = 1.dp)
                                ) {
                                    Text("• ", fontSize = 11.sp, color = ElectricCyan)
                                    Text(reason, fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mandatory Hackathon Criteria: Side-by-Side Comparison against legitimate Brand Profile
                    SocialSideBySideComparisonCard(
                        detection = item,
                        brandProfile = brandProfile
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Analyst Action Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Update Status Button
                        Button(
                            onClick = {
                                selectedNewStatus = item.status
                                analystNoteInput = item.analystNotes
                                showStatusDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardHover),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("update_social_status_btn")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = TextWhite, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Update Status", fontSize = 11.sp, color = TextWhite)
                        }

                        // Consult IRIS AI Button
                        Button(
                            onClick = { onOpenIrisWithThreat(item) },
                            colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("iris_takedown_action_btn")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("IRIS Takedown", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                        }
                    }
                }
            }
        }
    }

    // Status Change Dialog
    if (showStatusDialog && activeDetailDetection != null) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = {
                Text(
                    text = "Update Threat Triage Status",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Target: ${activeDetailDetection?.username}",
                        fontSize = 12.sp,
                        color = ElectricCyan
                    )

                    Text(
                        text = "Select New Classification:",
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    listOf(
                        DetectionStatus.INVESTIGATING to "Under Investigation",
                        DetectionStatus.CONFIRMED to "Confirmed Threat (Escalate Takedown)",
                        DetectionStatus.FALSE_POSITIVE to "False Positive (Benign Entity)",
                        DetectionStatus.RESOLVED to "Resolved / Platform Suspended"
                    ).forEach { (status, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedNewStatus == status) VividLime.copy(alpha = 0.2f) else SurfaceDark)
                                .clickable { selectedNewStatus = status }
                                .padding(8.dp)
                        ) {
                            RadioButton(
                                selected = selectedNewStatus == status,
                                onClick = { selectedNewStatus = status }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(label, fontSize = 12.sp, color = TextWhite)
                        }
                    }

                    OutlinedTextField(
                        value = analystNoteInput,
                        onValueChange = { analystNoteInput = it },
                        placeholder = { Text("Analyst investigation reason / justification...", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark,
                            focusedBorderColor = VividLime,
                            unfocusedBorderColor = SurfaceBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        activeDetailDetection?.let {
                            onUpdateStatus(it.id, selectedNewStatus, analystNoteInput)
                        }
                        showStatusDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark)
                ) {
                    Text("Save Classification", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text("Cancel", fontSize = 12.sp, color = TextMuted)
                }
            },
            containerColor = SurfaceCard,
            tonalElevation = 6.dp
        )
    }
}

@Composable
private fun SocialDetectionRow(
    detection: SocialDetection,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSelected -> ElectricCyan
        detection.isOfficialAsset -> SafeEmerald.copy(alpha = 0.5f)
        detection.riskLevel == RiskLevel.CRITICAL -> CriticalRed.copy(alpha = 0.4f)
        else -> SurfaceBorder
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) SurfaceCardHover else SurfaceCard)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .testTag("social_detection_row_${detection.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PlatformIconBadge(platform = detection.platform)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = detection.profileName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            if (detection.isOfficialAsset) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = "Verified Official", tint = SafeEmerald, modifier = Modifier.size(14.dp))
                            }
                        }
                        Text(
                            text = detection.username,
                            fontSize = 11.sp,
                            color = if (detection.isOfficialAsset) SafeEmerald else ElectricCyan
                        )
                    }
                }

                if (detection.isOfficialAsset) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SafeEmeraldBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("VERIFIED OFFICIAL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        StatusBadge(status = detection.status)
                        RiskBadge(riskLevel = detection.riskLevel, riskScore = detection.riskScore)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = detection.lookAlikePattern,
                fontSize = 11.sp,
                color = if (detection.isOfficialAsset) TextMuted else WarningAmber,
                maxLines = 1
            )
        }
    }
}
