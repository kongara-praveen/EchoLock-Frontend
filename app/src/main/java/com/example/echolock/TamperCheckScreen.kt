package com.example.echolock.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TamperCheckScreen(
    onBack: () -> Unit,
    onStartCheck: (Uri) -> Unit
) {
    val context = LocalContext.current

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }

    /* -------- FILE PICKER -------- */
    val picker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedUri = uri

                // 🔹 Get file name
                context.contentResolver.query(
                    uri,
                    arrayOf(OpenableColumns.DISPLAY_NAME),
                    null,
                    null,
                    null
                )?.use {
                    if (it.moveToFirst()) {
                        selectedFileName = it.getString(0)
                    }
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(22.dp)
    ) {

        /* ---------- HEADER ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Tamper Check",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Back",
                color = Color(0xFF005F73),
                modifier = Modifier.clickable { onBack() }
            )
        }

        Spacer(Modifier.height(30.dp))

        /* ---------- UPLOAD CARD ---------- */
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (selectedUri == null) {

                    Text(
                        text = "Upload Encrypted Image or Audio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { picker.launch("*/*") },
                        colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
                    ) {
                        Text("Choose File", color = Color.White)
                    }

                } else {

                    Text(
                        text = "File Selected",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = selectedFileName ?: "Selected file",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            selectedUri = null
                            selectedFileName = null
                            picker.launch("*/*")
                        }
                    ) {
                        Text("Reselect File")
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        /* ---------- CHECK BUTTON ---------- */
        Button(
            onClick = {
                selectedUri?.let { onStartCheck(it) }
            },
            enabled = selectedUri != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF006D77))
        ) {
            Text("Check Tamper", color = Color.White)
        }
    }
}
