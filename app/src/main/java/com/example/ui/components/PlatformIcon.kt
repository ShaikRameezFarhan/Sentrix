package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PlatformIconBadge(
    platform: String,
    modifier: Modifier = Modifier
) {
    val (icon, tint, bgColor) = when {
        platform.contains("Twitter", ignoreCase = true) || platform.contains("X", ignoreCase = true) ->
            Triple(Icons.Default.AlternateEmail, Color(0xFF1DA1F2), Color(0xFF0F2B48))
        platform.contains("Instagram", ignoreCase = true) ->
            Triple(Icons.Default.CameraAlt, Color(0xFFE1306C), Color(0xFF381224))
        platform.contains("LinkedIn", ignoreCase = true) ->
            Triple(Icons.Default.Business, Color(0xFF0A66C2), Color(0xFF0B213F))
        platform.contains("Facebook", ignoreCase = true) ->
            Triple(Icons.Default.ThumbUp, Color(0xFF1877F2), Color(0xFF0E2345))
        platform.contains("Telegram", ignoreCase = true) ->
            Triple(Icons.AutoMirrored.Filled.Send, Color(0xFF229ED9), Color(0xFF0A2638))
        platform.contains("Play", ignoreCase = true) || platform.contains("Android", ignoreCase = true) ->
            Triple(Icons.Default.Android, Color(0xFF3DDC84), Color(0xFF0A2E1A))
        platform.contains("Apple", ignoreCase = true) || platform.contains("iOS", ignoreCase = true) ->
            Triple(Icons.Default.PhoneIphone, Color(0xFFF8FAFC), Color(0xFF1E293B))
        else ->
            Triple(Icons.Default.Language, ElectricCyan, SurfaceCardHover)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(0.5.dp, tint.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = platform,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color = VividLime,
    icon: ImageVector = Icons.Default.Shield,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    letterSpacing = 0.8.sp
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = TextSubtle
            )
        }
    }
}
