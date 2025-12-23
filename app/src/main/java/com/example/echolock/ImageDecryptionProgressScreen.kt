package com.example.echolock.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.util.ImageSteganography
import kotlinx.coroutines.delay

@Composable
fun ImageDecryptionProgressScreen(
    imageUri: String,
    onCompleted: (imageName: String, extractedMessage: String) -> Unit,
    onFailed: () -> Unit           // ✅ ADD THIS
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

            // 🔓 REAL DECRYPTION
            val extractedMessage = ImageSteganography.decode(bitmap)

            if (extractedMessage == null || extractedMessage.isBlank()) {
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

            // ✅ SUCCESS ONLY HERE
            onCompleted(imageName, extractedMessage)

        } catch (e: Exception) {
            Toast.makeText(context, "Decryption failed", Toast.LENGTH_SHORT).show()
            onFailed()
        }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            progress = progress / 100f,
            color = Color(0xFF005F73),
            strokeWidth = 6.dp,
            modifier = Modifier.size(110.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            "$progress%",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Decrypting image...",
            fontSize = 15.sp,
            color = Color.Gray
        )
    }
}
