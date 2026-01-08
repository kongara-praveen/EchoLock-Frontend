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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.api.RetrofitClient
import com.example.echolock.R
import com.example.echolock.session.UserSession
import com.example.echolock.ui.theme.AppColors
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.echolock.ui.theme.GradientBackgrounds
import com.example.echolock.ui.theme.FeatureCardColors


@Composable
fun DecryptImageResultScreen(
    imageName: String,
    extractedMessage: String,
    onBack: () -> Unit,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val hasMessage = extractedMessage.isNotBlank()

    // Save history when screen appears
    LaunchedEffect(imageName) {
        val userId = UserSession.userId.toIntOrNull() ?: 1
        
        try {
            withContext(Dispatchers.IO) {
                RetrofitClient.instance.addHistory(
                    userId = userId,
                    fileName = imageName,
                    action = "Decrypted Image"
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
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBack() },
                tint = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Message Extracted",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            )
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- STATUS ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = if (hasMessage) AppColors.Success else AppColors.Error
                ,
                modifier = Modifier.size(40.dp)
            )

            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = if (hasMessage)
                        "Decryption Successful"
                    else
                        "No Hidden Message Found",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Source: $imageName",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- MESSAGE BOX ---------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    FeatureCardColors.Orange.copy(alpha = 0.9f),
                    RoundedCornerShape(16.dp)
                )

                .padding(16.dp)
        ) {
            Text(
                text = if (hasMessage)
                    extractedMessage
                else
                    "This image does not contain any hidden message.",
                fontSize = 15.sp,
                color = Color.White,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- COPY BUTTON ---------- */
        OutlinedButton(
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
                containerColor = FeatureCardColors.Orange,
                disabledContainerColor = Color(0xFF475569)
            )

            ,
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_copy),
                contentDescription = null,
                tint = FeatureCardColors.Orange,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Copy to Clipboard", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        /* ---------- DONE ---------- */
        Button(
            onClick = {
                // Clear decryption session
                UserSession.decryptImageUriString = null
                UserSession.decryptImagePassword = null
                onDone()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FeatureCardColors.Orange,   // 🔥 SOLID YELLOW
                contentColor = Color.White,
                disabledContainerColor = FeatureCardColors.Orange.copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = "Done",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}
