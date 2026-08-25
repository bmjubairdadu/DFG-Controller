package com.dfgcontroller.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dfgcontroller.R
import com.dfgcontroller.ui.theme.DarkBackground
import com.dfgcontroller.ui.theme.ElectricCyan
import com.dfgcontroller.ui.theme.OrbitronFamily

@Composable
fun RootAccessScreen(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Shield,
                contentDescription = null,
                tint = ElectricCyan,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "ROOT ACCESS REQUIRED",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontFamily = OrbitronFamily
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "This application requires superuser permissions to manage kernel parameters. Please grant access to continue.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("GRANT PERMISSION", color = DarkBackground, fontFamily = OrbitronFamily)
            }
        }
    }
}
