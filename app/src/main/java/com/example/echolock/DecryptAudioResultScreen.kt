package com.example.echolock.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolock.R
import com.example.echolock.session.UserSession

@Composable
fun DecryptAudioResultScreen(
    onDone: () -> Unit
) {
    val context = LocalContext.current

    val decryptedMessage =
        UserSession.decryptedMessage ?: "No hidden message found"

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
            modifier = Modifier.clickable { onDone() }
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
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = "Decryption Result",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF062A2F)
            )
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
                    "EXTRACTED MESSAGE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF062A2F)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    decryptedMessage,
                    fontSize = 14.sp,
                    color = Color(0xFF062A2F)
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // COPY BUTTON
        Button(
            onClick = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE)
                            as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        "Decrypted Message",
                        decryptedMessage
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8FFFB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_copy),
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
