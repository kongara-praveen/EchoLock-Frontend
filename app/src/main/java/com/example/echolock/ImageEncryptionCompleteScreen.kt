package com.example.echolock.ui.screens

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
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
import com.example.echolock.util.HistoryTempStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImageEncryptionCompleteScreen(
    onBackDashboard: () -> Unit
) {
    val context = LocalContext.current

    // ✅ GET REAL IMAGE NAME (SET DURING UPLOAD)
    val imageName = HistoryTempStore.lastImageFileName ?: return

    /* -------- SAVE HISTORY (RUNS ONCE AUTOMATICALLY) -------- */
    LaunchedEffect(key1 = imageName) {
        try {
            withContext(Dispatchers.IO) {
                val userId = UserSession.userId.toIntOrNull() ?: 1
                RetrofitClient.instance.addHistory(
                    userId = userId,
                    fileName = imageName,
                    action = "Encrypted Image"
                )
            }

            // ✅ OPTIONAL: Clear after saving (prevents duplicate history)
            HistoryTempStore.lastImageFileName = null

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
            .background(AppColors.Background)
            .alpha(alpha)
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = AppColors.Success,
            modifier = Modifier.size(110.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Encryption Complete",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Your message has been securely hidden within the image.",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = AppColors.TextSecondary
        )

        Spacer(Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryDark),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            ),
            onClick = {
                downloadEncryptedImage(context, imageName)
            }
        ) {
            Text("Download Image", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Back to Dashboard",
            color = AppColors.PrimaryDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { 
                // Clear image encryption session
                UserSession.clearImageSession()
                onBackDashboard() 
            }
        )
    }
}

/* ---------------- DOWNLOAD FUNCTION (NO HISTORY HERE) ---------------- */

fun downloadEncryptedImage(
    context: Context,
    imageName: String
) {
    val url =
        "http://10.0.2.2/echolock/image/download_image.php?image_name=$imageName"

    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle("EchoLock Encrypted Image")
        .setDescription("Downloading encrypted image")
        .setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        .setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            imageName
        )
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)

    val downloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    downloadManager.enqueue(request)
}
