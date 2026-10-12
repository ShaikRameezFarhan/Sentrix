package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DetectionStatus
import com.example.data.model.RiskLevel
import com.example.ui.theme.*

@Composable
fun RiskBadge(
    riskLevel: RiskLevel,
    riskScore: Int? = null,
    modifier: Modifier = Modifier
) {
    val (bgColor, borderColor, textColor, label) = when (riskLevel) {
        RiskLevel.CRITICAL -> Quad(CriticalRedBg, CriticalRed, Color(0xFFFF859B), "CRITICAL")
        RiskLevel.HIGH -> Quad(DangerCoralBg, DangerCoral, Color(0xFFFCA5A5), "HIGH")
        RiskLevel.MEDIUM -> Quad(WarningAmberBg, WarningAmber, Color(0xFFFDE68A), "MEDIUM")
        RiskLevel.LOW -> Quad(SafeEmeraldBg, SafeEmerald, Color(0xFF6EE7B7), "LOW")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(borderColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = if (riskScore != null) "$label $riskScore" else label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: DetectionStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        DetectionStatus.NEW -> Triple(VividLimeDark, VividLime, "NEW")
        DetectionStatus.INVESTIGATING -> Triple(WarningAmber.copy(alpha = 0.2f), WarningAmber, "UNDER INVESTIGATION")
        DetectionStatus.CONFIRMED -> Triple(DangerCoral.copy(alpha = 0.2f), DangerCoral, "CONFIRMED THREAT")
        DetectionStatus.FALSE_POSITIVE -> Triple(Color(0xFF475569).copy(alpha = 0.3f), Color(0xFF94A3B8), "FALSE POSITIVE")
        DetectionStatus.RESOLVED -> Triple(SafeEmerald.copy(alpha = 0.2f), SafeEmerald, "RESOLVED")
        DetectionStatus.DISMISSED -> Triple(Color(0xFF334155).copy(alpha = 0.4f), Color(0xFF64748B), "DISMISSED")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(0.5.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 2.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
