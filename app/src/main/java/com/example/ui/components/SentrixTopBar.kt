package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import com.example.data.model.BrandProfile
import com.example.ui.theme.*

enum class DevicePreviewMode(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    DESKTOP("Desktop", Icons.Default.Computer),
    TABLET("Tablet", Icons.Default.Tablet),
    MOBILE("Mobile", Icons.Default.PhoneIphone)
}

@Composable
fun SentrixTopBar(
    organizations: List<BrandProfile>,
    currentOrgId: String,
    onSelectOrg: (String) -> Unit,
    unreadAlertCount: Int,
    onOpenAlerts: () -> Unit,
    onOpenIris: () -> Unit,
    onOpenGuidedTour: () -> Unit,
    // Device preview switcher
    deviceMode: DevicePreviewMode,
    onSelectDeviceMode: (DevicePreviewMode) -> Unit,
    // Website vs Console switcher
    isConsoleMode: Boolean,
    onToggleConsoleMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedOrgMenu by remember { mutableStateOf(false) }
    val currentOrg = organizations.find { it.id == currentOrgId } ?: organizations.firstOrNull()

    // Radar pulsing effect for live telemetry
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radar_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SecondaryDark)
            .border(width = 1.dp, color = SurfaceBorder, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        // Row 1: Logo & Global Switchers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Brand Crest & Team CIPHER with Vivid Lime
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onOpenGuidedTour() }
                    .padding(2.dp)
                    .testTag("app_logo_button")
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VividLimeDark)
                        .border(1.dp, VividLime, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "SENTRIX Crest",
                        tint = VividLime,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SENTRIX",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(VividLimeDark)
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "CIPHER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = VividLime
                            )
                        }
                    }
                    Text(
                        text = "Digital Risk Protection",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            // Right Action Controls: Mode Toggle & Alerts & IRIS
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Mode Toggle: Website <-> Enterprise Console
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isConsoleMode) VividLimeDark else SurfaceCard)
                        .border(1.dp, if (isConsoleMode) VividLime else SurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { onToggleConsoleMode(!isConsoleMode) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("toggle_console_mode_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isConsoleMode) Icons.Default.Public else Icons.Default.Terminal,
                            contentDescription = null,
                            tint = if (isConsoleMode) VividLime else ElectricCyan,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isConsoleMode) "Public Site" else "SOC Console",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isConsoleMode) VividLime else TextWhite
                        )
                    }
                }

                // Alerts Bell
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { onOpenAlerts() }
                        .testTag("alerts_bell_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = if (unreadAlertCount > 0) WarningAmber else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    if (unreadAlertCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(CriticalRed)
                                .align(Alignment.TopEnd)
                        )
                    }
                }

                // IRIS AI Copilot Launcher
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VividLime)
                        .clickable { onOpenIris() }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("iris_copilot_launcher"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "IRIS Copilot",
                            tint = PrimaryDark,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "IRIS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = PrimaryDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2: Responsive Device Switcher + Tenant Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Device Preview Switcher (Desktop, Tablet, Mobile)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryDark)
                    .border(0.5.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DevicePreviewMode.values().forEach { mode ->
                    val isSel = deviceMode == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) VividLimeDark else Color.Transparent)
                            .border(0.5.dp, if (isSel) VividLime else Color.Transparent, RoundedCornerShape(6.dp))
                            .clickable { onSelectDeviceMode(mode) }
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                            .testTag("device_switcher_${mode.name.lowercase()}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = mode.icon,
                                contentDescription = mode.label,
                                tint = if (isSel) VividLime else TextMuted,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = mode.label,
                                fontSize = 9.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) VividLime else TextMuted
                            )
                        }
                    }
                }
            }

            // Tenant Switcher Dropdown
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { expandedOrgMenu = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("tenant_switcher_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = VividLime,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentOrg?.name ?: "Tenant",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Switch Tenant",
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }

                DropdownMenu(
                    expanded = expandedOrgMenu,
                    onDismissRequest = { expandedOrgMenu = false },
                    modifier = Modifier.background(SurfaceCard)
                ) {
                    organizations.forEach { org ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = org.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (org.id == currentOrgId) VividLime else TextWhite
                                    )
                                    Text(
                                        text = org.industry,
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }
                            },
                            onClick = {
                                onSelectOrg(org.id)
                                expandedOrgMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}
