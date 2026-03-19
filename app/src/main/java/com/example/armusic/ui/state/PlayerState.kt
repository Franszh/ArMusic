package com.example.armusic.ui.state

import androidx.media3.exoplayer.ExoPlayer
import kotlinx.serialization.descriptors.StructureKind

sealed class PlayerState<out T: Any> {
    object NoPlayer: PlayerState<Nothing>()
    data class Active<T: Any>(val player: T): PlayerState<T>()
}