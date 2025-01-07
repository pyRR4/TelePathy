package com.example.telepathy.presentation.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme


val DarkBackgroundColor = Color.DarkGray
val DarkButtonsColor = Color.Gray
val DarkFontColor = Color(0xFFCCCCCC)

// User dark pastel colors
val DarkRed = Color(0xFFB97A7A)       // Soft Red
val DarkPink = Color(0xFFC48B9F)      // Soft Pink
val DarkPurple = Color(0xFF9F7FAE)    // Soft Purple
val DarkLightBlue = Color(0xFF6D9BCB) // Soft Light Blue
val DarkTeal = Color(0xFF589D94)      // Soft Teal
val DarkGreen = Color(0xFF7DAA84)     // Soft Green
val DarkYellow = Color(0xFFC9B276)    // Soft Yellow
val DarkOrange = Color(0xFFC98D7A)    // Soft Orange
val DarkBrown = Color(0xFF8A7267)     // Soft Brown
val DarkVividBlue = Color(0xFF5080C7) // Soft Vivid Blue
val DarkLime = Color(0xFFB7BD83)      // Soft Lime
val DarkDeepPurple = Color(0xFF7F6AAE) // Soft Deep Purple

val DarkUserColors = listOf(
    DarkRed,
    DarkPink,
    DarkPurple,
    DarkLightBlue,
    DarkTeal,
    DarkGreen,
    DarkYellow,
    DarkOrange,
    DarkBrown,
    DarkVividBlue,
    DarkLime,
    DarkDeepPurple
)

val AlertRed = Color(0xFFB00020)


val DarkThemeColors = darkColorScheme(
    primary = Color(0xFF1E1E1E), // Dark Primary Color for emphasis
    secondary = Color(0xFF2C2C2C), // Secondary dark shade
    background = DarkBackgroundColor,
    surface = Color(0xFF333333),  // Dark Surface Color
    onPrimary = DarkFontColor,    // Light text color for primary
    onSecondary = DarkFontColor.copy(alpha = 0.8f),  // Light text color for secondary
    onBackground = DarkFontColor, // Light text on background
    onSurface = DarkFontColor,    // Light text on surfaces
)


val LightBackgroundColor = Color.White
val LightButtonsColor = Color.LightGray
val LightFontColor = Color.Black

val LightRed = Color(0xFFE57373)       // Soft Red
val LightPink = Color(0xFFF06292)      // Soft Pink
val LightPurple = Color(0xFFBA68C8)    // Soft Purple
val LightLightBlue = Color(0xFF64B5F6) // Soft Light Blue
val LightTeal = Color(0xFF4DB6AC)      // Soft Teal
val LightGreen = Color(0xFF81C784)     // Soft Green
val LightYellow = Color(0xFFFFD54F)    // Soft Yellow
val LightOrange = Color(0xFFFF8A65)    // Soft Orange
val LightBrown = Color(0xFFA1887F)     // Soft Brown
val LightVividBlue = Color(0xFF2979FF) // Soft Vivid Blue
val LightLime = Color(0xFFDCE775)      // Soft Lime
val LightDeepPurple = Color(0xFF673AB7) // Soft Deep Purple

// Custom Colors List for Light Theme (User-selected colors)
val LightUserColors = listOf(
    LightRed,
    LightPink,
    LightPurple,
    LightLightBlue,
    LightTeal,
    LightGreen,
    LightYellow,
    LightOrange,
    LightBrown,
    LightVividBlue,
    LightLime,
    LightDeepPurple
)

val LightThemeColors = lightColorScheme(
    primary = Color(0xFF6200EE), // Vibrant primary color for light theme
    secondary = Color(0xFF03DAC5), // Teal secondary
    background = LightBackgroundColor,
    surface = Color(0xFFEEEEEE),  // Light surface color
    onPrimary = LightFontColor,   // Dark text on primary color
    onSecondary = LightFontColor, // Dark text on secondary
    onBackground = LightFontColor, // Dark text on background
    onSurface = LightFontColor    // Dark text on surfaces
)