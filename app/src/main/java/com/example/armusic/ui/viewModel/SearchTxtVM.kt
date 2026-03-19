package com.example.armusic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SearchTxtVM {
    private val _text= MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    fun setText(text : String){
        _text.value = text
    }
}