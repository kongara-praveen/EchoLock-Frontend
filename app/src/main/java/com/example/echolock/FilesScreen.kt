package com.example.echolock.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.echolock.R
import com.example.echolock.model.FileItem
import com.example.echolock.session.UserSession
import com.example.echolock.ui.common.BottomNavBar
import com.example.echolock.ui.theme.AppColors
import com.example.echolock.viewmodel.FilesViewModel

@Composable
fun FilesScreen(
    onBack: () -> Unit,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onFileClick: (FileItem) -> Unit = {},
    viewModel: FilesViewModel = viewModel()
) {

    var selectedTab by remember { mutableStateOf(1) }
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf<FileItem?>(null) }

    val files by viewModel.files
    val loading by viewModel.loading
    val error by viewModel.error

    val userId = remember { UserSession.userId.toIntOrNull() ?: 1 }

    // 🔹 Load data from backend
    LaunchedEffect(Unit) {
        Log.d("FilesScreen", "UserSession.userId: ${UserSession.userId}, parsed userId: $userId")
        viewModel.loadFiles(userId = userId)
    }

    // Show error if any
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {

        /* TOP BAR */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onBack() }
                )

                Spacer(Modifier.width(10.dp))
                Text("Files", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            // Refresh button
            IconButton(
                onClick = {
                    viewModel.refreshFiles(userId)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = AppColors.PrimaryDark
                )
            }
        }

        /* CONTENT */
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("No files found", color = AppColors.TextSecondary, fontSize = 16.sp)
                    if (error != null) {
                        Text(
                            error ?: "",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.refreshFiles(userId)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.PrimaryDark
                        )
                    ) {
                        Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Refresh")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(files, key = { it.file_id ?: it.name }) { file ->
                    FileItemCard(
                        file = file,
                        onFileClick = { onFileClick(file) },
                        onDelete = {
                            showDeleteDialog = file
                        },
                        onShare = { viewModel.shareFile(context, file) },
                        onView = { onFileClick(file) }
                    )
                }
            }
        }

        /* BOTTOM NAV */
        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = {
                selectedTab = it
                when (it) {
                    0 -> onHomeClick()
                    1 -> {} // already here
                    2 -> onHistoryClick()
                    3 -> onSettingsClick()
                }
            }
        )
    }

    // Delete Confirmation Dialog
    showDeleteDialog?.let { file ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = {
                Text(
                    "Delete File",
                    color = AppColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${file.name}\"? This action cannot be undone.",
                    color = AppColors.TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteFile(
                            file = file,
                            userId = userId,
                            onSuccess = {
                                Toast.makeText(context, "File deleted successfully", Toast.LENGTH_SHORT).show()
                                showDeleteDialog = null
                            },
                            onError = { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                                showDeleteDialog = null
                            }
                        )
                    }
                ) {
                    Text("Delete", color = AppColors.Error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel", color = AppColors.TextSecondary)
                }
            },
            containerColor = AppColors.Surface
        )
    }
}

@Composable
fun FileItemCard(
    file: FileItem,
    onFileClick: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onView: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val icon =
        if (file.type == "audio") R.drawable.ic_music
        else R.drawable.ic_image

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onFileClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = AppColors.PrimaryDark,
                modifier = Modifier.size(36.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    file.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${file.size} • ${file.date}",
                    fontSize = 13.sp,
                    color = AppColors.TextSecondary
                )
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = AppColors.TextSecondary
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("View") },
                        onClick = {
                            expanded = false
                            onView()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Visibility, null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Share") },
                        onClick = {
                            expanded = false
                            onShare()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Share, null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = AppColors.Error) },
                        onClick = {
                            expanded = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, null, tint = Color.Red)
                        }
                    )
                }
            }
        }
    }
}