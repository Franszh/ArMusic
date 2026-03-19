package com.example.armusic.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.armusic.data.local.entities.AlbumesEntity
import com.example.armusic.data.local.entities.ArtistEntity
import com.example.armusic.data.local.entities.ArtistSongEntity
import com.example.armusic.data.local.entities.PlaylistEntity
import com.example.armusic.data.local.entities.PlaylistSongEntity
import com.example.armusic.data.local.entities.PlaylistWithSongs
import com.example.armusic.data.local.entities.SongEntity
import com.example.armusic.data.local.entities.TypeEntity

@Dao
interface SongDao {

    /**conseguir todas las canciones**/
    @Query("SELECT * FROM SongEntity")
    fun getAll(): List<SongEntity>

    /**conseguir canciones por id**/
    @Query("SELECT * FROM SongEntity WHERE song_id IN (:songid)")
    fun loadAllByIds(songid: IntArray): List<SongEntity>

    @Query("SELECT * FROM songentity WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): SongEntity

    @Insert
    fun insertAll(
        songEntity: SongEntity,
        artistEntity: ArtistEntity,
        artistSongEntity: ArtistSongEntity,
        albumesEntity: AlbumesEntity,
        playlistSong: PlaylistSongEntity,
        playlist: PlaylistEntity,
        type:TypeEntity
    )
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songEntity: SongEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artistEntity: ArtistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artistSongEntity: ArtistSongEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(albumesEntity: AlbumesEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistSong: PlaylistSongEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(type: TypeEntity): Long


    @Delete
    fun delete(user: SongEntity)


    @Query("SELECT * FROM PlaylistEntity WHERE name LIKE :name LIMIT 1")
    fun findPlaylistByName(name: String): PlaylistEntity

    @Query("SELECT count FROM PlaylistEntity WHERE name LIKE :name LIMIT 1")
    fun getCountOfListById(name: String):Int

    @Query("SELECT position,MAX(ple.count) FROM PlaylistSongEntity LEFT JOIN PlaylistEntity ple WHERE name LIKE :name LIMIT 1")
    fun getLastPosition(name: String):Int
    @Query("SELECT name FROM PlaylistEntity WHERE name LIKE :name LIMIT 1")
    fun getListByName(name: String): String?
    @Transaction
    @Query("SELECT * FROM PlaylistEntity")
    fun getAllPlayLists(): List<PlaylistWithSongs>
}