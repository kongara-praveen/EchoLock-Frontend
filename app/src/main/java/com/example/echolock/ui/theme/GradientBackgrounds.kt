package com.example.echolock.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object GradientBackgrounds {
    // Primary gradient - Dark blue to purple (most common)
    val PrimaryGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1F3A),  // Dark blue
            Color(0xFF2D1B4E),  // Purple-blue
            Color(0xFF4A2C7A)   // Deep purple
        )
    )
    
    // Success gradient - Green tones
    val SuccessGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F5132),  // Dark green
            Color(0xFF198754),  // Medium green
            Color(0xFF20C997)   // Light green
        )
    )
    
    // Info gradient - Blue tones
    val InfoGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D47A1),  // Dark blue
            Color(0xFF1976D2),  // Medium blue
            Color(0xFF42A5F5)   // Light blue
        )
    )
    
    // Warning gradient - Orange tones
    val WarningGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE65100),  // Dark orange
            Color(0xFFF57C00),  // Medium orange
            Color(0xFFFF9800)   // Light orange
        )
    )
    
    // Purple gradient - For special screens
    val PurpleGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4A148C),  // Dark purple
            Color(0xFF6A1B9A),  // Medium purple
            Color(0xFF9C27B0)   // Light purple
        )
    )
    
    // Teal gradient - For settings/preferences
    val TealGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF004D40),  // Dark teal
            Color(0xFF00796B),  // Medium teal
            Color(0xFF009688)   // Light teal
        )
    )
    
    // Dark gradient - For dark mode screens
    val DarkGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B132B),  // Very dark blue
            Color(0xFF1C2541),  // Dark blue-gray
            Color(0xFF3A506B)   // Medium blue-gray
        )
    )
}

// Color cards for features
object FeatureCardColors {
    val Purple = Color(0xFF6366F1)
    val Green = Color(0xFF10B981)
    val Orange = Color(0xFFF59E0B)
    val Blue = Color(0xFF3B82F6)
    val Red = Color(0xFFEF4444)
    val Pink = Color(0xFFEC4899)
    val Cyan = Color(0xFF06B6D4)
    val Indigo = Color(0xFF6366F1)
}

