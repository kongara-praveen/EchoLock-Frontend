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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.echolock.R

@Composable
fun DecryptImageScreen(
    onBack: () -> Unit,
    onContinue: (String) -> Unit   // ✅ pass image URI
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    /* ---------- IMAGE PICKER ---------- */
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri
        }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        /* ---------- HEADER ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
            Text(
                "Decrypt Image",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        /* ---------- IMAGE PICK / PREVIEW ---------- */
        if (selectedImageUri == null) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFEFFAF7), RoundedCornerShape(16.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painterResource(R.drawable.ic_upload),
                        contentDescription = null,
                        tint = Color(0xFF005F73),
                        modifier = Modifier.size(42.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Select Encrypted Image",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF005F73)
                    )
                    Text(
                        "Tap to extract hidden message",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

        } else {

            Text(
                "Selected Image",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(12.dp))

            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Tap image to reselect",
                color = Color(0xFF005F73),
                modifier = Modifier.clickable {
                    imagePickerLauncher.launch("image/*")
                }
            )
        }

        Spacer(Modifier.weight(1f))

        /* ---------- CONTINUE ---------- */
        Button(
            onClick = {
                onContinue(selectedImageUri.toString())
            },
            enabled = selectedImageUri != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF005F73)
            )
        ) {
            Text("Extract Message", color = Color.White)
        }
    }
}
