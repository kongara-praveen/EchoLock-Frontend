package com.example.echolock.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color Palette
object AppColors {
    // Primary Colors
    val Primary = Color(0xFF006D77)
    val PrimaryDark = Color(0xFF005F73)
    val PrimaryLight = Color(0xFF83C5BE)
    
    // Background Colors
    val Background = Color(0xFFF8FAFC)
    val Surface = Color.White
    val SurfaceDark = Color(0xFF1E293B)
    
    // Text Colors (Bright and visible)
    val TextPrimary = Color(0xFF0A2E45)      // Dark text for light backgrounds
    val TextSecondary = Color(0xFF6B7E80)    // Secondary text
    val TextTertiary = Color(0xFF8A9AA0)      // Tertiary/placeholder text
    val TextOnPrimary = Color.White           // White text on primary color
    
    // Status Colors
    val Success = Color(0xFF4CAF50)
    val Error = Color(0xFFE53935)
    val Warning = Color(0xFFFF9800)
    val Info = Color(0xFF2196F3)
    
    // Border Colors
    val BorderLight = Color(0xFFD0DBDF)
    val BorderMedium = Color(0xFFB8C5CA)
    
    // Card Colors
    val CardBackground = Color.White
    val CardElevated = Color(0xFFF1F5F9)
}

// Spacing
object AppSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
}

// Typography Sizes
object AppTypography {
    val h1 = 32.sp
    val h2 = 26.sp
    val h3 = 22.sp
    val h4 = 20.sp
    val body = 16.sp
    val bodySmall = 14.sp
    val caption = 12.sp
}

