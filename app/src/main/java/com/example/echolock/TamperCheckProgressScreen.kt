package com.example.echolock.ui.screens

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.util.TamperCheckUtil
import com.example.echolock.util.uriToFile
import kotlinx.coroutines.delay

@Composable
fun TamperCheckProgressScreen(
    fileUri: Uri,
    originalFileName: String? = null,
    onResult: (Boolean, String) -> Unit
) {
    val context = LocalContext.current

    var progress by remember { mutableFloatStateOf(0f) }
    var percent by remember { mutableIntStateOf(0) }

    var result by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var navigated by remember { mutableStateOf(false) }

    // Animation for screen entrance
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )
    
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "content_scale"
    )

    /* ================== WORK ================== */
    LaunchedEffect(fileUri, originalFileName) {

        val file = uriToFile(context, fileUri)
        val isSafe = TamperCheckUtil.checkFile(file, originalFileName)

        for (i in 1..100) {
            delay(18)
            percent = i
            progress = i / 100f
        }

        result = isSafe to file.name
    }

    /* ================== SAFE NAVIGATION ================== */
    LaunchedEffect(result) {
        if (result != null && !navigated) {
            navigated = true
            onResult(result!!.first, result!!.second)
        }
    }

    /* ================== UI ================== */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            progress = progress,
            color = AppColors.PrimaryDark,
            strokeWidth = 8.dp,
            modifier = Modifier
                .size(140.dp)
                .scale(scale),
            trackColor = AppColors.BorderLight
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "$percent%",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Checking file integrity...\nPlease wait.",
            textAlign = TextAlign.Center,
            color = AppColors.TextSecondary,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
    }
}
