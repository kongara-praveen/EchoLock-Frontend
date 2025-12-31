package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.api.RetrofitClient
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TamperCheckCompleteScreen(
    isSafe: Boolean,
    fileName: String,
    onBackDashboard: () -> Unit,
    onCheckAgain: () -> Unit
) {

    val icon =
        if (isSafe) Icons.Default.CheckCircle else Icons.Default.Warning

    val title =
        if (isSafe) "File is Safe" else "Tampered Detected"

    val description =
        if (isSafe)
            "This file has not been modified after encryption."
        else
            "This file has been modified or corrupted after encryption."

    val color =
        if (isSafe) AppColors.Success else AppColors.Error

    // Animation for screen entrance
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    // Save history when screen appears
    LaunchedEffect(fileName) {
        val userId = UserSession.userId.toIntOrNull() ?: 1
        val action = if (isSafe) "Tamper Check - Safe" else "Tamper Check - Tampered"
        
        try {
            withContext(Dispatchers.IO) {
                RetrofitClient.instance.addHistory(
                    userId = userId,
                    fileName = fileName,
                    action = action
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(120.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            color = AppColors.TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = fileName,
            fontSize = 13.sp,
            color = AppColors.TextTertiary
        )

        Spacer(Modifier.height(40.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.PrimaryDark
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            ),
            onClick = onBackDashboard
        ) {
            Text("Back to Dashboard", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Run Tamper Check Again",
            color = AppColors.PrimaryDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onCheckAgain() }
        )
    }
}
