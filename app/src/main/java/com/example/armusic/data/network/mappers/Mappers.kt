package com.example.armusic.data.network.mappers

import com.example.armusic.data.local.entities.ArtistEntity
import com.example.armusic.data.local.entities.PlaylistEntity
import com.example.armusic.data.local.entities.PlaylistWithSongs
import com.example.armusic.data.local.entities.SongEntity
import com.example.armusic.data.network.dto.ArtistDto
import com.example.armusic.data.network.dto.SongDto
import com.example.armusic.domain.model.Artist
import com.example.armusic.domain.model.Playlist
import com.example.armusic.domain.model.Song

fun SongDto.toEntity():SongEntity{
    return SongEntity(
        song_id = id,
        name = name,
        artist = artist.id,
        genre = genre,
        duration = duration,
        image = image,
        path = "",
        date = ""
    )
}
fun ArtistEntity.toDomain():Artist{
    return Artist(
        id = art_id,
        name = name
    )
}
fun SongEntity.toDomain(artist_id: String): Song{
    return Song(
        name = name,
        id = song_id,
        artist = ArtistEntity(art_id= artist_id, name = artist).toDomain(),
        image = image,
        url = path,
        genre = genre,
        duration = duration
    )
}

fun ArtistEntity.toDto():ArtistDto{
    return ArtistDto(
        id = art_id,
        name = name
    )
}

fun ArtistDto.toEntity(): ArtistEntity{
    return ArtistEntity(
        art_id = id,
        name = name
    )
}

fun PlaylistWithSongs.toDomain():Playlist{
    return Playlist(
        name = playlist.name,
        list = songs.map { it.toDomain(it.artist) }
    )

}