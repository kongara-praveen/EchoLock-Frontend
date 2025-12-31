package com.example.echolock.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.util.StorageFile
import com.example.echolock.util.StorageInfo
import com.example.echolock.util.StorageUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FileStorageScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var files by remember { mutableStateOf<List<StorageFile>>(emptyList()) }
    var storageInfo by remember { mutableStateOf<StorageInfo?>(null) }
    var loading by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf<StorageFile?>(null) }

    LaunchedEffect(Unit) {
        loading = true
        files = StorageUtil.getEchoLockFiles(context)
        storageInfo = StorageUtil.getStorageInfo(context)
        loading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF4FBFF), Color(0xFFE6F4FA))
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
                Text("File Storage", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                
                Spacer(Modifier.weight(1f))
                
                IconButton(onClick = {
                    scope.launch {
                        loading = true
                        files = StorageUtil.getEchoLockFiles(context)
                        storageInfo = StorageUtil.getStorageInfo(context)
                        loading = false
                        Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(Icons.Default.Refresh, null)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Storage Info Card
            storageInfo?.let { info ->
                StorageInfoCard(info)
                Spacer(Modifier.height(20.dp))
            }

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (files.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.FolderOpen,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF9E9E9E)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "No files found",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Encrypted files will appear here",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(files, key = { it.uri.toString() }) { file ->
                        FileItemCard(
                            file = file,
                            onShare = {
                                shareFile(context, file)
                            },
                            onDelete = {
                                showDeleteDialog = file
                            },
                            onView = {
                                viewFile(context, file)
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    showDeleteDialog?.let { file ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete File") },
            text = { Text("Are you sure you want to delete ${file.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            val success = StorageUtil.deleteFile(context, file)
                            if (success) {
                                files = StorageUtil.getEchoLockFiles(context)
                                storageInfo = StorageUtil.getStorageInfo(context)
                                Toast.makeText(context, "File deleted", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to delete file", Toast.LENGTH_SHORT).show()
                            }
                            showDeleteDialog = null
                        }
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StorageInfoCard(info: StorageInfo) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Storage Overview",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A2E45)
            )
            
            Spacer(Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    "Total Files",
                    "${info.totalFiles}",
                    Icons.Default.Folder,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    "Total Size",
                    formatFileSize(info.totalSize),
                    Icons.Default.Storage,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    "Audio Files",
                    "${info.audioFiles}",
                    Icons.Default.MusicNote,
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    "Image Files",
                    "${info.imageFiles}",
                    Icons.Default.Image,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun InfoItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = Color(0xFF005F73), modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(6.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun FileItemCard(
    file: StorageFile,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit
) {
    val icon = when (file.type) {
        com.example.echolock.util.FileType.AUDIO -> Icons.Default.MusicNote
        com.example.echolock.util.FileType.IMAGE -> Icons.Default.Image
        else -> Icons.Default.InsertDriveFile
    }
    
    val iconColor = when (file.type) {
        com.example.echolock.util.FileType.AUDIO -> Color(0xFF4A9FB8)
        com.example.echolock.util.FileType.IMAGE -> Color(0xFF6B9BD1)
        else -> Color(0xFF9E9E9E)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    file.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF0A2E45)
                )
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(
                        formatFileSize(file.size),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(" • ", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        formatDate(file.dateModified),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Action buttons
            IconButton(onClick = onView) {
                Icon(Icons.Default.Visibility, null, tint = Color(0xFF005F73))
            }
            IconButton(onClick = onShare) {
                Icon(Icons.Default.Share, null, tint = Color(0xFF005F73))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color(0xFFD32F2F))
            }
        }
    }
}

fun formatFileSize(bytes: Long): String {
    return when {
        bytes >= 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        bytes >= 1024 -> "${bytes / 1024} KB"
        else -> "$bytes B"
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun shareFile(context: android.content.Context, file: StorageFile) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = when (file.type) {
                com.example.echolock.util.FileType.AUDIO -> "audio/*"
                com.example.echolock.util.FileType.IMAGE -> "image/*"
                else -> "*/*"
            }
            putExtra(Intent.EXTRA_STREAM, file.uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share file"))
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to share file", Toast.LENGTH_SHORT).show()
    }
}

fun viewFile(context: android.content.Context, file: StorageFile) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(file.uri, when (file.type) {
                com.example.echolock.util.FileType.AUDIO -> "audio/*"
                com.example.echolock.util.FileType.IMAGE -> "image/*"
                else -> "*/*"
            })
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No app available to view this file", Toast.LENGTH_SHORT).show()
    }
}
