package com.example.armusic.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.domain.model.Playlist
import com.example.armusic.domain.usecase.GetPlayListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MyMusicVM @Inject constructor(
    val getPlayListsUseCase: GetPlayListsUseCase
): ViewModel() {
    private val _stateList = MutableStateFlow<List<Playlist>>(emptyList())
    val stateList = _stateList.asSharedFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asSharedFlow()

    private val _error = MutableStateFlow<String>("")
    val error = _error.asSharedFlow()

    fun getListFromLocal(){
        this.viewModelScope.launch(Dispatchers.IO) {
            when(val list = getPlayListsUseCase.getPlayListFromDb()){
                is ApiResponseWrapper.Success->{ _stateList.value = list.data}
                is ApiResponseWrapper.Error->{_error.value = "Error: ${list.message}"}
                ApiResponseWrapper.Loading -> _loading.value = true
            }
        }

    }
}