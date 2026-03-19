package com.example.armusic.domain.repository

import com.example.armusic.domain.model.Playlist

interface MyMusicRepository {
    fun getPlayLists():List<Playlist>
}