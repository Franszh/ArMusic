package com.example.armusic.domain.model

import java.sql.Time

data class Song(
    val id: String,
    val name: String,
    val artist: Artist,
    val genre: List<String>,
    val duration: String,
    val image: String,
    val url: String
)