package com.example.armusic.data.network.dto

import java.sql.Time

data class SongDto(
    val id: String,
    val name: String,
    val artist: ArtistDto,
    val genre: List<String>,
    val duration: String,
    val image: String,
    val url: String
)
