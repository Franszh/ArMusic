package com.example.armusic.data.repository

import com.example.armusic.data.local.AppDatabase
import com.example.armusic.data.network.mappers.toDomain
import com.example.armusic.domain.model.Playlist
import com.example.armusic.domain.repository.MyMusicRepository
import javax.inject.Inject

class MyMusicImp @Inject constructor(
    val database: AppDatabase
):MyMusicRepository {
    override fun getPlayLists(): List<Playlist> {
        val list = database.songDao().getAllPlayLists()
        return list.map { it.toDomain() }

    }
}