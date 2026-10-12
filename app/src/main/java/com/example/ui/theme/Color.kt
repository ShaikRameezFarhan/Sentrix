package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// SENTRIX Enterprise Cybersecurity Identity
// Primary Background
val PrimaryDark = Color(0xFF0B0D0F)           // #0B0D0F Deep near-black background
val SentrixBackground = PrimaryDark

// Surfaces & Structural Borders
val SecondaryDark = Color(0xFF15191C)         // Charcoal secondary background
val SurfaceCard = Color(0xFF1A2024)           // Dark charcoal card surface
val SurfaceCardHover = Color(0xFF222A30)      // Active card highlight
val SurfaceBorder = Color(0xFF30373C)         // Subtle charcoal-grey border
val SurfaceBorderLight = Color(0xFF434D54)     // Focused border

// Signature Brand Accents
val VividLime = Color(0xFFD7FF00)             // #D7FF00 Primary Brand Accent
val SentrixBrandAccent = VividLime
val VividLimeDark = Color(0xFF1D2600)         // Lime container background

// Secondary Accents
val ElectricCyan = Color(0xFF00D4E8)          // #00D4E8 Secondary Accent
val SentrixSecondaryAccent = ElectricCyan
val ElectricCyanDark = Color(0xFF072930)      // Cyan container background
val CyberBlue = VividLime                     // Compatibility mapped to brand accent

// Status & Severity Colors
val SafeEmerald = Color(0xFF36D399)           // Accessible green success
val SafeEmeraldBg = Color(0xFF0C2B1F)         // Green container
val WarningAmber = Color(0xFFF59E0B)          // Warning amber
val WarningAmberBg = Color(0xFF362007)        // Amber container
val DangerCoral = Color(0xFFFF4D5A)           // Accessible red critical threat
val DangerCoralBg = Color(0xFF381014)         // Red container
val CriticalRed = Color(0xFFFF4D5A)           // Critical threat red
val CriticalRedBg = Color(0xFF381014)

// Text & Neutral Colors
val TextWhite = Color(0xFFF5F7F8)             // Off-white primary text
val TextMuted = Color(0xFFA7B0B8)             // Cool grey secondary text
val TextSubtle = Color(0xFF707C87)            // Technical metadata grey
val DarkNavyTrack = Color(0xFF15191C)

// Compatibility aliases mapped to SENTRIX palette
val MidnightDark = PrimaryDark
val SurfaceDark = SecondaryDark
val DeepIndigo = SurfaceCard
val AccentCyan = ElectricCyan
val BrandLime = VividLime
