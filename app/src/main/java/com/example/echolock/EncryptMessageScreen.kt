package com.example.echolock.ui.screens

import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.session.UserSession

@Composable
fun EncryptMessageScreen(
    onBack: () -> Unit,
    onEncrypt: () -> Unit     // 🔥 no parameter needed now
) {

    var message by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        /* ---------- HEADER ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Secret Message",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(22.dp))

        /* ---------- INFO BOX ---------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE4FAF0), RoundedCornerShape(10.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text("Your message will be encrypted safely.")
        }

        Spacer(Modifier.height(22.dp))

        Text("Enter Your Message", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))

        /* ---------- INPUT ---------- */
        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
                error = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            placeholder = {
                Text(
                    "Type your secret message here...",
                    color = Color(0xFF8A9AA0)
                )
            },
            isError = error != null,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color(0xFF0A2E45) // 🔥 BRIGHT TEXT
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF0A2E45),
                unfocusedTextColor = Color(0xFF0A2E45),
                focusedBorderColor = Color(0xFF005F73),
                unfocusedBorderColor = Color(0xFFD0DBDF),
                cursorColor = Color(0xFF005F73)
            )
        )


        if (error != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = error!!,
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "AES-256 Encryption",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(Modifier.weight(1f))

        /* ---------- ACTION ---------- */
        Button(
            onClick = {
                val cleanMessage = message.trim()

                if (cleanMessage.isEmpty()) {
                    error = "Message cannot be empty"
                } else {
                    // ✅ STORE MESSAGE FOR NEXT SCREEN
                    UserSession.secretMessage = cleanMessage

                    // ✅ MOVE TO EncryptionProgressScreen
                    onEncrypt()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF005F73)
            )
        ) {
            Text(
                text = "Encrypt & Hide",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
