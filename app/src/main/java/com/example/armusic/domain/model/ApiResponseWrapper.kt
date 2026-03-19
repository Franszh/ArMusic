package com.example.armusic.domain.model

sealed class ApiResponseWrapper<out T> {
    data class Success<T>(val data: T) : ApiResponseWrapper<T>()
    data class Error(val message: String) : ApiResponseWrapper<Nothing>()
    data object Loading: ApiResponseWrapper<Nothing>()
}