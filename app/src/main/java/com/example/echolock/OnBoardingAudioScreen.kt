package com.example.echolock.ui.screens

// 🔥 REQUIRED IMPORTS
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.echolock.R   // 🔥 this is important

@Composable
fun OnBoardingAudioScreen(onContinue: () -> Unit, onSkip: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(135.dp))   // perfectly aligned like tamper screen

        Box(
            modifier = Modifier
                .size(255.dp)
                .background(Color(0xFFEFFBFB), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_mic),
                contentDescription = "mic icon",
                modifier = Modifier.size(88.dp)
            )
        }

        Spacer(modifier = Modifier.height(55.dp))

        Text("Hide Messages in Audio",
            fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFF0A2E45)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Securely hide messages within any audio file format",
            fontSize = 15.sp, color = Color(0xFF5F7076), textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(75.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            // ● Active dot (first)
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color(0xFF005F73), CircleShape)
            )

            Spacer(Modifier.width(10.dp))

            // ○ Inactive dots (remaining 2)
            repeat(2) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(Color(0xFFE1E6E8), CircleShape)
                )
                Spacer(Modifier.width(10.dp))
            }
        }


        Spacer(modifier = Modifier.height(58.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF005F73)),
            shape = RoundedCornerShape(12.dp)
        ) { Text("Continue", color = Color.White, fontSize = 17.sp) }

        Spacer(modifier = Modifier.height(18.dp))

        Text("Skip",
            color = Color(0xFF7D8C92),
            fontSize = 14.sp,
            modifier = Modifier.clickable { onSkip() }
        )
    }
}
