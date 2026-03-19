package com.example.armusic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.armusic.data.local.entities.AlbumesEntity
import com.example.armusic.data.local.entities.ArtistEntity
import com.example.armusic.data.local.entities.ArtistSongEntity
import com.example.armusic.data.local.entities.Converters
import com.example.armusic.data.local.entities.PlaylistEntity
import com.example.armusic.data.local.entities.PlaylistSongEntity
import com.example.armusic.data.local.entities.SongEntity
import com.example.armusic.data.local.entities.TypeEntity


@Database(
    entities = [
        ArtistEntity::class,
        SongEntity::class,
        ArtistSongEntity::class,
        AlbumesEntity::class,
        PlaylistSongEntity::class,
        PlaylistEntity::class,
        TypeEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}