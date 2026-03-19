package com.example.armusic.domain.repository

import android.media.session.PlaybackState
import androidx.media3.exoplayer.ExoPlayer
import com.example.armusic.domain.model.PlayBackState
import com.example.armusic.domain.model.Song
import com.example.armusic.ui.state.PlayerState
import kotlinx.coroutines.flow.Flow

interface MusicPlayerRepository {

    fun getPlaybackState(): Flow<PlayBackState>
    suspend fun play(songUrl: String):ExoPlayer
    suspend fun pause()
    suspend fun next()
    suspend fun previous()
    suspend fun resume()
    suspend fun stop()
    suspend fun seekTo(position: Long)
    suspend fun release()
}