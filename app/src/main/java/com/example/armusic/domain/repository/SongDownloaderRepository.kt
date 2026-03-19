package com.example.armusic.domain.repository

import com.example.armusic.domain.model.ApiResponseWrapper

interface SongDownloaderRepository {
    suspend fun downloadSong(url: String, onResult: (ApiResponseWrapper<Pair<String, ByteArray>>) -> Unit)
}