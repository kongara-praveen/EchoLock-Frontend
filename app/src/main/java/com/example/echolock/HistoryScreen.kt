package com.example.echolock.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.echolock.model.HistoryItem
import com.example.echolock.ui.common.BottomNavBar
import com.example.echolock.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onHomeClick: () -> Unit,
    onFilesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {

    val today by viewModel.today
    val yesterday by viewModel.yesterday
    val loading by viewModel.loading

    var selectedTab by remember { mutableStateOf(2) }

    // Load history from backend
    LaunchedEffect(Unit) {
        viewModel.loadHistory(userId = 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {

        /* ---------------- CONTENT AREA ---------------- */
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(Modifier.height(20.dp))

            /* ---------------- TOP BAR ---------------- */
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBack() }
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "History",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.height(18.dp))

            /* ---------------- LIST ---------------- */
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    if (today.isNotEmpty()) {
                        item { SectionTitle("Today") }
                        items(today) { item ->
                            HistoryCard(item)
                        }
                    }

                    if (yesterday.isNotEmpty()) {
                        item { SectionTitle("Yesterday") }
                        items(yesterday) { item ->
                            HistoryCard(item)
                        }
                    }
                }
            }
        }

        /* ---------------- BOTTOM NAV ---------------- */
        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = {
                selectedTab = it
                when (it) {
                    0 -> onHomeClick()
                    1 -> onFilesClick()
                    3 -> onSettingsClick()
                }
            }
        )
    }
}

/* ---------------- SECTION TITLE ---------------- */
@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF6B7E80),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/* ---------------- HISTORY CARD ---------------- */
@Composable
fun HistoryCard(item: HistoryItem) {

    val icon = when (item.action) {
        "Encrypted" -> Icons.Filled.Lock
        "Decrypted" -> Icons.Filled.LockOpen
        "Tamper Check" -> Icons.Filled.Warning
        else -> Icons.Filled.Info
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF006D77)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.fileName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${item.action} • ${item.time}",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Gray
            )
        }
    }
}
