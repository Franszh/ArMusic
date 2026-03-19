package com.example.armusic.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.armusic.ui.screens.DrawerMenu
import com.example.armusic.ui.screens.MusicPlayer
import com.example.armusic.ui.screens.MyMusicScreen

import com.example.armusic.ui.screens.Search
import com.example.armusic.ui.viewModel.InfoSongVM
import dagger.hilt.android.lifecycle.HiltViewModel

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController no encontrado")
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Nav(){
    val navController = rememberNavController()



        CompositionLocalProvider(LocalNavController provides navController) {
            DrawerMenu {
                NavHost(navController= navController, startDestination = Home) {


                    composable<Home> {backStackEntry->

                        val infoSongVM:InfoSongVM = hiltViewModel()
                        Search(infoSongVM)
                    }

                    composable<MPlayer>{backStackEntry->
                        val player:MPlayer = backStackEntry.toRoute()
                        val parentEntry = remember(backStackEntry){ navController.getBackStackEntry<Home>()}
                        val infoSongVM:InfoSongVM = hiltViewModel(parentEntry)
                        MusicPlayer(player.urlSong,infoSongVM)
                    }

                    composable<MyMusic> {
                        MyMusicScreen()
                    }
                }
            }
        }
    }



