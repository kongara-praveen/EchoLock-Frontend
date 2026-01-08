package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.echolock.util.SaveStegoAudio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import com.example.echolock.ui.theme.GradientBackgrounds
import com.example.echolock.ui.theme.FeatureCardColors


@Composable
fun EncryptionCompleteScreen(
    onBackDashboard: () -> Unit
) {
    val context = LocalContext.current

    val stegoFilePath = UserSession.stegoAudioPath
    val stegoFile = stegoFilePath?.let { File(it) }

    var isSaved by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Save history when screen appears
    LaunchedEffect(Unit) {
        val audioFileName = UserSession.originalAudioName ?: "audio_file"
        val userId = UserSession.userId.toIntOrNull() ?: 1
        
        try {
            withContext(Dispatchers.IO) {
                RetrofitClient.instance.addHistory(
                    userId = userId,
                    fileName = audioFileName,
                    action = "Encrypted Audio"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Animation for screen entrance
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "screen_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientBackgrounds.PrimaryGradient)
            .alpha(alpha)
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /* ✅ SUCCESS ICON */
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(AppColors.Success.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = AppColors.Success,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "Encryption Complete",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Your message has been securely hidden inside the audio file.",
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = when {
                isSaved -> "Saved to Downloads / EchoLock"
                error != null -> error!!
                else -> "Not saved to device yet"
            },
            fontSize = 14.sp,
            color = if (error != null) AppColors.Error else AppColors.TextSecondary
        )

        Spacer(Modifier.height(24.dp))

        /* ⬇️ DOWNLOAD BUTTON */
        val buttonEnabled by remember { derivedStateOf { !isSaved } }
        val buttonAlpha by animateFloatAsState(
            targetValue = if (buttonEnabled) 1f else 0.6f,
            animationSpec = tween(durationMillis = 200),
            label = "button_alpha"
        )

        Button(
            enabled = buttonEnabled,
            onClick = {
                error = null

                if (stegoFile == null || !stegoFile.exists() || stegoFile.length() == 0L) {
                    error = "Encrypted audio not found"
                    return@Button
                }

                val originalFileName = UserSession.originalAudioName
                val success = SaveStegoAudio.saveStegoAudioToDownloads(
                    context = context,
                    stegoFile = stegoFile,
                    originalFileName = originalFileName
                )

                if (success) {
                    isSaved = true
                } else {
                    error = "Failed to save encrypted audio"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(buttonAlpha),
            colors = ButtonDefaults.buttonColors(
                containerColor = FeatureCardColors.Blue,
                disabledContainerColor = Color(0xFF475569)
            ),

                    shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = if (isSaved) "Downloaded" else "Download Encrypted Audio",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(28.dp))

        /* 📊 SECURITY REPORT */
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = FeatureCardColors.Blue.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(16.dp)
        )
        {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Security Report",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(Modifier.height(18.dp))

                ReportItem("Stego Method", "LSB-Audio")
                Spacer(Modifier.height(12.dp))

                ReportItem("Encoding", "UTF-8")
                Spacer(Modifier.height(12.dp))

                ReportItem(
                    "File Status",
                    if (isSaved) "Saved" else "Pending"
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        /* 🔙 BACK */
        Text(
            text = "Back to Dashboard",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable {
                // 🔥 CLEAR SESSION (VERY IMPORTANT)
                UserSession.clearAudioEncryptionSession()
                UserSession.decryptAudioUri = null

                onBackDashboard()
            }
        )
    }
}

@Composable
fun ReportItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
        Text(
            value,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp
        )

    }
}
