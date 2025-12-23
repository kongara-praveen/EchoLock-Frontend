package com.example.echolock.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.session.UserSession

@Composable
fun DecryptAudioScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit
) {

    var selectedAudioUri by remember { mutableStateOf<Uri?>(null) }

    /* ---------- File Picker ---------- */
    val audioPickerLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let {
                selectedAudioUri = it
                UserSession.decryptAudioUri = it.toString()
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        /* ---------- HEADER ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "<",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Decrypt Audio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        /* ---------- AUDIO SELECTED / UPLOAD ---------- */
        if (selectedAudioUri == null) {

            // 🔼 Upload UI
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(Color(0xFFE9F7F7), RoundedCornerShape(14.dp))
                    .clickable {
                        audioPickerLauncher.launch(arrayOf("audio/*"))
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = null,
                        modifier = Modifier.size(44.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "Select Encrypted Audio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF005F73)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "Upload audio to extract message",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7E80)
                    )
                }
            }

        } else {

            // ✅ Selected File UI
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F9F9), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_music),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Encrypted Audio Selected",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = selectedAudioUri?.lastPathSegment
                                ?: "audio file",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Reselect Audio",
                    color = Color(0xFF005F73),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        selectedAudioUri = null
                        UserSession.decryptAudioUri = null
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        /* ---------- CONTINUE ---------- */
        Button(
            onClick = onContinue,
            enabled = selectedAudioUri != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Continue",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
