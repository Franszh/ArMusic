package com.example.armusic.ui.mappers

import com.example.armusic.data.local.entities.ArtistEntity
import com.example.armusic.data.network.dto.ArtistDto
import com.example.armusic.data.network.dto.SongDto
import com.example.armusic.domain.model.Artist
import com.example.armusic.domain.model.Song

fun Artist.toDto(): ArtistDto{
    return ArtistDto(
        id = id,
        name = name
    )
}


fun Song.toDto(): SongDto{
    return SongDto(
        id = id,
        name = name,
        artist = artist.toDto(),
        genre = genre,
        duration = duration,
        image = image,
        url = url
    )
}