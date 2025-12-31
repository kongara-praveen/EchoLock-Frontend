package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.echolock.R
import com.example.echolock.model.FileItem
import com.example.echolock.ui.theme.AppColors
import java.net.URLEncoder

@Composable
fun ImageFileDetailScreen(
    file: FileItem,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() },
                tint = AppColors.TextPrimary
            )
            Spacer(Modifier.width(10.dp))
            Text(
                "Image File",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            // Image Preview - Use file_url from database if available, otherwise construct from file name
            val imageUrl = remember(file.name, file.file_url) {
                val url = if (!file.file_url.isNullOrBlank()) {
                    // Use the URL provided by the database
                    file.file_url
                } else {
                    // Construct URL using view endpoint for proper image display
                    val encodedFileName = URLEncoder.encode(file.name, "UTF-8")
                    "http://10.167.221.28/echolock_api/image/view_image.php?image_name=$encodedFileName"
                }
                android.util.Log.d("ImageFileDetail", "File: ${file.name}, file_url: ${file.file_url}, final URL: $url")
                url
            }
            
            // Full screen image display
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Image: ${file.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    error = painterResource(id = R.drawable.ic_image),
                    placeholder = painterResource(id = R.drawable.ic_image),
                    onError = {
                        android.util.Log.e("ImageFileDetail", "Failed to load image: $imageUrl")
                        android.util.Log.e("ImageFileDetail", "File details - name: ${file.name}, file_url: ${file.file_url}")
                    },
                    onSuccess = {
                        android.util.Log.d("ImageFileDetail", "Image loaded successfully: $imageUrl")
                    }
                )
            }
        }
    }
}


