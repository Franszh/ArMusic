package com.example.armusic.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.armusic.domain.model.Playlist
import com.example.armusic.domain.model.Song
import com.github.f4b6a3.ulid.UlidCreator
import java.sql.Time
import java.util.UUID


@Entity
data class ArtistEntity(
    @PrimaryKey
    val art_id: String = UlidCreator.getUlid().toString(),
    val name: String
)

@Entity(
    foreignKeys =
    [ForeignKey(
        entity = ArtistEntity::class,
        parentColumns = ["art_id"],
        childColumns = ["artistEntity"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class AlbumesEntity(
    @PrimaryKey
    val alb_id: String = UlidCreator.getUlid().toString(),
    val nombre: String,
    val artistEntity: String,
    val image: String
)

@Entity(
    primaryKeys = ["art_id", "song_id"]

)
data class ArtistSongEntity(
    val art_id: String = UlidCreator.getUlid().toString(),
    val song_id: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = ArtistEntity::class,
        parentColumns = ["art_id"],
        childColumns = ["artist"],
        onDelete = ForeignKey.CASCADE
    )
    ]
)
data class SongEntity(
    @PrimaryKey
    val song_id: String = UlidCreator.getUlid().toString(),
    val name: String,
    val genre: List<String>,
    val artist: String,
    val duration: String,
    val image: String,
    val date: String,
    val path: String
)

@Entity(
    primaryKeys = ["pl_id", "song_id"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["playl_id"],
            childColumns = ["pl_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["song_id"],
            childColumns = ["song_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaylistSongEntity(
    /** INTERMEDIATE TABLE **/
    val pl_id: String,
    val song_id: String,
    val position: Int
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TypeEntity::class,
            parentColumns = ["tl_id"],
            childColumns = ["type"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["song_id"],
            childColumns = ["song"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaylistEntity(
    @PrimaryKey
    val playl_id: String = UlidCreator.getUlid().toString(),
    val name: String,
    val type: String,
    val song: String,
    val count: Int
)

@Entity
data class TypeEntity(
    @PrimaryKey
    val tl_id: String = UlidCreator.getUlid().toString(),
    val name: String
)

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playl_id",
        entityColumn = "song_id",
        associateBy = Junction(
            PlaylistSongEntity::class,
            parentColumn = "pl_id",
            entityColumn = "song_id"
        )

    )
    val songs: List<SongEntity>
)
