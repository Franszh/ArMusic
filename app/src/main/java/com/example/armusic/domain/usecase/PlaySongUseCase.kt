package com.example.armusic.domain.usecase

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.armusic.domain.model.Song
import com.example.armusic.domain.repository.MusicPlayerRepository
import com.example.armusic.ui.state.PlayerState
import javax.annotation.meta.When

class PlaySongUseCase(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    suspend operator fun invoke(songUrl: String): ExoPlayer{

        return musicPlayerRepository.play(songUrl)

    }
}