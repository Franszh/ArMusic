package com.example.armusic.navigation

import com.example.armusic.ui.viewModel.InfoSongVM
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
data class MPlayer(val urlSong: String)
@Serializable
object MyMusic