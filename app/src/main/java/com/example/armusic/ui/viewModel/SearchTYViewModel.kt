package com.example.armusic.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.domain.model.Song
import com.example.armusic.domain.usecase.GetSongUseCase
import com.example.armusic.ui.state.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class SearchTYViewModel @Inject constructor(
    val getSongUseCase: GetSongUseCase
): ViewModel() {
    private val _searchResult = MutableLiveData<List<Song>>()
    val searchResult: LiveData<List<Song>> get() = _searchResult


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _urlSongResult = MutableSharedFlow<String>()
    val urlSongResult  = _urlSongResult.asSharedFlow()

    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading = _isLoading.asSharedFlow()
    suspend fun getVideos(query: String) {

        try {
            when (val list = getSongUseCase.invoke(query)) {
                is ApiResponseWrapper.Success -> {
                    _searchResult.value = list.data
                    _error.value = "" // Limpiar cualquier error previo
                }

                is ApiResponseWrapper.Error -> {

                    _error.value = list.message ?: "Error desconocido"
                    Log.e("SearchViewModel", "Error en búsqueda: ${list.message}")
                }

                ApiResponseWrapper.Loading -> {
                    _isLoading.emit(value = true)
                }

            }
        } catch (e: Exception) {
            // Manejar excepciones no capturadas
            _error.value = "Error inesperado: ${e.message}"
            Log.e("SearchViewModel", "Excepción en getVideos", e)
        }
    }

    suspend fun getUrlSongVM(url: String){
        when(val result = getSongUseCase.getUrlSong(url)){
            is ApiResponseWrapper.Success->{
                _urlSongResult.emit(result.data)
                _error.value = ""
            }
            is ApiResponseWrapper.Error->{
                _error.value = result.message ?: "Error desconocido"
                Log.e("SearchViewModel", "Error en búsqueda: ${result.message}")
                ApiResponseWrapper.Error(result.message).message
            }

            ApiResponseWrapper.Loading -> {
                _isLoading.emit(value = true)
            }
        }
    }
    suspend fun clearUrl(){
        _urlSongResult.emit("")
    }
    suspend fun setLoading(isloading: Boolean){
        _isLoading.emit(value = isloading)
    }


}