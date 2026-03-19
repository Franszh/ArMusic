package com.example.armusic.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.armusic.data.local.AppDatabase
import com.example.armusic.data.local.entities.AlbumesEntity
import com.example.armusic.data.local.entities.ArtistEntity
import com.example.armusic.data.local.entities.ArtistSongEntity
import com.example.armusic.data.local.entities.PlaylistEntity
import com.example.armusic.data.local.entities.PlaylistSongEntity
import com.example.armusic.data.local.entities.SongEntity
import com.example.armusic.data.local.entities.TypeEntity
import com.example.armusic.data.network.dto.SongDto
import com.example.armusic.domain.model.ApiResponseWrapper
import com.example.armusic.domain.model.Song
import com.example.armusic.domain.repository.SongDownloaderRepository
import com.example.armusic.ui.mappers.toDto
import com.example.armusic.ui.viewModel.AudioDownloadVM
import androidx.sqlite.SQLiteException
import com.github.f4b6a3.ulid.UlidCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.regex.Pattern

class DownloadSongUseCase(
    private val musicDownloaderInterface: SongDownloaderRepository
) {
    suspend operator fun invoke(url: String,onResult: (ApiResponseWrapper<Pair<String, ByteArray>>) -> Unit){
        musicDownloaderInterface.downloadSong(url, onResult)
    }

    companion object{
        private const val MAX_FILE_SIZE = 50 * 1024 * 1024
        private const val MIN_FILE_SIZE = 1024
        private const val MAX_FILENAME_LENGTH = 200


        private val ALLOWED_MIME_TYPE = setOf(
            "audio/mp4",
            "audio/mpeg",
            "audio/wav",
            "audio/ogg",
            "audio/m4a"
        )

        private val SAFE_FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9áéíóúüñÁÉÍÓÚÜÑ ._(),:!¡?¿'\"-]+$")
        private val FORBIDDEN_EXTENSIONS  = setOf(
            "exe", "bat", "cmd", "scr", "pif", "com",
            "jar", "js", "vbs", "ps1", "sh"
        )


    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun guardarEnDescargas(context: Context, nombreArchivo: String, contenido: ByteArray, song: Song,viewmodel: AudioDownloadVM): Boolean {
        val validationError = validateEntryFile(contenido,nombreArchivo, "audio/mp4")
        if (validationError != null) {
            println("❌ Error de validación: $validationError")
            return false
        }

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, nombreArchivo)
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4")
            put(MediaStore.Audio.Media.IS_PENDING, 1)
            put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/ArMusic/songs")
            put(MediaStore.Audio.Media.IS_MUSIC, 1)
        }

        val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {newUri->
            resolver.openOutputStream(newUri)?.use { outputStream ->
                outputStream.write(contenido)
            }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(newUri, contentValues, null, null)
            val songDto = SongDto(
                id = UlidCreator.getUlid().toString(),
                name = song.name,
                genre = song.genre,
                artist = song.artist.toDto(),
                duration = song.duration,
                image = song.image,
                url = song.url
            )

            saveSongOnLocal(songDto, path = uri.path.toString(),context,viewmodel)
            return true
        }
        return false
    }

    fun validateEntryFile(contenido: ByteArray,nombreArchivo: String, mimeType: String):String?{
        val errorContent = validateContent(contenido)
        if(errorContent != null) return errorContent

        val errorName = validateName(nombreArchivo)
        if (errorName != null) return errorName

        val errorMime = validateMime(mimeType)
        if (errorMime != null) return errorMime

        val errorCoherence = validateCoherence(nombreArchivo,mimeType)
        if (errorCoherence != null) return errorCoherence


        return null
    }

    private fun validateContent(contenido: ByteArray): String?{
        return when{
            contenido.isEmpty()->"El archivo esta vacio"
            contenido.size < MIN_FILE_SIZE ->"El archivo es demasiado pequeño (mínimo $MIN_FILE_SIZE bytes)"
            contenido.size > MAX_FILE_SIZE ->"El archivo es demasiado grande (máximo ${MAX_FILE_SIZE / (1024*1024)}MB)"
            else->null
        }
    }
    private fun validateName(name: String): String?{
        return when{
            name.isBlank()->"El nombre del archivo no puede estar vacío"
            name.length > MAX_FILENAME_LENGTH ->"El nombre del archivo es demasiado largo (máximo $MAX_FILENAME_LENGTH caracteres)"
            name.startsWith(".")->"El nombre del archivo no puede empezar con punto"
            name.endsWith(".")->"El nombre del archivo no puede terminar con punto"
            name.contains("..")->"El nombre del archivo no puede contener '..' (traversal de directorio)"
            !SAFE_FILENAME_PATTERN.matcher(name).matches()->"El nombre del archivo contiene caracteres no permitidos. Solo se permiten letras, números, puntos, guiones y guiones bajos -> $name"
            haveDangerousExtension(name)->"La extensión del archivo no está permitida por seguridad"
            else -> null
        }
    }

    private fun haveDangerousExtension(name: String):Boolean{
        val extension = name.substringAfter(".","").lowercase()
        return FORBIDDEN_EXTENSIONS.contains(extension)
    }
    private fun validateMime(mimeType: String): String?{
        return when{
            mimeType.isBlank() -> ""
            !ALLOWED_MIME_TYPE.contains(mimeType.lowercase())->""
            else -> null
        }
    }
    private fun validateCoherence(fileName: String, mimeType: String):String?{
        val extension = fileName.substringAfter(".", "").lowercase()
        val lowerMime = mimeType.lowercase()

        val expectedExtension = when(lowerMime){
            "audio/mp4" -> listOf("m4a", "mp4")
            "audio/mpeg" -> listOf("mp3", "mpeg")
            "audio/wav" -> listOf("wav")
            "audio/ogg" -> listOf("ogg")
            "audio/m4a" -> listOf("m4a")
            else -> emptyList()
        }

        return if (expectedExtension.isEmpty() && !expectedExtension.contains(extension)){
            "La extensión '$extension' no coincide con el tipo MIME '$mimeType'. Extensiones esperadas: ${expectedExtension.joinToString(", ")}"
        }else{
            null
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun saveSongOnLocal(songDto: SongDto, path : String, context: Context,viewmodel: AudioDownloadVM){
        viewmodel.viewModelScope.launch(Dispatchers.IO){
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "ArMusicDataBase"
            ).build()


            val songDao = db.songDao()
            try {
                val artist = ArtistEntity(

                    name = songDto.artist.name
                )
                songDao.insert(artist)

                val song = SongEntity(
                    song_id = songDto.id,
                    name = songDto.name,
                    artist = artist.art_id,
                    genre = songDto.genre,
                    duration = songDto.duration,
                    image = songDto.image,
                    date = ZonedDateTime.now().toString(),
                    path = path
                )
                songDao.insert(song)
                val artistSongEntity = ArtistSongEntity(
                    song_id = songDto.id,
                    art_id = artist.art_id
                )
                songDao.insert(artistSongEntity)
                val type = TypeEntity(

                    name = "Descargas"
                )
                songDao.insert(type)
                val albumesEntity = AlbumesEntity(

                    artistEntity = artist.art_id,
                    image = "",
                    nombre = ""
                )
                songDao.insert(albumesEntity)
                val playlistEntity = PlaylistEntity(

                    name = "Descargas",
                    song = song.song_id,
                    type = type.tl_id,
                    count = 0

                )
                songDao.insert(playlistEntity)
                val existList = songDao.getListByName("Descargas")?: ""
                val lastposition: Int
                var position = 0
                if (existList.isNotBlank()){
                    lastposition = songDao.getLastPosition("Descargas")
                    position = lastposition + 1
                }else if(existList.isBlank()) position = 1


                val playlistSong = PlaylistSongEntity(
                    pl_id = playlistEntity.playl_id,
                    song_id = song.song_id,

                    position = position
                )
                songDao.insert(playlistSong)

            }catch (e: SQLiteException){
                Log.e("ERROR TRYING TO INSERT TABLE",e.message.toString())

            }


            /*songDao.insertAll(
                songEntity = song,
                artistEntity = artist,
                artistSongEntity = artistSongEntity,
                albumesEntity = albumesEntity,
                playlistSong = playlistSong,
                playlist = playlistEntity,
                type = type
            )*/
        }

    }


}