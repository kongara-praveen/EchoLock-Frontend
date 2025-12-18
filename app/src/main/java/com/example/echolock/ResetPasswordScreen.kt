package com.example.echolock.ui.screens

// REQUIRED IMPORTS
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    onBack: () -> Unit,
    onResetDone: () -> Unit
) {

    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    // 👁️ PASSWORD VISIBILITY TOGGLES
    var newPassVisible by remember { mutableStateOf(false) }
    var confirmPassVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(Modifier.height(25.dp))

        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(26.dp)
                .clickable { onBack() }
        )

        Spacer(Modifier.height(25.dp))

        Text(
            "Reset Password",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF082B34)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "Create new strong password for your account.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80)
        )

        Spacer(Modifier.height(28.dp))

        // ================= NEW PASSWORD ================= //
        OutlinedTextField(
            value = newPass,
            onValueChange = { newPass = it },
            label = { Text("New Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation =
                if (newPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = if (newPassVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = Color(0xFF005F73),
                    modifier = Modifier.clickable { newPassVisible = !newPassVisible }
                )
            }
        )

        Spacer(Modifier.height(14.dp))

        // ================= CONFIRM PASSWORD ================= //
        OutlinedTextField(
            value = confirmPass,
            onValueChange = { confirmPass = it },
            label = { Text("Confirm New Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation =
                if (confirmPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    imageVector = if (confirmPassVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = Color(0xFF005F73),
                    modifier = Modifier.clickable { confirmPassVisible = !confirmPassVisible }
                )
            }
        )

        Spacer(Modifier.height(30.dp))

        // ================= RESET BUTTON ================= //
        Button(
            onClick = onResetDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            Text("Reset Password", color = Color.White, fontSize = 16.sp)
        }
    }
}
