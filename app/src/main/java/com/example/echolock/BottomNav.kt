package com.example.echolock.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R

@Composable
fun BottomNavBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BottomNavItem(0, selectedIndex, "Home", R.drawable.ic_home_filled, onTabSelected)
        BottomNavItem(1, selectedIndex, "Files", R.drawable.ic_folder, onTabSelected)
        BottomNavItem(2, selectedIndex, "History", R.drawable.ic_history, onTabSelected)
        BottomNavItem(3, selectedIndex, "Settings", R.drawable.ic_settings, onTabSelected)
    }
}

@Composable
fun BottomNavItem(
    index: Int,
    selectedIndex: Int,
    label: String,
    iconRes: Int,
    onTabSelected: (Int) -> Unit
) {

    val isSelected = index == selectedIndex
    val color = if (isSelected) Color(0xFF005F73) else Color(0xFFA3AFB4)
    val weight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onTabSelected(index) }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(26.dp),
            colorFilter = ColorFilter.tint(color)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = weight,
            color = color
        )
    }
}
