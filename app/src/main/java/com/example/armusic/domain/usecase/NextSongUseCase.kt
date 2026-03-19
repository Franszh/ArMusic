package com.example.armusic.domain.usecase

import com.example.armusic.domain.repository.MusicPlayerRepository

class NextSongUseCase(
    private val musicPlayerRepository: MusicPlayerRepository
) {
    suspend operator fun invoke(){
        musicPlayerRepository.next()
    }
}