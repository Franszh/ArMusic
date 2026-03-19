package com.example.armusic.domain.usecase

import androidx.sqlite.SQLiteException
import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.domain.model.Playlist
import com.example.armusic.domain.repository.MyMusicRepository

class GetPlayListsUseCase(
    val myMusicRepository: MyMusicRepository
) {
    suspend fun getPlayListFromDb(): ApiResponseWrapper<List<Playlist>>{
        return try {
            val list  = myMusicRepository.getPlayLists()
            ApiResponseWrapper.Success(list)
        }catch (e: SQLiteException){
            ApiResponseWrapper.Error(e.message.toString())
        }
    }
}