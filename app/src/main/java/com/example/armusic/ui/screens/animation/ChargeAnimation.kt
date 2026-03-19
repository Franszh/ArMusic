package com.example.armusic.ui.screens.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.armusic.R

@Composable
fun PulsingBarsLoader(
    modifier: Modifier = Modifier,
    color: Color = colorResource(R.color.backgroundBlue),
    barWidth: Dp = 6.dp,
    barMaxHeight: Dp = 40.dp
) {
    val bars = (0..4).map { index ->
        remember { Animatable(0.3f) }
    }

    LaunchedEffect(Unit) {
        bars.forEachIndexed { index, animatable ->
            kotlinx.coroutines.delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = EaseInOutCubic),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        bars.forEach { animatable ->
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(barMaxHeight * animatable.value)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(barWidth / 2)
                    )
            )
        }
    }
}