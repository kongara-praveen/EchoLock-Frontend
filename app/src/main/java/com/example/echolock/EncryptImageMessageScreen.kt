package com.example.echolock.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.util.ImageSteganography
import com.example.echolock.util.saveStegoImage

@Composable
fun EncryptImageMessageScreen(
    imageUri: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {

        /* ---------- TOP BAR ---------- */
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = "Secret Message",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- INFO CARD ---------- */
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEFFFF9)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Your message will be securely hidden inside the selected image using LSB steganography.",
                fontSize = 14.sp,
                color = Color(0xFF005F73),
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- MESSAGE INPUT ---------- */
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            placeholder = {
                Text("Type your secret message here…")
            },
            shape = RoundedCornerShape(14.dp),
            maxLines = 6
        )

        Spacer(Modifier.height(30.dp))

        /* ---------- ACTION BUTTON ---------- */
        Button(
            onClick = {
                if (message.isBlank()) {
                    Toast.makeText(
                        context,
                        "Message cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                loading = true

                try {
                    val uri = Uri.parse(imageUri)
                    val stream = context.contentResolver.openInputStream(uri)
                        ?: throw Exception("Cannot open image")

                    val bitmap = BitmapFactory.decodeStream(stream)
                        ?: throw Exception("Image decode failed")

                    val stegoBitmap =
                        ImageSteganography.encode(bitmap, message)

                    saveStegoImage(context, stegoBitmap)

                    loading = false
                    onSuccess()

                } catch (e: Exception) {
                    loading = false
                    Toast.makeText(
                        context,
                        "Encryption failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            enabled = !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF005F73)
            )
        ) {
            Text(
                text = if (loading) "Encrypting…" else "Encrypt & Hide",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
