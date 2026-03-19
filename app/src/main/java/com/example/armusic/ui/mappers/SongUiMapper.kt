package com.example.armusic.ui.mappers

import com.example.armusic.data.network.dto.SongDto
import com.example.armusic.domain.model.Song
import com.example.armusic.ui.model.SongUi
import kotlin.concurrent.timerTask


fun Song.toUi():SongUi{
        return SongUi(
            id = id,
            title = name,
            artist =artist.toUi() ,
            duration = duration,
            albumArt = null,
            image = image,
            url = url
        )
    }

