package com.example.armusic.ui.screens.customBackgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

@Composable
fun GlassmorphismCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .padding(horizontal = 7.dp)
            .height(150.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))

    ) {
        // Capa con fondo glass
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(13.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.03f),
                            Color.White.copy(alpha = 0.3f)
                        )
                    )
                )
                .blur(16.dp)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(5.dp) // Borde reflejo
                )
        )

        // Contenido
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(10.dp)
        ) {
            content()
        }
    }
}
@Preview
@Composable
fun GlassmorphismCard() {
    Box(
        modifier = Modifier
            .padding(horizontal = 7.dp)
            .height(150.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))

    ) {
        // Capa con fondo glass
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(13.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.03f),
                            Color.White.copy(alpha = 0.3f)
                        )
                    )
                )
                .blur(16.dp)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(5.dp) // Borde reflejo
                )
        )

        // Contenido
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(10.dp)
        ) {

        }
    }
}