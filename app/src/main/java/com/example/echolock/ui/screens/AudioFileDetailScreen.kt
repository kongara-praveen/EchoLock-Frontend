package com.example.echolock.ui.screens

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.echolock.R
import com.example.echolock.model.FileItem
import com.example.echolock.ui.theme.AppColors
import java.net.URLEncoder

@Composable
fun AudioFileDetailScreen(
    file: FileItem,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    
    // Audio player state
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableIntStateOf(0) }
    var duration by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Construct audio URL - prioritize file_url from database
    val audioUrl = remember(file.name, file.file_url) {
        val url = if (!file.file_url.isNullOrBlank()) {
            // Use the URL from database
            file.file_url
        } else {
            // Construct URL from filename if database URL is not available
            val encodedFileName = URLEncoder.encode(file.name, "UTF-8")
            "http://10.167.221.28/echolock_api/audio/download_audio.php?audio_name=$encodedFileName"
        }
        Log.d("AudioFileDetail", "File: ${file.name}, file_url: ${file.file_url}, final URL: $url")
        url
    }
    
    // Initialize MediaPlayer
    LaunchedEffect(audioUrl) {
        try {
            isLoading = true
            Log.d("AudioFileDetail", "Initializing MediaPlayer with URL: $audioUrl")
            val player = MediaPlayer().apply {
                setDataSource(audioUrl)
                setOnPreparedListener {
                    duration = it.duration
                    isLoading = false
                    Log.d("AudioFileDetail", "Audio prepared. Duration: $duration ms")
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "Error: what=$what, extra=$extra, URL=$audioUrl")
                    isLoading = false
                    Toast.makeText(context, "Error loading audio", Toast.LENGTH_SHORT).show()
                    true
                }
                setOnCompletionListener {
                    isPlaying = false
                    currentPosition = 0
                    Log.d("AudioFileDetail", "Audio playback completed")
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Failed to initialize", e)
            Log.e("AudioPlayer", "URL was: $audioUrl")
            isLoading = false
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Update position while playing
    LaunchedEffect(isPlaying) {
        while (isPlaying && mediaPlayer != null) {
            kotlinx.coroutines.delay(100)
            mediaPlayer?.let {
                if (it.isPlaying) {
                    currentPosition = it.currentPosition
                }
            }
        }
    }
    
    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
    
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
                "Audio File",
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))

            // File Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon in circle
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                AppColors.PrimaryDark,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_music),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        file.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "${file.size} • Encrypted",
                        fontSize = 14.sp,
                        color = AppColors.TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Audio Player Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Duration",
                    fontSize = 14.sp,
                    color = AppColors.TextSecondary,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // Progress bar
                val progress = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = AppColors.PrimaryDark,
                    trackColor = AppColors.BorderLight
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        formatTime(currentPosition),
                        fontSize = 12.sp,
                        color = AppColors.TextSecondary
                    )
                    Text(
                        formatTime(duration),
                        fontSize = 12.sp,
                        color = AppColors.TextSecondary
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Play/Pause button
                FloatingActionButton(
                    onClick = {
                        mediaPlayer?.let { player ->
                            if (isPlaying) {
                                player.pause()
                                isPlaying = false
                            } else {
                                player.start()
                                isPlaying = true
                            }
                        }
                    },
                    modifier = Modifier.size(64.dp),
                    containerColor = AppColors.PrimaryDark
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = Color.White,
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

fun formatTime(milliseconds: Int): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}

