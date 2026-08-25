package com.dfgcontroller.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
    var startAnimation by remember { mutableStateOf(false) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

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
            Box(contentAlignment = Alignment.Center) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(ElectricCyan.copy(alpha = alpha * 0.2f))
                )
                
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn() + expandVertically()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "DFG CONTROLLER",
                        style = MaterialTheme.typography.headlineLarge,
                        color = ElectricCyan,
                        fontFamily = OrbitronFamily,
                        letterSpacing = 4.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "PREMIUM EDITION",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontFamily = OrbitronFamily,
                        letterSpacing = 2.sp
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = "INITIALIZING SYSTEM...",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontFamily = OrbitronFamily,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Please grant Superuser permission to access kernel tunables.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "GRANT PERMISSION",
                    color = DarkBackground,
                    fontWeight = FontWeight.Bold,
                    fontFamily = OrbitronFamily
                )
            }
        }
    }
}
