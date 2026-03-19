package com.example.armusic.data.repository

import android.util.Log
import com.example.armusic.data.network.dto.ArtistDto
import com.example.armusic.data.network.dto.SongDto
import com.example.armusic.data.network.remote.ApiClientRetrofit
import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.domain.model.Artist
import com.example.armusic.domain.model.Song
import com.example.armusic.domain.repository.SongsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException

class GetSearchResult @Inject constructor(
    private val retrofit: ApiClientRetrofit
): SongsRepository {
    suspend fun getSearchResult(query:String): ApiResponseWrapper<SearchResponse> {
        return try {
            val response = retrofit.getData(query)
            if (response.isSuccessful){
                response.body()?.let {

                    ApiResponseWrapper.Success(it)

                }?: ApiResponseWrapper.Error("empty body")
            }else{
                ApiResponseWrapper.Error("ERROR : " + response.code().toString())
            }
        }catch (e: Exception){
            Log.e("ErrorOnGetSearch",e.message.toString())
            ApiResponseWrapper.Error(e.message.toString())
        }

    }

    override suspend fun getUrlSong(id: String): String {

         val url = getUrl(id)
        return when(url){
            is ApiResponseWrapper.Success->{
                url.data.url
            }

            is ApiResponseWrapper.Error->{
                ApiResponseWrapper.Error("Error al conseguir url").message
            }

            ApiResponseWrapper.Loading -> TODO()
        }
    }

    override suspend fun getSongs(query: String): List<Song> {

            val listSong = ArrayList<Song>()
            return when (val result = getSearchResult(query)){

                is ApiResponseWrapper.Success->{
                    result.data.results.forEach {

                        listSong.add(Song(
                            id = "",
                            name = it.title,
                            artist = Artist(id = it.id, name =it.uploader),
                            genre = emptyList(),
                            duration = it.duration,
                            image = it.thumbnail,
                            url = it.url,
                        ))
                    }
                    return listSong
                }
                is ApiResponseWrapper.Error->{
                   emptyList<Song>()
                }

                ApiResponseWrapper.Loading -> TODO()
            }
    }

    override suspend fun getSongInfo(id: String,onResult: (ApiResponseWrapper<Song>)->Unit){
        val call = retrofit.getInfoMusic(id)
        call.enqueue(object : Callback<Song>{
            override fun onResponse(call: Call<Song>, response: Response<Song>) {

                if (!response.isSuccessful || response.body() == null) {
                    onResult(ApiResponseWrapper.Error("Error al descargar el audio"))
                    Log.e("ERRROR GETTING INFO:", response.message())
                    return
                }


                try{
                    val response = response.body()!!
                    Log.e("CUERPO DE LA RESPUESTA",response.toString())
                    onResult(ApiResponseWrapper.Success(Song(
                        id = response.id,
                        name = response.name,
                        artist = response.artist,
                        genre = response.genre,
                        duration = response.duration,
                        image = response.image,
                        url = response.url
                    )))
                }catch (e:Exception){
                    ApiResponseWrapper.Error("ERROR GETTING INFO: $e")
                }
            }

            override fun onFailure(call: Call<Song>, error: Throwable) {
                Log.e("API", "=== DEBUG COMPLETO ===")
                Log.e("API", "Error type: ${error::class.java.simpleName}")
                Log.e("API", "Error message: ${error.message}")
                Log.e("API", "Stack trace completo:")
                error.printStackTrace()

                // Tipos específicos de error
                when (error) {
                    is SocketTimeoutException -> Log.e("API", "TIMEOUT en socket")
                    is ConnectException -> Log.e("API", "Error de CONEXIÓN")
                    is UnknownHostException -> Log.e("API", "Host no encontrado")
                    is SSLException -> Log.e("API", "Error SSL")
                    is IOException -> {
                        Log.e("API", "IOException específica:")
                        Log.e("API", "Cause: ${error.cause?.message}")
                        Log.e("API", "LocalizedMessage: ${error.localizedMessage}")
                    }
                }

                // Info del request
                val request = call.request()
                Log.e("API", "URL: ${request.url}")
                Log.e("API", "Method: ${request.method}")
                Log.e("API", "Headers: ${request.headers}")
                Log.e("ERROR DE RED:", error.message!!)
                onResult(ApiResponseWrapper.Error("Error de red: ${error.message}"))

            }

        })
    }


    fun ArtistDto.toDomain(): Artist {
        return Artist(
            id = id,
            name = name
        )
    }
    fun SongDto.toDomain(): Song{
        return Song(
            id = id,
            name = name,
            artist = artist.toDomain(),
            genre = genre,
            duration = duration,
            image = "",
            url = url,
        )
    }
    fun Result.toArtistDto(): ArtistDto{
        return ArtistDto(
            id = "",
            name = uploader
        )

    }
    fun Result.toSongDto(resultList : List<Result>):List<SongDto>{
        val listSong = ArrayList<SongDto>()
        resultList.forEach {result->
                val songDto = SongDto(
                    id ="" ,
                    name =result.title,
                    artist = result.toArtistDto(),
                    genre = emptyList(),
                    duration = result.duration,
                    image = thumbnail,
                    url = result.url
                )
                listSong.add(songDto)
        }
        return listSong.toList()
    }


    suspend fun getUrl(url:String): ApiResponseWrapper<UrlResponse> {
        return try {
            val response = retrofit.getStreamAudio(url)
            if (response.isSuccessful){
                response.body()?.let {
                    ApiResponseWrapper.Success(it)
                }?: ApiResponseWrapper.Error("empty body")
            }else{
                ApiResponseWrapper.Error("ERROR : " + response.code().toString())
            }
        }catch (e: Exception){
            Log.e("ErrorGettingUrl",e.message.toString())
            ApiResponseWrapper.Error(e.message.toString())
        }

    }
}