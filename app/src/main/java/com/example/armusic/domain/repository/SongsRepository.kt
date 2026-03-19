package com.example.armusic.domain.repository

import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.domain.model.Song

interface SongsRepository {
    suspend fun getUrlSong(id: String): String
    suspend fun getSongs(query: String):List<Song>
    suspend fun getSongInfo(id: String, onResult:(ApiResponseWrapper<Song>)->Unit)
}