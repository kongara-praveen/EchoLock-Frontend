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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.session.UserSession
import com.example.echolock.util.SaveStegoAudio
import java.io.File

@Composable
fun EncryptionCompleteScreen(
    onBackDashboard: () -> Unit
) {
    val context = LocalContext.current

    val stegoFilePath = UserSession.stegoAudioPath
    val stegoFile = stegoFilePath?.let { File(it) }

    var isSaved by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        /* ✅ SUCCESS ICON */
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(Color(0xFFE8FFF1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color(0xFF1EC971),
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(Modifier.height(25.dp))

        Text(
            text = "Encryption Complete",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Your message has been securely hidden inside the audio file.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = when {
                isSaved -> "Saved to Downloads / EchoLock"
                error != null -> error!!
                else -> "Not saved to device yet"
            },
            fontSize = 14.sp,
            color = if (error != null) Color.Red else Color(0xFF6B7E80)
        )

        Spacer(Modifier.height(22.dp))

        /* ⬇️ DOWNLOAD BUTTON */
        Button(
            enabled = !isSaved,
            onClick = {
                error = null

                if (stegoFile == null || !stegoFile.exists() || stegoFile.length() == 0L) {
                    error = "Encrypted audio not found"
                    return@Button
                }

                val success = SaveStegoAudio.saveStegoAudioToDownloads(
                    context = context,
                    stegoFile = stegoFile
                )

                if (success) {
                    isSaved = true
                } else {
                    error = "Failed to save encrypted audio"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF005F73)
            )
        ) {
            Text(
                text = if (isSaved) "Downloaded" else "Download Encrypted Audio",
                color = Color.White,
                fontSize = 15.sp
            )
        }

        Spacer(Modifier.height(28.dp))

        /* 📊 SECURITY REPORT */
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color(0xFFF6F8F9)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "Security Report",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF062A2F)
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
            color = Color(0xFF005F73),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable {
                // 🔥 CLEAR SESSION (VERY IMPORTANT)
                UserSession.decryptAudioUri = null
                UserSession.secretMessage = null
                UserSession.stegoAudioPath = null

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
        Text(label, color = Color(0xFF6B7E80), fontSize = 14.sp)
        Text(
            value,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF062A2F),
            fontSize = 14.sp
        )
    }
}
