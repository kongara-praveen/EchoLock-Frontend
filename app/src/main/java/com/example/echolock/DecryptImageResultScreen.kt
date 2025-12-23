package com.example.echolock.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R

@Composable
fun DecryptImageResultScreen(
    imageName: String,
    extractedMessage: String,
    onBack: () -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val hasMessage = extractedMessage.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        /* ---------- HEADER ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )

            Text(
                text = "Message Extracted",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- STATUS ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = if (hasMessage) Color(0xFF00A86B) else Color.Red,
                modifier = Modifier.size(40.dp)
            )

            Column(Modifier.padding(start = 10.dp)) {
                Text(
                    text = if (hasMessage)
                        "Decryption Successful"
                    else
                        "No Hidden Message Found",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Source: $imageName",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- MESSAGE BOX ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color(0xFFF7F9FA), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                text = if (hasMessage)
                    extractedMessage
                else
                    "This image does not contain any hidden message.",
                fontSize = 14.sp,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- COPY BUTTON ---------- */
        Button(
            onClick = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE)
                            as ClipboardManager

                val clip = ClipData.newPlainText(
                    "EchoLock Message",
                    extractedMessage
                )
                clipboard.setPrimaryClip(clip)

                Toast.makeText(
                    context,
                    "Message copied to clipboard",
                    Toast.LENGTH_SHORT
                ).show()
            },
            enabled = hasMessage,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE7FBF6),
                disabledContainerColor = Color(0xFFE0E0E0)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_copy),
                contentDescription = null,
                tint = Color(0xFF005F73)
            )
            Spacer(Modifier.width(6.dp))
            Text("Copy to Clipboard", color = Color(0xFF005F73))
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- DONE ---------- */
        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF005F73)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done", color = Color.White)
        }
    }
}
