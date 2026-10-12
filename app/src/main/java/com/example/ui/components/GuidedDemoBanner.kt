package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun GuidedDemoBanner(
    currentStep: String,
    onNavigateTo: (String) -> Unit,
    onResetDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, VividLime.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(VividLime)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ACTIVE THREAT DISCOVERY ROADMAP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VividLime,
                    letterSpacing = 0.8.sp
                )
            }

            // Reset demo button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceCardHover)
                    .border(0.5.dp, SurfaceBorderLight, RoundedCornerShape(6.dp))
                    .clickable { onResetDemo() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("reset_demo_btn"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset Demo",
                    tint = TextMuted,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Reset Demo Data",
                    fontSize = 10.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Quick Flow Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DemoStepChip(
                label = "1. Social Threats",
                subtext = "Look-Alikes & Redirection",
                isSelected = currentStep == "social",
                onClick = { onNavigateTo("social") },
                testTag = "demo_step_social"
            )
            DemoStepChip(
                label = "2. Rogue Apps",
                subtext = "Trojans & Developer Mismatch",
                isSelected = currentStep == "apps",
                onClick = { onNavigateTo("apps") },
                testTag = "demo_step_apps"
            )
            DemoStepChip(
                label = "3. Brand Profile",
                subtext = "Verified Asset Whitelist",
                isSelected = currentStep == "brand",
                onClick = { onNavigateTo("brand") },
                testTag = "demo_step_brand"
            )
            DemoStepChip(
                label = "4. IRIS AI Copilot",
                subtext = "Forensics & Takedown Drafts",
                isSelected = currentStep == "iris",
                onClick = { onNavigateTo("iris") },
                testTag = "demo_step_iris"
            )
            DemoStepChip(
                label = "5. Investigations",
                subtext = "Case Timeline & Evidence",
                isSelected = currentStep == "investigations",
                onClick = { onNavigateTo("investigations") },
                testTag = "demo_step_investigations"
            )
            DemoStepChip(
                label = "6. Remediation",
                subtext = "Takedown Enforcement",
                isSelected = currentStep == "remediation",
                onClick = { onNavigateTo("remediation") },
                testTag = "demo_step_remediation"
            )
        }
    }
}

@Composable
private fun DemoStepChip(
    label: String,
    subtext: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val bg by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) VividLimeDark else SurfaceDark,
        label = "demo_chip_bg"
    )
    val border by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) VividLime else SurfaceBorder,
        label = "demo_chip_border"
    )
    val textColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) VividLime else TextWhite,
        label = "demo_chip_text"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = subtext,
            fontSize = 9.sp,
            color = if (isSelected) ElectricCyan else TextSubtle
        )
    }
}
