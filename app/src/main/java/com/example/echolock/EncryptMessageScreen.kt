package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
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

@Composable
fun EncryptMessageScreen(
    onBack: () -> Unit,
    onEncrypt: () -> Unit
) {

    var message by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "",
                modifier = Modifier.size(26.dp).clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("Secret Message", fontSize = 21.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(22.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFE4FAF0), RoundedCornerShape(10.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(id = R.drawable.ic_lock), "", Modifier.size(24.dp))
            Spacer(Modifier.width(10.dp))
            Text("Your message will be encrypted safely.")
        }

        Spacer(Modifier.height(22.dp))

        Text("Enter Your Message", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth().height(180.dp),
            placeholder = { Text("Type your secret message here...") }
        )

        Spacer(Modifier.height(10.dp))
        Text("AES-256 Encryption", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.align(Alignment.End))

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onEncrypt,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73))
        ) {
            Text("Encrypt & Hide", color = Color.White, fontSize = 16.sp)
        }
    }
}
