package com.example.armusic.data.network.remote

import com.example.armusic.data.repository.SearchResponse
import com.example.armusic.data.repository.UrlResponse
import com.example.armusic.domain.model.Song
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClientRetrofit {
    @GET("search_videos")
    suspend fun getData(
        @Query("query") query: String
    ): Response<SearchResponse>

    @GET("get_audio_url")
    suspend fun getStreamAudio(
        @Query("url") url: String
    ): Response<UrlResponse>
    @GET("download_music")
    fun getAudioFile(

        @Query("url") url: String
    ):Call<ResponseBody>

    @GET("get_video_info")
    fun getInfoMusic(
        @Query("url") url: String
    ):Call<Song>
}