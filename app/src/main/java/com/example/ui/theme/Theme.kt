package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * SENTRIX Enterprise Identity Color Scheme:
 * - Primary Background: #0B0D0F (PrimaryDark)
 * - Brand Accent: #D7FF00 (VividLime)
 * - Secondary Accent: #00D4E8 (ElectricCyan)
 */
val SentrixDarkColorScheme = darkColorScheme(
    primary = VividLime,               // #D7FF00 Primary Brand Accent
    onPrimary = PrimaryDark,           // #0B0D0F High contrast on brand accent
    primaryContainer = VividLimeDark,
    onPrimaryContainer = VividLime,
    secondary = ElectricCyan,          // #00D4E8 Secondary Accent
    onSecondary = PrimaryDark,         // #0B0D0F
    secondaryContainer = ElectricCyanDark,
    onSecondaryContainer = ElectricCyan,
    tertiary = SafeEmerald,
    onTertiary = PrimaryDark,
    background = PrimaryDark,          // #0B0D0F Primary Background
    onBackground = TextWhite,
    surface = SecondaryDark,
    onSurface = TextWhite,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextMuted,
    outline = SurfaceBorder,
    outlineVariant = SurfaceBorderLight,
    error = DangerCoral,
    onError = Color.White
)

@Composable
fun SentrixTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SentrixDarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    SentrixTheme(content = content)
}
