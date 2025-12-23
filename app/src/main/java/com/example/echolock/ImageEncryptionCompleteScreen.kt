package com.example.echolock.ui.screens

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImageEncryptionCompleteScreen(
    imageName: String,
    onBackDashboard: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF00A86B),
            modifier = Modifier.size(110.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Encryption Complete",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Your message has been securely hidden within the image.",
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                downloadEncryptedImage(context, imageName)
            }
        ) {
            Text("Download Image", color = Color.White)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Back to Dashboard",
            color = Color(0xFF005F73),
            modifier = Modifier.clickable { onBackDashboard() }
        )
    }
}

/* ---------------- DOWNLOAD FUNCTION ---------------- */

fun downloadEncryptedImage(
    context: Context,
    imageName: String
) {
    val url =
        "http://10.0.2.2/echolock/image/download_image.php?image_name=$imageName"
    // 🔁 Replace IP for real device

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
