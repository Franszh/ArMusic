package com.example.armusic.domain.usecase

import com.example.armusic.domain.model.Song
import com.example.armusic.domain.repository.SongsRepository
import com.example.armusic.domain.model.ApiResponseWrapper

class GetSongUseCase(
    private val songsRepository: SongsRepository
) {

    suspend operator fun invoke(query : String): ApiResponseWrapper<List<Song>>{
        return try {
            val songs = songsRepository.getSongs(query)
            ApiResponseWrapper.Success(songs)
        }catch (e: Exception){
            ApiResponseWrapper.Error(e.message.toString())
        }
    }
    suspend fun getUrlSong(urlId: String):ApiResponseWrapper<String>{
        return try {
            val url = songsRepository.getUrlSong(urlId)
            ApiResponseWrapper.Success(url)
        }catch (e: Exception){
            ApiResponseWrapper.Error(e.message.toString())
        }
    }
    suspend fun getSongInfo(urlId: String, onResult: (ApiResponseWrapper<Song>)->Unit){
        songsRepository.getSongInfo(urlId,onResult)
    }
}