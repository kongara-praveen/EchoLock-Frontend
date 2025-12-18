package com.example.echolock.ui.screens

// REQUIRED IMPORTS
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R

@Composable
fun PasswordResetSuccessScreen(onContinue: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier.size(180.dp).background(Color(0xFFE8FFF1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(R.drawable.ic_success), contentDescription = "Success", modifier = Modifier.size(90.dp))
        }

        Spacer(Modifier.height(35.dp))

        Text("Password Reset Successfully", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF082B34), textAlign = TextAlign.Center)

        Spacer(Modifier.height(14.dp))

        Text("Your new password is now active.\nYou can log in securely.", textAlign = TextAlign.Center, color = Color(0xFF6B7E80))

        Spacer(Modifier.height(60.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) { Text("Continue to Login", color = Color.White, fontSize = 16.sp) }
    }
}
