package com.example.armusic.ui.screens.animation
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
@Preview
@Composable


fun AudioVisualizer() {
    val barCount = 60  // Número de barras verticales

    val animatedHeights = List(barCount) {
        remember { Animatable(50f) } // Altura inicial de cada barra
    }

    // Animación infinita que cambia la altura de las barras
    LaunchedEffect(Unit) {
        while (true) {
            animatedHeights.forEach { animatable ->
                animatable.animateTo(
                    targetValue = (20..100).random().toFloat(),
                    animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                )
            }
        }
    }

    Canvas(
        modifier = Modifier
            .width(200.dp)
            .height(50.dp)
    ) {
        val barWidth = size.width / barCount  // Espacio por barra

        animatedHeights.forEachIndexed { index, animatable ->
            drawRect(
                color = Color.Green,
                topLeft = androidx.compose.ui.geometry.Offset(
                    x = index * barWidth,
                    y = size.height - animatable.value
                ),
                size = androidx.compose.ui.geometry.Size(
                    width = barWidth * 0.6f, // Grosor de cada barra
                    height = animatable.value
                )
            )
        }
    }
}
