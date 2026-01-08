package com.example.echolock.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.util.ImageSteganography
import kotlinx.coroutines.delay
import com.example.echolock.ui.theme.GradientBackgrounds


@Composable
fun ImageDecryptionProgressScreen(
    imageUri: String,
    onCompleted: (imageName: String, extractedMessage: String) -> Unit,
    onFailed: () -> Unit
) {
    val context = LocalContext.current
    var progress by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {

        // Fake progress animation
        while (progress < 90) {
            delay(40)
            progress++
        }

        try {
            val uri = Uri.parse(imageUri)
            val inputStream = context.contentResolver.openInputStream(uri)

            if (inputStream == null) {
                Toast.makeText(context, "Unable to open image", Toast.LENGTH_SHORT).show()
                onFailed()
                return@LaunchedEffect
            }

            val bitmap = BitmapFactory.decodeStream(inputStream)

            if (bitmap == null) {
                Toast.makeText(context, "Invalid image file", Toast.LENGTH_SHORT).show()
                onFailed()
                return@LaunchedEffect
            }

            // 🔓 REAL DECRYPTION (UPDATED)
            val password = UserSession.decryptImagePassword
            val decodedResult = ImageSteganography.decode(bitmap, password)

            if (decodedResult == null) {
                Toast.makeText(
                    context,
                    "No hidden message found in this image",
                    Toast.LENGTH_LONG
                ).show()
                onFailed()
                return@LaunchedEffect
            }

            // Check for password errors
            if (decodedResult.message == "WRONG_PASSWORD") {
                Toast.makeText(
                    context,
                    "Wrong password",
                    Toast.LENGTH_LONG
                ).show()
                onFailed()
                return@LaunchedEffect
            }

            if (decodedResult.message == "PASSWORD_REQUIRED") {
                Toast.makeText(
                    context,
                    "Password required",
                    Toast.LENGTH_LONG
                ).show()
                onFailed()
                return@LaunchedEffect
            }

            if (decodedResult.message.isBlank()) {
                Toast.makeText(
                    context,
                    "No hidden message found in this image",
                    Toast.LENGTH_LONG
                ).show()
                onFailed()
                return@LaunchedEffect
            }

            progress = 100
            delay(300)

            val imageName = uri.lastPathSegment ?: "EncryptedImage.png"

            // ✅ PASS ONLY MESSAGE STRING
            onCompleted(imageName, decodedResult.message)

        } catch (e: Exception) {
            Toast.makeText(context, "Decryption failed", Toast.LENGTH_SHORT).show()
            onFailed()
        }
    }

    // Animation for progress
    val progressAlpha by animateFloatAsState(
        targetValue = if (progress > 0) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "progress_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientBackgrounds.PrimaryGradient)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            progress = progress / 100f,
            color = Color.White,
            strokeWidth = 6.dp,
            modifier = Modifier
                .size(110.dp)
                .alpha(progressAlpha)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "$progress%",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Decrypting image...",
            fontSize = 16.sp,
            color = Color.White
        )
    }
}
