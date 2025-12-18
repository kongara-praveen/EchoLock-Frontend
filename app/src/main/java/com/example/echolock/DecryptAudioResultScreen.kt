package com.example.echolock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R

@Composable
fun DecryptAudioResultScreen(
    onDone: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {

        // HEADER
        Text(
            text = "<  Message Extracted",
            fontSize = 20.sp,
            color = Color(0xFF062A2F),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onDone() } // Back
        )

        Spacer(modifier = Modifier.height(26.dp))

        // SUCCESS ROW
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color(0xFFE8FFF2), RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_lock), // Use your lock-success icon
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    "Decryption Successful",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF062A2F)
                )
                Text(
                    "Source: secret_mission.mp3",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // MESSAGE BOX
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFA)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    "CONFIDENTIAL BRIEFING:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF062A2F)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    "Target location confirmed at coordinates\n" +
                            "34.0522° N, 118.2437° W. Surveillance\n" +
                            "team in position. Awaiting further\n" +
                            "instructions.\n\n" +
                            "Do not acknowledge receipt of this\n" +
                            "message via open channels.",
                    fontSize = 14.sp,
                    color = Color(0xFF062A2F),
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // COPY BUTTON
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8FFFB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_copy), // your clipboard icon
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Copy to Clipboard",
                color = Color(0xFF005F73),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // DONE BUTTON
        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done", color = Color.White, fontSize = 16.sp)
        }
    }
}
