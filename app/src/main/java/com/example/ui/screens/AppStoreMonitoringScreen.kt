package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.components.AppSideBySideComparisonCard
import com.example.ui.components.PlatformIconBadge
import com.example.ui.components.RiskBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun AppStoreMonitoringScreen(
    brandProfile: BrandProfile,
    detections: List<AppDetection>,
    initialSelectedId: String? = null,
    onUpdateStatus: (String, DetectionStatus, String) -> Unit,
    onOpenIrisWithApp: (AppDetection) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStoreFilter by remember { mutableStateOf("All") }
    var activeDetailDetection by remember {
        mutableStateOf(detections.find { it.id == initialSelectedId } ?: detections.firstOrNull())
    }
    var showStatusDialog by remember { mutableStateOf(false) }
    var selectedNewStatus by remember { mutableStateOf(DetectionStatus.INVESTIGATING) }
    var analystNoteInput by remember { mutableStateOf("") }

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
        val matchesSearch = item.appName.contains(searchQuery, ignoreCase = true) ||
                item.publisher.contains(searchQuery, ignoreCase = true) ||
                item.packageName.contains(searchQuery, ignoreCase = true)

        val matchesStore = selectedStoreFilter == "All" ||
                item.store.contains(selectedStoreFilter, ignoreCase = true)

        matchesSearch && matchesStore
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
                        text = "APP STORE MONITORING",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CriticalRedBg)
                            .border(0.5.dp, CriticalRed, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ROGUE APP DETECTION",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF859B)
                        )
                    }
                }
                Text(
                    text = "Rogue Apps & Trojan Detection",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
            }

            Text(
                text = "${filteredList.size} Apps Listed",
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by app name, developer, or package ID...", fontSize = 12.sp, color = TextSubtle) },
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
                .testTag("app_store_search_input"),
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

        // Store Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("All", "Google Play", "Apple App Store", "APKPure", "Aptoide").forEach { store ->
                FilterChip(
                    selected = selectedStoreFilter == store,
                    onClick = { selectedStoreFilter = store },
                    label = { Text(store, fontSize = 11.sp, fontWeight = if (selectedStoreFilter == store) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VividLime,
                        selectedLabelColor = PrimaryDark,
                        containerColor = SurfaceCard,
                        labelColor = TextMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedStoreFilter == store,
                        borderColor = SurfaceBorder,
                        selectedBorderColor = VividLime
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Detections List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredList) { app ->
                AppDetectionRow(
                    app = app,
                    isSelected = activeDetailDetection?.id == app.id,
                    onClick = { activeDetailDetection = app }
                )
            }
        }

        // Active App Detail Sheet
        if (activeDetailDetection != null) {
            val app = activeDetailDetection!!
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCard)
                    .border(1.dp, if (app.isOfficialAsset) SafeEmerald else VividLime, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlatformIconBadge(platform = app.store)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = app.appName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "${app.store} • ${app.packageName}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        if (app.isOfficialAsset) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SafeEmeraldBg)
                                    .border(1.dp, SafeEmerald, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("OFFICIAL APP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
                            }
                        } else {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                StatusBadge(status = app.status)
                                RiskBadge(riskLevel = app.riskLevel, riskScore = app.riskScore)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Developer Origin Mismatch Alert
                    if (app.isPublisherMismatch && !app.isOfficialAsset) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CriticalRedBg.copy(alpha = 0.5f))
                                .border(1.dp, CriticalRed.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = CriticalRed, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "DEVELOPER ORIGIN MISMATCH DETECTED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF859B)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Listed Developer: '${app.publisher}'",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Authorized Whitelist: '${app.officialPublisherExpected}'",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Suspicious Permissions Section
                    if (app.suspiciousPermissions.isNotEmpty() && !app.isOfficialAsset) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceDark)
                                .border(0.5.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "SUSPICIOUS ANDROID CAPABILITIES / TROJAN FLAGS:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningAmber
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                app.suspiciousPermissions.forEach { perm ->
                                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 1.dp)) {
                                        Text("⚠️ ", fontSize = 10.sp)
                                        Text(perm, fontSize = 11.sp, color = TextWhite)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Side-by-Side App Comparison Card
                    AppSideBySideComparisonCard(
                        detection = app,
                        brandProfile = brandProfile
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                selectedNewStatus = app.status
                                analystNoteInput = app.analystNotes
                                showStatusDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardHover),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("update_app_status_btn")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = TextWhite, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Update Status", fontSize = 11.sp, color = TextWhite)
                        }

                        Button(
                            onClick = { onOpenIrisWithApp(app) },
                            colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("iris_app_takedown_btn")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Store Takedown", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                        }
                    }
                }
            }
        }
    }

    // Status Dialog
    if (showStatusDialog && activeDetailDetection != null) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = {
                Text(
                    text = "Update App Threat Triage Status",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Target: ${activeDetailDetection?.appName}",
                        fontSize = 12.sp,
                        color = ElectricCyan
                    )

                    listOf(
                        DetectionStatus.INVESTIGATING to "Under Investigation",
                        DetectionStatus.CONFIRMED to "Confirmed Rogue App (Escalate Takedown)",
                        DetectionStatus.FALSE_POSITIVE to "False Positive (Authorized Publisher)",
                        DetectionStatus.RESOLVED to "Resolved / Delisted from Store"
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
                        placeholder = { Text("Analyst investigation justification...", fontSize = 11.sp) },
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
private fun AppDetectionRow(
    app: AppDetection,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSelected -> ElectricCyan
        app.isOfficialAsset -> SafeEmerald.copy(alpha = 0.5f)
        app.riskLevel == RiskLevel.CRITICAL -> CriticalRed.copy(alpha = 0.4f)
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
            .testTag("app_detection_row_${app.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PlatformIconBadge(platform = app.store)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = app.appName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            if (app.isOfficialAsset) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = "Official", tint = SafeEmerald, modifier = Modifier.size(14.dp))
                            }
                        }
                        Text(
                            text = "Pub: ${app.publisher} (${app.store})",
                            fontSize = 11.sp,
                            color = if (app.isPublisherMismatch && !app.isOfficialAsset) CriticalRed else TextMuted
                        )
                    }
                }

                if (app.isOfficialAsset) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SafeEmeraldBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("VERIFIED APP", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        StatusBadge(status = app.status)
                        RiskBadge(riskLevel = app.riskLevel, riskScore = app.riskScore)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Lexical Match: ${app.nameSimilarity}/100",
                    fontSize = 10.sp,
                    color = TextSubtle
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Branding Match: ${app.brandingSimilarity}/100",
                    fontSize = 10.sp,
                    color = TextSubtle
                )
                if (app.isPublisherMismatch && !app.isOfficialAsset) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• PUBLISHER MISMATCH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CriticalRed
                    )
                }
            }
        }
    }
}
