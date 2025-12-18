package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.ui.common.BottomNavBar

@Composable
fun DashboardScreen(
    onEncryptAudio: () -> Unit,
    onEncryptImage: () -> Unit,
    onTamperCheck: () -> Unit,
    onDecryptAudio: () -> Unit,
    onDecryptImage: () -> Unit,

    onFilesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    var selectedTab by remember { mutableStateOf(0) }   // Home selected

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(22.dp)
        ) {

            Text(
                text = "Welcome back",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A2E45)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "What would you like to do today?",
                color = Color(0xFF6D7F85),
                fontSize = 15.sp
            )

            Spacer(Modifier.height(30.dp))

            DashboardTile(
                icon = R.drawable.ic_mic,
                title = "Hide in Audio",
                subtitle = "Embed secret messages in audio files",
                bg = Color(0xFFDDF7FF),
                onClick = onEncryptAudio
            )

            Spacer(Modifier.height(18.dp))

            DashboardTile(
                icon = R.drawable.ic_image,
                title = "Hide in Image",
                subtitle = "Conceal data within image files",
                bg = Color(0xFFEDE4FF),
                onClick = onEncryptImage
            )

            Spacer(Modifier.height(18.dp))

            DashboardTile(
                icon = R.drawable.ic_tamper,
                title = "Tamper Check",
                subtitle = "Verify file integrity",
                bg = Color(0xFFE5FFF1),
                onClick = onTamperCheck
            )

            Spacer(Modifier.height(30.dp))

            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A2E45)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                QuickBox(
                    text = "Decrypt Audio",
                    icon = R.drawable.ic_mic,
                    onClick = onDecryptAudio
                )

                QuickBox(
                    text = "Decrypt Image",
                    icon = R.drawable.ic_image,
                    onClick = onDecryptImage
                )
            }
        }

        // Bottom Navigation Bar
        BottomNavBar(
            selectedIndex = selectedTab,
            onTabSelected = { tab ->
                selectedTab = tab
                when (tab) {
                    1 -> onFilesClick()
                    2 -> onHistoryClick()
                    3 -> onSettingsClick()
                }
            }
        )
    }
}

/*───────────────────────────────────────────────────────────────*/
/* Dashboard Tiles */
/*───────────────────────────────────────────────────────────────*/

@Composable
fun DashboardTile(icon: Int, title: String, subtitle: String, bg: Color, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(bg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF062A2F)
                )

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7E80)
                )
            }

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/*───────────────────────────────────────────────────────────────*/
/* Quick Action Box */
/*───────────────────────────────────────────────────────────────*/

@Composable
fun QuickBox(text: String, icon: Int, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .width(160.dp)
            .background(Color(0xFFF5F9FA), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFFE2ECEC), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
