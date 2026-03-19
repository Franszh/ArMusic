package com.example.armusic.data.repository

import java.sql.Time

data class Result(
    val duration: String,
    val id: String,
    val thumbnail: String,
    val thumbnail_high: String,
    val thumbnail_medium: String,
    val title: String,
    val uploader: String,
    val url: String
)