package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun VerificationScreen(
    onBack: () -> Unit,
    onVerify: (String) -> Unit,
    onResend: () -> Unit
) {

    var otp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(25.dp))

        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .size(26.dp)
                .clickable { onBack() }
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Verification",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF082B34)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Enter the 6-digit code sent to your email.",
            fontSize = 15.sp,
            color = Color(0xFF6B7E80)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 🔢 HIDDEN INPUT (controls OTP state)
        OutlinedTextField(
            value = otp,
            onValueChange = {
                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                    otp = it
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp), // hidden
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Transparent
            )
        )

        // 🔲 OTP BOXES
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(6) { index ->
                OtpBox(
                    value = otp.getOrNull(index)?.toString() ?: ""
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { onVerify(otp) },
            enabled = otp.length == 6,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            Text("Verify Code", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Didn't receive code?", color = Color(0xFF6B7E80))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "Resend",
                color = Color(0xFF005F73),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onResend() }
            )
        }
    }
}

@Composable
private fun OtpBox(value: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .border(
                width = 1.5.dp,
                color = Color(0xFF005F73),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
