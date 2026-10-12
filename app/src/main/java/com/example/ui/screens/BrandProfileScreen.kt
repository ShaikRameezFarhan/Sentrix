package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrandProfile
import com.example.data.model.OfficialAppAsset
import com.example.data.model.OfficialSocialAsset
import com.example.ui.components.PlatformIconBadge
import com.example.ui.theme.*

@Composable
fun BrandProfileScreen(
    brandProfile: BrandProfile,
    onSaveProfile: (BrandProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    var brandName by remember(brandProfile) { mutableStateOf(brandProfile.name) }
    var industry by remember(brandProfile) { mutableStateOf(brandProfile.industry) }
    var description by remember(brandProfile) { mutableStateOf(brandProfile.description) }
    var domains by remember(brandProfile) { mutableStateOf(brandProfile.officialDomains) }
    var authorizedPublishers by remember(brandProfile) { mutableStateOf(brandProfile.authorizedPublishers) }
    var brandKeywords by remember(brandProfile) { mutableStateOf(brandProfile.brandKeywords) }
    var brandAliases by remember(brandProfile) { mutableStateOf(brandProfile.brandAliases) }

    var newDomainInput by remember { mutableStateOf("") }
    var newKeywordInput by remember { mutableStateOf("") }
    var newPublisherInput by remember { mutableStateOf("") }
    var showSavedSnackbar by remember { mutableStateOf(false) }

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BRAND PROFILE ONBOARDING",
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
                                text = "VERIFIED ASSET ANCHOR",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafeEmerald
                            )
                        }
                    }
                    Text(
                        text = "Legitimate Asset Anchor",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }

                Button(
                    onClick = {
                        val updated = brandProfile.copy(
                            name = brandName,
                            industry = industry,
                            description = description,
                            officialDomains = domains,
                            authorizedPublishers = authorizedPublishers,
                            brandKeywords = brandKeywords,
                            brandAliases = brandAliases
                        )
                        onSaveProfile(updated)
                        showSavedSnackbar = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark),
                    modifier = Modifier.testTag("save_brand_profile_btn")
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = PrimaryDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save Changes", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                }
            }
        }

        if (showSavedSnackbar) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SafeEmeraldBg)
                        .border(1.dp, SafeEmerald, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SafeEmerald, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Brand Profile successfully saved and calibrated across detection engines!", fontSize = 12.sp, color = SafeEmerald)
                        }
                        IconButton(onClick = { showSavedSnackbar = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = SafeEmerald, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // Section 1: General Brand Info
        item {
            CardContainer(title = "General Enterprise Details") {
                OutlinedTextField(
                    value = brandName,
                    onValueChange = { brandName = it },
                    label = { Text("Organization & Brand Name", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = brandFieldColors()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = industry,
                    onValueChange = { industry = it },
                    label = { Text("Primary Industry Sector", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = brandFieldColors()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Corporate Description", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = brandFieldColors()
                )
            }
        }

        // Section 2: Official Whitelisted Domains
        item {
            CardContainer(title = "Official Whitelisted Domains (${domains.size})") {
                Text(
                    text = "External links matching these domains are exempted from phishing redirection alerts.",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                domains.forEach { domain ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceDark)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(domain, fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                        }
                        IconButton(
                            onClick = { domains = domains.filter { it != domain } },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = DangerCoral, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newDomainInput,
                        onValueChange = { newDomainInput = it },
                        placeholder = { Text("e.g. apex-portal.io", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = brandFieldColors()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newDomainInput.isNotBlank()) {
                                domains = domains + newDomainInput.trim().lowercase()
                                newDomainInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark)
                    ) {
                        Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                    }
                }
            }
        }

        // Section 3: Official Social Media Accounts
        item {
            CardContainer(title = "Registered Official Social Channels (${brandProfile.officialSocials.size})") {
                Text(
                    text = "These authentic channels are explicitly safeguarded with zero false positives.",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                brandProfile.officialSocials.forEach { social ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceDark)
                            .border(1.dp, SafeEmerald.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlatformIconBadge(platform = social.platform)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(social.handle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = "Verified", tint = SafeEmerald, modifier = Modifier.size(12.dp))
                                }
                                Text("${social.platform} • ${social.followerCount} Followers", fontSize = 10.sp, color = TextMuted)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SafeEmeraldBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("AUTHENTIC", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }

        // Section 4: Official Mobile Applications
        item {
            CardContainer(title = "Registered Mobile Applications (${brandProfile.officialMobileApps.size})") {
                brandProfile.officialMobileApps.forEach { app ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceDark)
                            .border(1.dp, SafeEmerald.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlatformIconBadge(platform = if (app.platform == "Android") "Google Play" else "Apple App Store")
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(app.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                Text("ID: ${app.appId} • Pub: ${app.publisher}", fontSize = 10.sp, color = TextMuted)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SafeEmeraldBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("VERIFIED APP", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SafeEmerald)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }

        // Section 5: Authorized Publishers Whitelist
        item {
            CardContainer(title = "Authorized App Publishers Whitelist (${authorizedPublishers.size})") {
                Text(
                    text = "Any app store listing claiming association with the brand that is published by an entity NOT in this list will be flagged for Developer Mismatch.",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                authorizedPublishers.forEach { pub ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceDark)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = SafeEmerald, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(pub, fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                        }
                        IconButton(
                            onClick = { authorizedPublishers = authorizedPublishers.filter { it != pub } },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove", tint = DangerCoral, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newPublisherInput,
                        onValueChange = { newPublisherInput = it },
                        placeholder = { Text("e.g. Apex Digital Corp", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = brandFieldColors()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newPublisherInput.isNotBlank()) {
                                authorizedPublishers = authorizedPublishers + newPublisherInput.trim()
                                newPublisherInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark)
                    ) {
                        Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                    }
                }
            }
        }

        // Section 6: Brand Keywords & Aliases
        item {
            CardContainer(title = "Monitored Keywords & Aliases (${brandKeywords.size})") {
                Text(
                    text = "Used by the detection engine to identify trademark scraping in profile bios and look-alike usernames.",
                    fontSize = 11.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    brandKeywords.forEach { kw ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(VividLimeDark)
                                .border(0.5.dp, VividLime, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(kw, fontSize = 11.sp, color = VividLime, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newKeywordInput,
                        onValueChange = { newKeywordInput = it },
                        placeholder = { Text("Add monitored keyword...", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        colors = brandFieldColors()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newKeywordInput.isNotBlank()) {
                                brandKeywords = brandKeywords + newKeywordInput.trim()
                                newKeywordInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VividLime, contentColor = PrimaryDark)
                    ) {
                        Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
                    }
                }
            }
        }
    }
}

@Composable
private fun CardContainer(
    title: String,
    content: @Composable ColumnScope.() -> Unit
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
            Text(
                text = title.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun brandFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = SurfaceDark,
    unfocusedContainerColor = SurfaceDark,
    focusedBorderColor = ElectricCyan,
    unfocusedBorderColor = SurfaceBorder,
    focusedTextColor = TextWhite,
    unfocusedTextColor = TextWhite
)
