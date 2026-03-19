package com.example.armusic.ui.model

import java.sql.Time


data class SongUi(
        val id: String,
        val title: String,
        val artist: ArtistUi,
        val duration: String,
        val albumArt: String?,
        val image: String,
        val url: String
)
