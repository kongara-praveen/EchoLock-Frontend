package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.echolock.R
import com.example.echolock.model.FileItem
import com.example.echolock.ui.common.BottomNavBar
import com.example.echolock.viewmodel.FilesViewModel

@Composable
fun FilesScreen(
    onBack: () -> Unit,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: FilesViewModel = viewModel()
) {

    var selectedTab by remember { mutableStateOf(1) }

    val files by viewModel.files
    val loading by viewModel.loading

    // 🔹 Load data from backend
    LaunchedEffect(Unit) {
        viewModel.loadFiles(userId = 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {

        /* TOP BAR */
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                Text("No files found", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(files) { file ->
                    FileItemCard(file)
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
}

@Composable
fun FileItemCard(file: FileItem) {

    val icon =
        if (file.type == "audio") R.drawable.ic_music
        else R.drawable.ic_image

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color(0xFF006D77),
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
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
