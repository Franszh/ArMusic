package com.example.armusic.data.repository

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject

class PlaySong @Inject constructor(
    private val exoPlayer: ExoPlayer
) {
    fun execute(songUrl: String): ExoPlayer{
        return exoPlayer.apply {
            val mediaItem = MediaItem.fromUri(songUrl) // Carga la URL del video
            setMediaItem(mediaItem)
            prepare() // Prepara el video para reproducirse
            playWhenReady = true // Inicia la reproducción automáticamente
        }
    }

}

