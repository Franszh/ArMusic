package com.example.armusic.ui.screens.animation


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.runtime.Composable

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Offset.Companion.Infinite
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import com.example.armusic.R

@Composable
fun Title(){
    val context = rememberCompositionContext()
    // Creamos una animación infinita
    val infiniteTransition = rememberInfiniteTransition()

    // Animamos el blurRadius de la sombra, alternando entre 5f y 20f
    val blurRadius by infiniteTransition.animateFloat(
        initialValue = 3f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(
                durationMillis = 4000,
                easing = LinearEasing
            )
        )

    )
    val font = GoogleFont("M Plus 1")
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 5.dp, start = 110.dp)
    ) {
        Text(text = "ArMusic", style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily(
                    Font(
                        font,
                        fontProvider = provider,
                        weight = FontWeight.SemiBold,
                        style = FontStyle.Italic

                    )
                ),
                shadow = Shadow(
                    offset = Offset(2f, 2f),
                    blurRadius = blurRadius,  // blurRadius animado
                    color = colorResource(R.color.pallet1Cream).copy(alpha = 0.5f)
                ),
                color = colorResource(R.color.secondaryGray)
            ))
    }
}