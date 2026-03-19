package com.example.armusic.ui.viewModel

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room

import com.example.armusic.data.local.AppDatabase
import com.example.armusic.data.local.entities.AlbumesEntity
import com.example.armusic.data.local.entities.ArtistEntity
import com.example.armusic.data.local.entities.ArtistSongEntity
import com.example.armusic.data.local.entities.PlaylistEntity
import com.example.armusic.data.local.entities.PlaylistSongEntity
import com.example.armusic.data.local.entities.SongEntity
import com.example.armusic.data.local.entities.TypeEntity
import com.example.armusic.data.network.dto.SongDto
import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.domain.model.Song
import com.example.armusic.domain.usecase.DownloadSongUseCase
import com.example.armusic.ui.mappers.toDto
import com.github.f4b6a3.ulid.UlidCreator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.ZonedDateTime
import java.util.regex.Pattern
import javax.inject.Inject

import android.content.Context as Context1

@HiltViewModel
class AudioDownloadVM @Inject constructor(
    private val audioDownloader: DownloadSongUseCase,

):ViewModel() {
    private val _dowloadState  = MutableStateFlow<Boolean>(false)
    val downloadState = _dowloadState.asStateFlow()





    @RequiresApi(Build.VERSION_CODES.Q)
     suspend fun downloadAudio(url: String, file: File, context: Context,song: Song) {


       audioDownloader.invoke(url) { success ->
            when(success){
                is ApiResponseWrapper.Success ->{
                    val (filename,byteArray) = success.data
                    val saved = audioDownloader.guardarEnDescargas(context,song.name,byteArray,song, this)
                    if (saved){
                        _dowloadState.value = true
                    }



                }

                is ApiResponseWrapper.Error -> {_dowloadState.value = false}
                ApiResponseWrapper.Loading -> TODO()
            }


        }
    }



}