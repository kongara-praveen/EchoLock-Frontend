package com.example.echolock.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.echolock.navigation.AppTheme
import com.example.echolock.navigation.currentAppTheme
import com.example.echolock.ui.theme.AppColors

@Composable
fun AppThemeScreen(onBack: () -> Unit) {

    val selectedTheme = currentAppTheme.value

    // Screen entrance animation
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        // 🔙 Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = AppColors.TextPrimary,
                modifier = Modifier.clickable { onBack() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "App Theme",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        Spacer(Modifier.height(30.dp))

        ThemeItem(
            title = "Light Mode",
            selected = selectedTheme == AppTheme.LIGHT
        ) {
            currentAppTheme.value = AppTheme.LIGHT
        }

        ThemeItem(
            title = "Dark Mode",
            selected = selectedTheme == AppTheme.DARK
        ) {
            currentAppTheme.value = AppTheme.DARK
        }

        ThemeItem(
            title = "System Default",
            selected = selectedTheme == AppTheme.SYSTEM
        ) {
            currentAppTheme.value = AppTheme.SYSTEM
        }
    }
}

@Composable
fun ThemeItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) AppColors.PrimaryDark.copy(alpha = 0.1f) else AppColors.Surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 4.dp else 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = AppColors.TextPrimary
            )

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = AppColors.PrimaryDark
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))
}
