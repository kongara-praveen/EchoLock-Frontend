package com.example.echolock.ui.screens

// 🔥 Required Imports (DON’T MISS)
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
import com.example.echolock.R

@Composable
fun OnBoardingImageScreen(onContinue: () -> Unit, onSkip: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(135.dp))   // same as audio & tamper

        Box(
            modifier = Modifier
                .size(255.dp)
                .background(Color(0xFFEFFBFB), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = "image icon",
                modifier = Modifier.size(88.dp)
            )
        }

        Spacer(modifier = Modifier.height(55.dp))

        Text("Hide Messages in Images",
            fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFF0A2E45)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Embed secret data inside image pixels using LSB algorithms\n" +
                    "that remain invisible to the naked eye.",
            fontSize = 15.sp, color = Color(0xFF5F7076), textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(75.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(7.dp).background(Color(0xFFD0DBDF), CircleShape))
            Spacer(Modifier.width(8.dp))
            Box(Modifier.width(38.dp).height(6.dp).background(Color(0xFF005F73), RoundedCornerShape(50)))
            Spacer(Modifier.width(8.dp))
            repeat(2) { Box(Modifier.size(7.dp).background(Color(0xFFD0DBDF), CircleShape)); Spacer(Modifier.width(8.dp)) }
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
