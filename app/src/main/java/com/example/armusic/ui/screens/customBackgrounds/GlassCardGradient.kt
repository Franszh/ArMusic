package com.example.armusic.ui.screens.customBackgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun GradientGlassCard(modifier: Modifier,colorBackground: Color? = null, content:@Composable ()->Unit) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))

    ) {
        // Capa con fondo glass
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    Brush.linearGradient(
                        colors =
                        if(colorBackground != null ){
                            listOf(colorBackground.copy(alpha = 0.8f), colorBackground.copy(alpha = 0.0f))
                        }else{
                            listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.0f)
                            )
                        }

                    )
                )
                .blur(16.dp)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.0f),
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
fun GradientGlassCard() {
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
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.0f)
                        )
                    )
                )
                .blur(16.dp)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.0f),
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