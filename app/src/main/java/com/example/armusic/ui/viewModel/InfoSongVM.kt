package com.example.armusic.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.domain.model.Song
import com.example.armusic.domain.usecase.GetSongUseCase
import com.example.armusic.ui.state.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoSongVM @Inject constructor(
    private val getSongUseCase: GetSongUseCase
) : ViewModel() {
    private val _songInfo = MutableStateFlow<Song?>(null)
    val songInfo = _songInfo.asSharedFlow()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asSharedFlow()

    suspend fun getInfoSong(urlId: String) {
        getSongUseCase.getSongInfo(urlId) { success ->
            when (success) {
                is ApiResponseWrapper.Success -> {
                    _error.value = ""
                    _isLoading.value = false
                    _songInfo.value = success.data
                }

                is ApiResponseWrapper.Error -> {
                    _songInfo.value = null
                    _isLoading.value = false
                    _error.value = success.message
                }

                ApiResponseWrapper.Loading -> {
                    _isLoading.value = true
                }
            }
        }


    }

    suspend fun setLoading(isloading: Boolean){
        _isLoading.emit(value = isloading)
    }
}