package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PublicWebsiteScreen(
    onLaunchConsole: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf("overview") } // overview, platform, solutions, resources, pricing, company
    var showDemoModal by remember { mutableStateOf(false) }
    var selectedIndustry by remember { mutableStateOf("fintech") }

    // Demo Form state
    var formName by remember { mutableStateOf("") }
    var formEmail by remember { mutableStateOf("") }
    var formCompany by remember { mutableStateOf("") }
    var formIndustry by remember { mutableStateOf("Financial Services") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryDark),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. ZeroFox-Inspired Public Navigation Bar
        item {
            WebsiteNavBar(
                activeTab = activeTab,
                onSelectTab = { activeTab = it },
                onOpenDemoModal = { showDemoModal = true },
                onLaunchConsole = { onLaunchConsole(null) }
            )
        }

        // 2. High-Impact Cybersecurity Hero Section
        item {
            WebsiteHeroSection(
                onLaunchConsole = { onLaunchConsole(null) },
                onRequestDemo = { showDemoModal = true }
            )
        }

        // 3. Section Content based on activeTab
        when (activeTab) {
            "overview" -> {
                // Platform Value & Problem Statements
                item {
                    ThreatLandscapeSection()
                }

                // Core Capabilities: Social & App Store Protection
                item {
                    CorePillarsSection(
                        onExploreSocial = { onLaunchConsole("social") },
                        onExploreApps = { onLaunchConsole("apps") }
                    )
                }

                // Video Understanding Spotlight (Gemini Pro gemini-3.1-pro-preview)
                item {
                    VideoAnalysisSpotlightSection(
                        onOpenVideoScanner = { onLaunchConsole("iris") }
                    )
                }

                // Industry Solutions Switcher
                item {
                    IndustrySolutionsSection(
                        selectedIndustry = selectedIndustry,
                        onSelectIndustry = { selectedIndustry = it },
                        onOpenConsoleOrg = { orgId -> onLaunchConsole(orgId) }
                    )
                }

                // Threat Anatomy Demonstrator
                item {
                    ThreatAnatomySection()
                }

                // Digital Risk ROI Calculator
                item {
                    RoiCalculatorSection()
                }

                // Customer Testimonials & Security Certifications
                item {
                    SocialProofSection()
                }
            }

            "platform" -> {
                item {
                    PlatformDeepDiveSection(
                        onLaunchSocial = { onLaunchConsole("social") },
                        onLaunchApps = { onLaunchConsole("apps") },
                        onLaunchThreats = { onLaunchConsole("threats") },
                        onLaunchRemediation = { onLaunchConsole("remediation") }
                    )
                }
            }

            "solutions" -> {
                item {
                    IndustrySolutionsSection(
                        selectedIndustry = selectedIndustry,
                        onSelectIndustry = { selectedIndustry = it },
                        onOpenConsoleOrg = { orgId -> onLaunchConsole(orgId) }
                    )
                }
            }

            "resources" -> {
                item {
                    ResourcesSection()
                }
            }

            "pricing" -> {
                item {
                    PricingSection(
                        onRequestPlan = { showDemoModal = true },
                        onStartDemo = { onLaunchConsole(null) }
                    )
                }
            }

            "company" -> {
                item {
                    CompanySection(
                        onRequestContact = { showDemoModal = true }
                    )
                }
            }
        }

        // 4. Global Call To Action
        item {
            WebsiteFooterCta(
                onLaunchConsole = { onLaunchConsole(null) },
                onRequestDemo = { showDemoModal = true }
            )
        }

        // 5. Public Website Footer
        item {
            WebsiteFooter()
        }
    }

    // Interactive Demo / Sales Contact Dialog
    if (showDemoModal) {
        AlertDialog(
            onDismissRequest = { showDemoModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(VividLimeDark)
                            .border(1.dp, VividLime, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = VividLime, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Request Enterprise Demo",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Experience SENTRIX tailored to your organization's digital brand footprint and mobile app perimeter.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )

                    OutlinedTextField(
                        value = formName,
                        onValueChange = { formName = it },
                        label = { Text("Full Name", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VividLime,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedLabelColor = VividLime,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = formEmail,
                        onValueChange = { formEmail = it },
                        label = { Text("Work Corporate Email", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VividLime,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedLabelColor = VividLime,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = formCompany,
                        onValueChange = { formCompany = it },
                        label = { Text("Company Name", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VividLime,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedLabelColor = VividLime,
                            unfocusedLabelColor = TextMuted,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        singleLine = true
                    )

                    Text(
                        text = "Industry Sector:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Banking & Fintech", "E-Commerce", "Enterprise SaaS", "Healthcare").forEach { ind ->
                            val isSel = formIndustry == ind
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) VividLimeDark else SecondaryDark)
                                    .border(1.dp, if (isSel) VividLime else SurfaceBorder, RoundedCornerShape(6.dp))
                                    .clickable { formIndustry = ind }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = ind,
                                    fontSize = 10.sp,
                                    color = if (isSel) VividLime else TextMuted,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (formEmail.isNotBlank() || formName.isNotBlank()) {
                            Toast.makeText(context, "Demo request submitted for ${if (formCompany.isNotBlank()) formCompany else "your organization"}. Welcome to SENTRIX!", Toast.LENGTH_LONG).show()
                            showDemoModal = false
                        } else {
                            Toast.makeText(context, "Please enter your name or email.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark)
                ) {
                    Text("Schedule Live Demo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDemoModal = false }) {
                    Text("Cancel", fontSize = 12.sp, color = TextMuted)
                }
            },
            containerColor = SurfaceCard,
            tonalElevation = 8.dp
        )
    }
}

// -------------------------------------------------------------
// Top Navigation Bar
// -------------------------------------------------------------
@Composable
private fun WebsiteNavBar(
    activeTab: String,
    onSelectTab: (String) -> Unit,
    onOpenDemoModal: () -> Unit,
    onLaunchConsole: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SecondaryDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onSelectTab("overview") }
                    .testTag("website_logo_btn")
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
                        contentDescription = "SENTRIX Logo",
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
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(VividLime.copy(alpha = 0.15f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "B2B DRP",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = VividLime
                            )
                        }
                    }
                    Text(
                        text = "Digital Risk Protection",
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
            }

            // CTAs: Request Demo & Launch Console
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenDemoModal,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VividLime),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VividLime.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("website_request_demo_btn")
                ) {
                    Text("Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onLaunchConsole,
                    colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("website_launch_console_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Console", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Navigation Links Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                "overview" to "Overview",
                "platform" to "Platform",
                "solutions" to "Solutions",
                "resources" to "Resources",
                "pricing" to "Pricing",
                "company" to "Company"
            ).forEach { (key, title) ->
                val isSelected = activeTab == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) SurfaceCard else Color.Transparent)
                        .border(1.dp, if (isSelected) VividLime.copy(alpha = 0.5f) else Color.Transparent, RoundedCornerShape(6.dp))
                        .clickable { onSelectTab(key) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) VividLime else TextMuted
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Hero Section
// -------------------------------------------------------------
@Composable
private fun WebsiteHeroSection(
    onLaunchConsole: () -> Unit,
    onRequestDemo: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SecondaryDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            // Live Sentinel Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(VividLimeDark)
                    .border(1.dp, VividLime.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(VividLime)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NEXT-GEN B2B DIGITAL RISK PROTECTION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = VividLime,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Autonomous AI Defense for Social Media & App Stores",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "SENTRIX continuously monitors, identifies, and eradicates brand impersonation, fraudulent mobile trojans, look-alike typo squatters, and deepfake scam reels before customer trust is compromised.",
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onLaunchConsole,
                    colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Explore Console", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onRequestDemo,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderLight),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Request Demo", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fast Highlights
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HighlightStat(number = "< 4 hrs", label = "Mean Takedown Time")
                HighlightStat(number = "0", label = "False Positives on Whitelist")
                HighlightStat(number = "SQL/Room", label = "Persistent Device DB")
            }
        }
    }
}

@Composable
private fun HighlightStat(number: String, label: String) {
    Column {
        Text(text = number, fontSize = 16.sp, fontWeight = FontWeight.Black, color = ElectricCyan)
        Text(text = label, fontSize = 10.sp, color = TextSubtle)
    }
}

// -------------------------------------------------------------
// Threat Landscape
// -------------------------------------------------------------
@Composable
private fun ThreatLandscapeSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "THE DIGITAL PERIMETER CRISIS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ElectricCyan,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Modern attackers don't breach your network. They clone your brand.",
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProblemCard(
                icon = Icons.Default.AlternateEmail,
                title = "Fake Social Profiles",
                desc = "Attackers create handles using character swaps ('0' for 'o'), deceptive punctuation, and hijack customer support queries.",
                modifier = Modifier.weight(1f)
            )
            ProblemCard(
                icon = Icons.Default.Apps,
                title = "Rogue Mobile APKs",
                desc = "Fraudulent APKs mimicking your app bundle overlay banking logins and steal SMS 2FA authorization codes.",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProblemCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(PrimaryDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = VividLime, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = desc, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)
        }
    }
}

// -------------------------------------------------------------
// Core Pillars: Social & App Store Protection
// -------------------------------------------------------------
@Composable
private fun CorePillarsSection(
    onExploreSocial: () -> Unit,
    onExploreApps: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "PLATFORM CORE PILLARS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = VividLime,
            letterSpacing = 1.sp
        )
        Text(
            text = "Specialized Surveillance Across Critical Attack Vectors",
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )

        // Pillar 1: Social Media
        PillarDetailCard(
            title = "Social Media Monitoring",
            subtitle = "Deep Multi-Platform Coverage",
            description = "Analyzes X, Instagram, Facebook, LinkedIn, Telegram, and TikTok for typosquatting, homoglyph substitutions, copied trademark graphics, and VIP executive impersonation.",
            tags = listOf("Homoglyphs", "Image Match", "Redirect Phishing", "Telegram Scanners"),
            actionText = "View Social Monitoring",
            onAction = onExploreSocial
        )

        // Pillar 2: App Store
        PillarDetailCard(
            title = "App Store Monitoring",
            subtitle = "Google Play, iOS & Rogue APK Repositories",
            description = "Discovers look-alike binaries, flags unauthorized developer origins against verified whitelists, and inspects dangerous Android permissions (SMS reading, Screen Overlay, Camera).",
            tags = listOf("Trojan Overlays", "Developer Mismatch", "APK Mirrors", "Package Spoofing"),
            actionText = "View App Store Monitoring",
            onAction = onExploreApps
        )
    }
}

@Composable
private fun PillarDetailCard(
    title: String,
    subtitle: String,
    description: String,
    tags: List<String>,
    actionText: String,
    onAction: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = subtitle, fontSize = 10.sp, color = ElectricCyan)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 11.sp, color = TextMuted, lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(PrimaryDark)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(text = tag, fontSize = 9.sp, color = VividLime, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryDark, contentColor = VividLime),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderLight),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(actionText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = VividLime, modifier = Modifier.size(13.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// Video Understanding Feature Spotlight (Gemini Pro gemini-3.1-pro-preview)
// -------------------------------------------------------------
@Composable
private fun VideoAnalysisSpotlightSection(
    onOpenVideoScanner: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(SecondaryDark)
            .border(1.dp, ElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(ElectricCyanDark)
                            .border(1.dp, ElectricCyan, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NEW: VIDEO THREAT UNDERSTANDING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        letterSpacing = 0.8.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PrimaryDark)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("gemini-3.1-pro-preview", fontSize = 9.sp, color = VividLime, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Autonomous AI Forensics for Reels, Deepfakes & Video Scams",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Scammers increasingly deploy synthetic voice clones of executives and fraudulent promotional videos on Instagram, YouTube, and TikTok. SENTRIX integrates Gemini Pro to extract speech transcripts, identify illicit brand watermarks, and formulate evidence-backed takedown packages.",
                fontSize = 11.sp,
                color = TextMuted,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onOpenVideoScanner,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = PrimaryDark),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Launch Video Investigator in IRIS Copilot", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Industry Solutions Section
// -------------------------------------------------------------
@Composable
private fun IndustrySolutionsSection(
    selectedIndustry: String,
    onSelectIndustry: (String) -> Unit,
    onOpenConsoleOrg: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "INDUSTRY SOLUTIONS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ElectricCyan,
            letterSpacing = 1.sp
        )
        Text(
            text = "Tailored Brand Safeguards for Every Enterprise Sector",
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )

        // Industry Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "fintech" to "Banking & Fintech (ApexBank)",
                "ecommerce" to "E-Commerce (ShopNova)",
                "saas" to "Enterprise SaaS (CloudMatrix)",
                "health" to "Healthcare IoT (PulseWear)"
            ).forEach { (id, label) ->
                val isSelected = selectedIndustry == id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) VividLimeDark else SurfaceCard)
                        .border(1.dp, if (isSelected) VividLime else SurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { onSelectIndustry(id) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) VividLime else TextMuted
                    )
                }
            }
        }

        // Selected Industry Card Details
        when (selectedIndustry) {
            "fintech" -> {
                IndustryCaseCard(
                    clientName = "ApexBank International",
                    threatProfile = "Threat actors deployed rogue 'Apex Bank MicroLoan' APKs with SMS interception trojans and look-alike customer support Twitter handles.",
                    sentrixImpact = "Automated detection within 12 minutes. Zero whitelisted Apex assets falsely alerted. Takedowns executed across Google Play and Twitter.",
                    targetOrgId = "org_apex",
                    onOpenTenant = { onOpenConsoleOrg("org_apex") }
                )
            }
            "ecommerce" -> {
                IndustryCaseCard(
                    clientName = "ShopNova Global Retail",
                    threatProfile = "Syndicate created 'ShopNova VIP Flash Sale Phish' fake Instagram pages and cloned APK shopping portals stealing credit card numbers.",
                    sentrixImpact = "All 4 impersonator accounts flagged, DNS sinkholes applied, and rogue Android store listings neutralized.",
                    targetOrgId = "org_shop",
                    onOpenTenant = { onOpenConsoleOrg("org_shop") }
                )
            }
            "saas" -> {
                IndustryCaseCard(
                    clientName = "CloudMatrix DevOps Infrastructure",
                    threatProfile = "Phishing campaign spoofing cloudmatrix-support.io and circulating trojanized CLI wrappers harvesting API access tokens.",
                    sentrixImpact = "C2 domain takedown processed via ICANN registrar within 3.2 hours. Whitelist protection maintained for all developer tooling.",
                    targetOrgId = "org_cloud",
                    onOpenTenant = { onOpenConsoleOrg("org_cloud") }
                )
            }
            "health" -> {
                IndustryCaseCard(
                    clientName = "PulseWear Health Systems",
                    threatProfile = "Counterfeit companion APK requesting sensitive device storage and HIPAA health telemetry permissions.",
                    sentrixImpact = "Rogue APK binary unpacked, malicious permissions cataloged, and Google Play Security alert dispatched.",
                    targetOrgId = "org_pulse",
                    onOpenTenant = { onOpenConsoleOrg("org_pulse") }
                )
            }
        }
    }
}

@Composable
private fun IndustryCaseCard(
    clientName: String,
    threatProfile: String,
    sentrixImpact: String,
    targetOrgId: String,
    onOpenTenant: () -> Unit
) {
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
                Text(text = clientName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = "LIVE SEEDED TENANT", fontSize = 9.sp, color = VividLime, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "• THREAT PROFILE:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WarningAmber)
            Text(text = threatProfile, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "• SENTRIX RESOLUTION:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
            Text(text = sentrixImpact, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onOpenTenant,
                colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("Launch $clientName in Security Console", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// Threat Anatomy Demonstrator
// -------------------------------------------------------------
@Composable
private fun ThreatAnatomySection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SecondaryDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "THREAT ANATOMY DEMONSTRATOR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VividLime,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "How Attackers Deceive Users vs How SENTRIX Detects Them",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnatomyItem(
                    title = "Homoglyphs",
                    example = "@ApexB0nk_Help",
                    expl = "Replaces 'o' with '0'. SENTRIX uses Levenshtein matrix to catch visual confusion.",
                    modifier = Modifier.weight(1f)
                )
                AnatomyItem(
                    title = "Trojan Permissions",
                    example = "READ_SMS + OVERLAY",
                    expl = "Fake APK intercepts 2FA codes. SENTRIX flags dangerous manifest permissions instantly.",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun AnatomyItem(
    title: String,
    example: String,
    expl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorderLight, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(PrimaryDark)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = example, fontSize = 10.sp, color = CriticalRed, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = expl, fontSize = 10.sp, color = TextMuted, lineHeight = 14.sp)
        }
    }
}

// -------------------------------------------------------------
// Digital Risk ROI Calculator
// -------------------------------------------------------------
@Composable
private fun RoiCalculatorSection() {
    var brandAssetsMonitored by remember { mutableStateOf(15f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "DIGITAL RISK ROI ESTIMATOR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VividLime,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Calculate Loss Prevented Through Autonomous Takedowns",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Monitored Digital Footprint: ${brandAssetsMonitored.toInt()} Brand Assets",
                fontSize = 12.sp,
                color = TextWhite,
                fontWeight = FontWeight.SemiBold
            )

            Slider(
                value = brandAssetsMonitored,
                onValueChange = { brandAssetsMonitored = it },
                valueRange = 5f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = VividLime,
                    activeTrackColor = VividLime,
                    inactiveTrackColor = PrimaryDark
                )
            )

            val annualAttacksEstimate = (brandAssetsMonitored * 1.8).toInt()
            val costAvoidanceEstimate = annualAttacksEstimate * 14500

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Projected Incidents Avoided", fontSize = 10.sp, color = TextMuted)
                    Text("$annualAttacksEstimate attacks / yr", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Estimated Fraud Avoidance", fontSize = 10.sp, color = TextMuted)
                    Text("$$costAvoidanceEstimate USD", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Social Proof & Compliance
// -------------------------------------------------------------
@Composable
private fun SocialProofSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "ENTERPRISE COMPLIANCE & REPUTATION",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextSubtle,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ComplianceBadge(title = "SOC 2 Type II", desc = "Audit Validated", modifier = Modifier.weight(1f))
            ComplianceBadge(title = "ISO 27001", desc = "Certified SecOps", modifier = Modifier.weight(1f))
            ComplianceBadge(title = "Room SQLite", desc = "Zero Data Loss", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ComplianceBadge(title: String, desc: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SecondaryDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(text = desc, fontSize = 9.sp, color = TextMuted)
        }
    }
}

// -------------------------------------------------------------
// Platform Deep Dive
// -------------------------------------------------------------
@Composable
private fun PlatformDeepDiveSection(
    onLaunchSocial: () -> Unit,
    onLaunchApps: () -> Unit,
    onLaunchThreats: () -> Unit,
    onLaunchRemediation: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "SENTRIX PLATFORM ARCHITECTURE",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = VividLime,
            letterSpacing = 1.sp
        )
        Text(
            text = "End-to-End Digital Threat Remediation Workflow",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )
        Text(
            text = "From continuous API and crawler surveillance to forensic score calculation and automated legal notice dispatch, SENTRIX operates as your organization's autonomous brand protection command center.",
            fontSize = 12.sp,
            color = TextMuted,
            lineHeight = 17.sp
        )

        ArchitectureStep(
            number = "01",
            title = "Continuous Ingestion & Surface Discovery",
            desc = "Scans social networks, official app stores (Google Play, iOS), and unauthorized APK download hubs.",
            buttonText = "Test Social Scanners",
            onClick = onLaunchSocial
        )

        ArchitectureStep(
            number = "02",
            title = "Heuristic & Lexical Detection Engine",
            desc = "Levenshtein distance, Jaro-Winkler homoglyph matching, publisher verification, and manifest permission analysis.",
            buttonText = "Test Rogue App Scanners",
            onClick = onLaunchApps
        )

        ArchitectureStep(
            number = "03",
            title = "Threat Intelligence Correlation",
            desc = "Correlates disparate IOCs, C2 domains, and phishing handles into unified campaigns (e.g. 'Operation ApexPhish').",
            buttonText = "Explore Threat Intelligence",
            onClick = onLaunchThreats
        )

        ArchitectureStep(
            number = "04",
            title = "Rapid Platform Enforcement",
            desc = "Dispatches platform abuse reports and DMCA cease & desist notices directly to hosting and social providers.",
            buttonText = "Open Takedown Center",
            onClick = onLaunchRemediation
        )
    }
}

@Composable
private fun ArchitectureStep(
    number: String,
    title: String,
    desc: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = number,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = VividLime
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = desc, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark, contentColor = VividLime),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderLight),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(buttonText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Resources & Cybersecurity Glossary
// -------------------------------------------------------------
@Composable
private fun ResourcesSection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "CYBERSECURITY RESOURCES & GUIDES",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ElectricCyan,
            letterSpacing = 1.sp
        )
        Text(
            text = "Executive Threat Reports & DRP Knowledge Base",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )

        ResourceCard(
            tag = "WHITE PAPER",
            title = "2026 Mobile Banking Trojan Threat Report",
            desc = "Comprehensive analysis of how rogue APKs use deceptive permissions and overlay windows to harvest 2FA credentials."
        )

        ResourceCard(
            tag = "PLAYBOOK",
            title = "Executive Playbook: Social Media Impersonation Takedowns",
            desc = "Step-by-step guidance on gathering forensic evidence, trademark documentation, and achieving sub-4-hour resolution."
        )

        // Cybersecurity Glossary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCard)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "DRP SECURITY GLOSSARY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = VividLime)

                GlossaryEntry(term = "Homoglyph Attack", def = "Using visually similar characters (e.g. Cyrillic 'а' for Latin 'a', or digit '0' for letter 'O') to deceive victims.")
                GlossaryEntry(term = "Typosquatting", def = "Registering variants with intentional spelling mistakes, added words (_support, _official), or missing characters.")
                GlossaryEntry(term = "Overlay Trojan", def = "An illicit mobile app drawing an invisible fake login window on top of legitimate banking applications.")
                GlossaryEntry(term = "C2 Infrastructure", def = "Command and Control servers that communicate with rogue binaries to execute malicious directives.")
            }
        }
    }
}

@Composable
private fun ResourceCard(tag: String, title: String, desc: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SecondaryDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(text = tag, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, fontSize = 11.sp, color = TextMuted, lineHeight = 15.sp)
        }
    }
}

@Composable
private fun GlossaryEntry(term: String, def: String) {
    Column {
        Text(text = term, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Text(text = def, fontSize = 10.sp, color = TextMuted, lineHeight = 14.sp)
    }
}

// -------------------------------------------------------------
// Pricing Plans
// -------------------------------------------------------------
@Composable
private fun PricingSection(
    onRequestPlan: () -> Unit,
    onStartDemo: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "ENTERPRISE B2B PRICING",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = VividLime,
            letterSpacing = 1.sp
        )
        Text(
            text = "Predictable Subscription Built for Security Operations",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )

        PricingPlanCard(
            name = "STARTER DRP",
            price = "$2,400 / month",
            features = listOf(
                "Up to 10 Monitored Brand Assets",
                "Social Media Monitoring (X, Instagram, LinkedIn)",
                "Standard Takedown Workflows (< 24h SLA)",
                "Weekly Executive Summary Reports"
            ),
            isPopular = false,
            onAction = onStartDemo,
            actionLabel = "Try Live Console"
        )

        PricingPlanCard(
            name = "ENTERPRISE SOC (RECOMMENDED)",
            price = "$5,800 / month",
            features = listOf(
                "Unlimited Monitored Brand Assets",
                "Full App Store Monitoring (Google Play, iOS, APKs)",
                "IRIS AI Copilot + Video Reel Forensics (Gemini Pro)",
                "Priority Takedown SLA (< 4h Resolution)",
                "Persistent Multi-Tenant Room DB Storage",
                "Dedicated SOC Brand Analyst"
            ),
            isPopular = true,
            onAction = onRequestPlan,
            actionLabel = "Schedule Enterprise Demo"
        )

        PricingPlanCard(
            name = "GLOBAL BRAND FORTRESS",
            price = "Custom Enterprise Quote",
            features = listOf(
                "Multi-Entity Global Corporate Subsidiary Coverage",
                "Dark Web & C2 Infrastructure Takedowns",
                "Direct API SIEM/SOAR Ingestion Integrations",
                "24/7 Dedicated Incident Response War Room"
            ),
            isPopular = false,
            onAction = onRequestPlan,
            actionLabel = "Contact Sales"
        )
    }
}

@Composable
private fun PricingPlanCard(
    name: String,
    price: String,
    features: List<String>,
    isPopular: Boolean,
    onAction: () -> Unit,
    actionLabel: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isPopular) VividLimeDark.copy(alpha = 0.5f) else SurfaceCard)
            .border(
                1.dp,
                if (isPopular) VividLime else SurfaceBorder,
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            if (isPopular) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(VividLime)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("MOST POPULAR FOR FINANCIAL SECTOR", fontSize = 8.sp, fontWeight = FontWeight.Black, color = PrimaryDark)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = price, fontSize = 18.sp, fontWeight = FontWeight.Black, color = if (isPopular) VividLime else ElectricCyan)

            Spacer(modifier = Modifier.height(10.dp))

            features.forEach { feat ->
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("✓", fontSize = 11.sp, color = VividLime, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = feat, fontSize = 11.sp, color = TextWhite)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPopular) VividLime else SecondaryDark,
                    contentColor = if (isPopular) PrimaryDark else VividLime
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(actionLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// Company Section
// -------------------------------------------------------------
@Composable
private fun CompanySection(onRequestContact: () -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "ABOUT SENTRIX & TEAM CIPHER",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = VividLime,
            letterSpacing = 1.sp
        )
        Text(
            text = "Built for Kampus.VC Digital Risk Protection Challenge",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite
        )
        Text(
            text = "Team CIPHER engineered SENTRIX to set a new benchmark in autonomous brand protection. By combining lexical distance algorithms, APK decompilation heuristics, and multimodal video analysis via Gemini Pro, we empower enterprises to reclaim their digital identity.",
            fontSize = 12.sp,
            color = TextMuted,
            lineHeight = 17.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceCard)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Team CIPHER Core Principles:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text("1. Zero False Positives on Whitelisted Official Assets.", fontSize = 11.sp, color = SafeEmerald)
                Text("2. Fully Explainable Scoring Math (No Black-Box Hallucinations).", fontSize = 11.sp, color = ElectricCyan)
                Text("3. Immediate Actionability (One-tap Legally Certified Takedowns).", fontSize = 11.sp, color = VividLime)
            }
        }

        Button(
            onClick = onRequestContact,
            colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Contact Engineering Team CIPHER", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// Footer CTA
// -------------------------------------------------------------
@Composable
private fun WebsiteFooterCta(
    onLaunchConsole: () -> Unit,
    onRequestDemo: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(SecondaryDark)
            .border(1.dp, VividLime.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "READY TO PROTECT YOUR BRAND FOOTPRINT?",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VividLime,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Experience SENTRIX DRP Console Live",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Switch into the authenticated security analyst console to evaluate real detections across fintech, e-commerce, and healthcare tenants.",
                fontSize = 11.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLaunchConsole,
                    colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Launch Enterprise Console", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onRequestDemo,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderLight),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Request Demo", fontSize = 11.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Footer
// -------------------------------------------------------------
@Composable
private fun WebsiteFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SecondaryDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SENTRIX — Digital Risk Protection Platform",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Engineered by Team CIPHER for the Kampus.VC Hackathon Challenge.",
            fontSize = 10.sp,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "© 2026 SENTRIX Security Inc. All rights reserved.",
            fontSize = 9.sp,
            color = TextSubtle
        )
    }
}
