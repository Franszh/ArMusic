package com.example.armusic.ui.mappers

import com.example.armusic.domain.model.Artist
import com.example.armusic.ui.model.ArtistUi



    fun Artist.toUi():ArtistUi{
        return ArtistUi(
            id = id,
            name= name
        )
    }
