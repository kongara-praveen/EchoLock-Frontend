package com.example.echolock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.echolock.navigation.AppTheme
import com.example.echolock.navigation.currentAppTheme

@Composable
fun AppThemeScreen(onBack: () -> Unit) {

    val selectedTheme = currentAppTheme.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {

        // 🔙 Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable { onBack() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "App Theme",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.height(30.dp))

        ThemeItem(
            title = "Light Mode",
            selected = selectedTheme == AppTheme.LIGHT
        ) {
            currentAppTheme.value = AppTheme.LIGHT
        }

        ThemeItem(
            title = "Dark Mode",
            selected = selectedTheme == AppTheme.DARK
        ) {
            currentAppTheme.value = AppTheme.DARK
        }

        ThemeItem(
            title = "System Default",
            selected = selectedTheme == AppTheme.SYSTEM
        ) {
            currentAppTheme.value = AppTheme.SYSTEM
        }
    }
}

@Composable
fun ThemeItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                else
                    Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(Modifier.height(10.dp))
}
