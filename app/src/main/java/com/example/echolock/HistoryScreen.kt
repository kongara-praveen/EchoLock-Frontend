package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.ui.common.BottomNavBar

data class HistoryItem(
    val fileName: String,
    val action: String,   // "Encrypted", "Decrypted", "Tamper Check"
    val time: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onHomeClick: () -> Unit,
    onFilesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    val today = listOf(
        HistoryItem("secret_mission.mp3", "Encrypted", "10:23 AM", Icons.Filled.Lock),
        HistoryItem("classified_doc.png", "Decrypted", "9:45 AM", Icons.Filled.LockOpen),
        HistoryItem("evidence_photo.jpg", "Tamper Check", "8:30 AM", Icons.Filled.Warning)
    )

    val yesterday = listOf(
        HistoryItem("intel_brief.wav", "Encrypted", "4:15 PM", Icons.Filled.Lock),
        HistoryItem("witness_audio.mp3", "Decrypted", "2:00 PM", Icons.Filled.LockOpen)
    )

    var selectedTab by remember { mutableStateOf(2) }   // History tab selected

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {

        /* ------------------- CONTENT AREA ------------------- */
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)   // ✅ padding for content only
        ) {

            Spacer(Modifier.height(20.dp))

            /* ------------------- TOP BAR ------------------- */
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
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
                    contentDescription = "Filter",
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.height(18.dp))

            /* ------------------- LIST ------------------- */
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                item { SectionTitle("Today") }
                items(today) { item ->
                    HistoryCard(item)
                }

                item { SectionTitle("Yesterday") }
                items(yesterday) { item ->
                    HistoryCard(item)
                }
            }
        }

        /* ------------------- BOTTOM NAV (Aligned Perfectly) ------------------- */
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

@Composable
fun HistoryCard(item: HistoryItem) {

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
                imageVector = item.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF006D77)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.fileName, fontSize = 15.sp, fontWeight = FontWeight.Bold)
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
