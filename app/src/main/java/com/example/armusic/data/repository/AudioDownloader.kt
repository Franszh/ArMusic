package com.example.armusic.data.repository

import android.util.Log

import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.data.network.remote.ApiClientRetrofit
import com.example.armusic.domain.repository.SongDownloaderRepository
import retrofit2.Callback
import okhttp3.ResponseBody
import java.io.IOException
import javax.inject.Inject

class AudioDownloader @Inject constructor(
    //val okHttpClient: OkHttpClient
    val retrofit: ApiClientRetrofit
): SongDownloaderRepository {
    override suspend fun downloadSong(
        ytUrl: String,
        onResult: (ApiResponseWrapper<Pair<String, ByteArray>>) -> Unit
    ) {
        val call = retrofit.getAudioFile(ytUrl)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (!response.isSuccessful || response.body() == null) {
                    onResult(ApiResponseWrapper.Error("Error al descargar el audio"))
                    Log.e("ERROR AL DESCARGAR:", response.message())
                    return
                }

                try {
                    val audioBytes = response.body()!!.bytes()

                    val filename = response.headers()["Content-Disposition"]
                        ?.let { disposition ->
                            // Manejar filename="..." o filename=...
                            val regex = Regex("""filename\s*=\s*"?([^";\r\n]+)"?""", RegexOption.IGNORE_CASE)
                            regex.find(disposition)?.groupValues?.get(1)?.trim()
                        }
                        ?: "audioDescargado.m4a"

                    onResult(ApiResponseWrapper.Success(Pair(filename,audioBytes)))
                    Log.d("Download", "Content-Disposition: ${response.headers()}")
                } catch (e: IOException) {
                    e.printStackTrace()
                    onResult(ApiResponseWrapper.Error("Error al leer el audio"))
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                onResult(ApiResponseWrapper.Error("Error de red: ${t.message}"))
                Log.e("ERROR DE RED:", t.message!!)
            }
        })
    }




    /*
    fun downloadAudio(url: String, destinationFile: File, onResult: (Boolean) -> Unit) {
        val request = Request.Builder().url(url).build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onResult(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onResult(false)
                    return
                }

                response.body?.byteStream()?.use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    onResult(true)
                } ?: onResult(false)
            }
        }
    }*/
}