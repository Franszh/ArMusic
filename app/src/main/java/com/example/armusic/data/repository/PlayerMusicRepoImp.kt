package com.example.armusic.data.repository

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.armusic.domain.model.PlayBackState
import com.example.armusic.domain.repository.MusicPlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlayerMusicRepoImp @Inject constructor(
    private val exoPlayer: ExoPlayer
): MusicPlayerRepository {

    override fun getPlaybackState(): Flow<PlayBackState> {
        TODO("Not yet implemented")
    }


    override suspend fun play(songUrl:String):ExoPlayer {
        val player = exoPlayer.apply {
            val mediaItem = MediaItem.fromUri(songUrl) // Carga la URL del video
            setMediaItem(mediaItem)
            prepare() // Prepara el video para reproducirse
            playWhenReady = true // Inicia la reproducción automáticamente
        }

        return player
    }

    override suspend fun pause() {
        exoPlayer.pause()
    }

    override suspend fun next() {
        exoPlayer.nextMediaItemIndex
    }

    override suspend fun previous() {
        exoPlayer.previousMediaItemIndex
    }

    override suspend fun resume() {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }

    override suspend fun seekTo(position: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun release() {
        TODO("Not yet implemented")
    }

}