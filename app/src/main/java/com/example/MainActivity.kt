package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ai.IrisCopilotService
import com.example.data.model.AppDetection
import com.example.data.model.DetectionStatus
import com.example.data.model.RemediationStatus
import com.example.data.model.SocialDetection
import com.example.data.repository.SentrixRepository
import com.example.ui.components.DevicePreviewMode
import com.example.ui.components.GuidedDemoBanner
import com.example.ui.components.SentrixTopBar
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private lateinit var repository: SentrixRepository
    private val irisService = IrisCopilotService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        repository = SentrixRepository(applicationContext)
        setContent {
            MyApplicationTheme {
                SentrixApp(
                    repository = repository,
                    irisService = irisService
                )
            }
        }
    }
}

@Composable
fun SentrixApp(
    repository: SentrixRepository,
    irisService: IrisCopilotService
) {
    val context = LocalContext.current

    val organizations by repository.organizations.collectAsStateWithLifecycle()
    val selectedOrgId by repository.selectedOrgId.collectAsStateWithLifecycle()
    val socialDetections by repository.socialDetections.collectAsStateWithLifecycle()
    val appDetections by repository.appDetections.collectAsStateWithLifecycle()
    val campaigns by repository.threatCampaigns.collectAsStateWithLifecycle()
    val indicators by repository.threatIndicators.collectAsStateWithLifecycle()
    val investigations by repository.investigationCases.collectAsStateWithLifecycle()
    val remediationTasks by repository.remediationTasks.collectAsStateWithLifecycle()
    val notifications by repository.notifications.collectAsStateWithLifecycle()

    var isConsoleMode by remember { mutableStateOf(true) }
    var deviceMode by remember { mutableStateOf(DevicePreviewMode.MOBILE) }

    var currentScreen by remember { mutableStateOf("overview") }
    var selectedSocialThreatId by remember { mutableStateOf<String?>(null) }
    var selectedAppThreatId by remember { mutableStateOf<String?>(null) }
    var selectedCaseId by remember { mutableStateOf<String?>(null) }

    var activeIrisThreatSocial by remember { mutableStateOf<SocialDetection?>(null) }
    var activeIrisThreatApp by remember { mutableStateOf<AppDetection?>(null) }

    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showGuidedTourDialog by remember { mutableStateOf(false) }

    val activeBrand = organizations.find { it.id == selectedOrgId } ?: (if (organizations.isNotEmpty()) organizations.first() else repository.getActiveBrandProfile())
    val orgSocialDetections = socialDetections.filter { it.brandId == selectedOrgId }
    val orgAppDetections = appDetections.filter { it.brandId == selectedOrgId }
    val unreadNotificationsCount = notifications.count { !it.isRead }

    // Android Hardware / Predictive Back Handler
    BackHandler(enabled = !isConsoleMode || currentScreen != "overview") {
        if (!isConsoleMode) {
            isConsoleMode = true
        } else {
            currentScreen = "overview"
        }
    }

    // Outer Container handling Responsive Device Switcher (Desktop, Tablet, Mobile)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDark),
        contentAlignment = Alignment.Center
    ) {
        val deviceModifier = when (deviceMode) {
            DevicePreviewMode.DESKTOP -> Modifier
                .fillMaxHeight()
                .widthIn(max = 1080.dp)
                .border(1.dp, VividLime.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
            DevicePreviewMode.TABLET -> Modifier
                .fillMaxHeight()
                .widthIn(max = 768.dp)
                .border(1.dp, ElectricCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
            DevicePreviewMode.MOBILE -> Modifier.fillMaxSize()
        }

        Scaffold(
            modifier = deviceModifier,
            topBar = {
                SentrixTopBar(
                    organizations = organizations,
                    currentOrgId = selectedOrgId,
                    onSelectOrg = { newOrgId ->
                        repository.selectOrganization(newOrgId)
                        Toast.makeText(context, "Switched active tenant to ${organizations.find { it.id == newOrgId }?.name}", Toast.LENGTH_SHORT).show()
                    },
                    unreadAlertCount = unreadNotificationsCount,
                    onOpenAlerts = { showNotificationsDialog = true },
                    onOpenIris = {
                        activeIrisThreatSocial = null
                        activeIrisThreatApp = null
                        isConsoleMode = true
                        currentScreen = "iris"
                    },
                    onOpenGuidedTour = { showGuidedTourDialog = true },
                    deviceMode = deviceMode,
                    onSelectDeviceMode = { mode ->
                        deviceMode = mode
                        Toast.makeText(context, "Reflowed layout to ${mode.label} preview mode", Toast.LENGTH_SHORT).show()
                    },
                    isConsoleMode = isConsoleMode,
                    onToggleConsoleMode = { newMode ->
                        isConsoleMode = newMode
                    }
                )
            },
            bottomBar = {
                if (isConsoleMode) {
                    Surface(
                        color = SecondaryDark,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .testTag("sentrix_bottom_navigation")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SentrixNavChip(
                                label = "Overview",
                                icon = Icons.Default.Dashboard,
                                isSelected = currentScreen == "overview",
                                onClick = { currentScreen = "overview" },
                                testTag = "nav_overview"
                            )
                            SentrixNavChip(
                                label = "Social",
                                icon = Icons.Default.AlternateEmail,
                                isSelected = currentScreen == "social",
                                onClick = { currentScreen = "social" },
                                testTag = "nav_social"
                            )
                            SentrixNavChip(
                                label = "App Store",
                                icon = Icons.Default.Apps,
                                isSelected = currentScreen == "apps",
                                onClick = { currentScreen = "apps" },
                                testTag = "nav_apps"
                            )
                            SentrixNavChip(
                                label = "Brand Profile",
                                icon = Icons.Default.Shield,
                                isSelected = currentScreen == "brand",
                                onClick = { currentScreen = "brand" },
                                testTag = "nav_brand"
                            )
                            SentrixNavChip(
                                label = "Threat Intel",
                                icon = Icons.Default.Radar,
                                isSelected = currentScreen == "threats",
                                onClick = { currentScreen = "threats" },
                                testTag = "nav_threats"
                            )
                            SentrixNavChip(
                                label = "Cases",
                                icon = Icons.Default.FolderOpen,
                                isSelected = currentScreen == "cases",
                                onClick = { currentScreen = "cases" },
                                testTag = "nav_cases"
                            )
                            SentrixNavChip(
                                label = "Takedowns",
                                icon = Icons.Default.Gavel,
                                isSelected = currentScreen == "remediation",
                                onClick = { currentScreen = "remediation" },
                                testTag = "nav_remediation"
                            )
                            SentrixNavChip(
                                label = "Analytics",
                                icon = Icons.Default.BarChart,
                                isSelected = currentScreen == "analytics",
                                onClick = { currentScreen = "analytics" },
                                testTag = "nav_analytics"
                            )
                            SentrixNavChip(
                                label = "IRIS AI",
                                icon = Icons.Default.AutoAwesome,
                                isSelected = currentScreen == "iris",
                                onClick = { currentScreen = "iris" },
                                testTag = "nav_iris"
                            )
                        }
                    }
                }
            },
            containerColor = PrimaryDark
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (!isConsoleMode) {
                    // Public-Facing Cybersecurity SaaS Website
                    PublicWebsiteScreen(
                        onLaunchConsole = { targetSection ->
                            isConsoleMode = true
                            if (targetSection != null) {
                                if (targetSection.startsWith("org_")) {
                                    repository.selectOrganization(targetSection)
                                    currentScreen = "overview"
                                } else {
                                    currentScreen = targetSection
                                }
                            }
                        }
                    )
                } else {
                    // Enterprise Security Analyst SOC Console
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Guided Demo Roadmap Banner
                        GuidedDemoBanner(
                            currentStep = currentScreen,
                            onNavigateTo = { screen -> currentScreen = screen },
                            onResetDemo = {
                                repository.resetToInitialDemoData()
                                Toast.makeText(context, "SENTRIX demo data reset to pristine state!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )

                        // Screen Router
                        Box(modifier = Modifier.weight(1f)) {
                            when (currentScreen) {
                                "overview" -> OverviewScreen(
                                    brandProfile = activeBrand,
                                    socialDetections = orgSocialDetections,
                                    appDetections = orgAppDetections,
                                    investigations = investigations,
                                    remediationTasks = remediationTasks,
                                    onNavigateToSocial = { id ->
                                        selectedSocialThreatId = id
                                        currentScreen = "social"
                                    },
                                    onNavigateToApps = { id ->
                                        selectedAppThreatId = id
                                        currentScreen = "apps"
                                    },
                                    onNavigateToInvestigations = { id ->
                                        selectedCaseId = id
                                        currentScreen = "cases"
                                    },
                                    onNavigateToRemediation = {
                                        currentScreen = "remediation"
                                    }
                                )

                                "social" -> SocialMonitoringScreen(
                                    brandProfile = activeBrand,
                                    detections = orgSocialDetections,
                                    initialSelectedId = selectedSocialThreatId,
                                    onUpdateStatus = { id, status, notes ->
                                        repository.updateSocialDetectionStatus(id, status, notes)
                                        Toast.makeText(context, "Status updated to ${status.name}", Toast.LENGTH_SHORT).show()
                                    },
                                    onOpenIrisWithThreat = { threat ->
                                        activeIrisThreatSocial = threat
                                        activeIrisThreatApp = null
                                        currentScreen = "iris"
                                    }
                                )

                                "apps" -> AppStoreMonitoringScreen(
                                    brandProfile = activeBrand,
                                    detections = orgAppDetections,
                                    initialSelectedId = selectedAppThreatId,
                                    onUpdateStatus = { id, status, notes ->
                                        repository.updateAppDetectionStatus(id, status, notes)
                                        Toast.makeText(context, "Status updated to ${status.name}", Toast.LENGTH_SHORT).show()
                                    },
                                    onOpenIrisWithApp = { app ->
                                        activeIrisThreatSocial = null
                                        activeIrisThreatApp = app
                                        currentScreen = "iris"
                                    }
                                )

                                "brand" -> BrandProfileScreen(
                                    brandProfile = activeBrand,
                                    onSaveProfile = { updated ->
                                        repository.updateBrandProfile(updated)
                                    }
                                )

                                "threats" -> ThreatIntelligenceScreen(
                                    campaigns = campaigns,
                                    indicators = indicators
                                )

                                "cases" -> InvestigationsScreen(
                                    cases = investigations,
                                    initialSelectedCaseId = selectedCaseId
                                )

                                "remediation" -> RemediationScreen(
                                    tasks = remediationTasks,
                                    onUpdateRemediationStatus = { id, status ->
                                        repository.updateRemediationStatus(id, status)
                                    }
                                )

                                "analytics" -> AnalyticsScreen(
                                    brandProfile = activeBrand,
                                    socialDetections = orgSocialDetections,
                                    appDetections = orgAppDetections
                                )

                                "iris" -> IrisCopilotScreen(
                                    brandProfile = activeBrand,
                                    irisService = irisService,
                                    initialThreatSocial = activeIrisThreatSocial,
                                    initialThreatApp = activeIrisThreatApp
                                )

                                else -> OverviewScreen(
                                    brandProfile = activeBrand,
                                    socialDetections = orgSocialDetections,
                                    appDetections = orgAppDetections,
                                    investigations = investigations,
                                    remediationTasks = remediationTasks,
                                    onNavigateToSocial = { currentScreen = "social" },
                                    onNavigateToApps = { currentScreen = "apps" },
                                    onNavigateToInvestigations = { currentScreen = "cases" },
                                    onNavigateToRemediation = { currentScreen = "remediation" }
                                )
                            }
                        }
                    }
                }

                // Persistent Floating IRIS Assistant Launcher (Visible in both Website & Console)
                if (currentScreen != "iris" || !isConsoleMode) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        FloatingActionButton(
                            onClick = {
                                activeIrisThreatSocial = null
                                activeIrisThreatApp = null
                                isConsoleMode = true
                                currentScreen = "iris"
                            },
                            containerColor = VividLime,
                            contentColor = PrimaryDark,
                            shape = CircleShape,
                            modifier = Modifier.testTag("floating_iris_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "IRIS AI Copilot",
                                    tint = PrimaryDark,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "IRIS AI",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PrimaryDark
                                )
                            }
                        }
                    }
                }
            }

            // Dialogs
            if (showNotificationsDialog) {
                NotificationsDialog(
                    notifications = notifications,
                    onMarkAllRead = {
                        repository.markAllNotificationsRead()
                        Toast.makeText(context, "All notifications marked as read", Toast.LENGTH_SHORT).show()
                    },
                    onDismiss = { showNotificationsDialog = false },
                    onSelectNotification = { id ->
                        showNotificationsDialog = false
                        isConsoleMode = true
                        if (id.startsWith("soc_")) {
                            selectedSocialThreatId = id
                            currentScreen = "social"
                        } else if (id.startsWith("app_")) {
                            selectedAppThreatId = id
                            currentScreen = "apps"
                        } else if (id.startsWith("TKD-")) {
                            currentScreen = "remediation"
                        }
                    }
                )
            }

            if (showGuidedTourDialog) {
                GuidedDemoTourDialog(
                    onNavigateToSection = { section ->
                        isConsoleMode = true
                        currentScreen = section
                    },
                    onResetDemo = {
                        repository.resetToInitialDemoData()
                        Toast.makeText(context, "Demo data reset successfully!", Toast.LENGTH_SHORT).show()
                    },
                    onDismiss = { showGuidedTourDialog = false }
                )
            }
        }
    }
}

@Composable
private fun SentrixNavChip(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val bgColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) VividLime else SurfaceCard,
        label = "chip_bg"
    )
    val borderColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) VividLime else SurfaceBorder,
        label = "chip_border"
    )
    val contentColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) PrimaryDark else TextMuted,
        label = "chip_text"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag(testTag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}
