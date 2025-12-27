package com.example.echolock.ui.screens

import android.net.Uri
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.util.TamperCheckUtil
import com.example.echolock.util.uriToFile
import kotlinx.coroutines.delay

@Composable
fun TamperCheckProgressScreen(
    fileUri: Uri,
    onResult: (Boolean, String) -> Unit
) {
    val context = LocalContext.current

    var progress by remember { mutableFloatStateOf(0f) }
    var percent by remember { mutableIntStateOf(0) }

    var result by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var navigated by remember { mutableStateOf(false) }

    /* ================== WORK ================== */
    LaunchedEffect(fileUri) {

        val file = uriToFile(context, fileUri)
        val isSafe = TamperCheckUtil.checkFile(file)

        for (i in 1..100) {
            delay(18)
            percent = i
            progress = i / 100f
        }

        result = isSafe to file.name
    }

    /* ================== SAFE NAVIGATION ================== */
    LaunchedEffect(result) {
        if (result != null && !navigated) {
            navigated = true
            onResult(result!!.first, result!!.second)
        }
    }

    /* ================== UI ================== */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator(
            progress = progress,
            color = Color(0xFF005F73),
            strokeWidth = 6.dp,
            modifier = Modifier.size(120.dp)
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "$percent%",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Checking file integrity...\nPlease wait.",
            textAlign = TextAlign.Center,
            color = Color(0xFF6B7E80)
        )
    }
}
