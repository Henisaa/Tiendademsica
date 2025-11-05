package com.example.tiendademsica.archivos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    // Replace with your logo in res/drawable (e.g., ic_logo.png)
    logoResId: Int
) {
    // Infinite rotation 0 → 360° every 1.2s
    val infinite = rememberInfiniteTransition(label = "splash-rotation")
    val rotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing)
        ),
        label = "rotation"
    )

    // Keep splash for ~1.5s, then continue
    LaunchedEffect(Unit) {
        delay(1500)
        onFinished()
    }

    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.White
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = logoResId),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(128.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
        }
    }
}
