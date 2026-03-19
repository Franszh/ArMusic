package com.example.armusic.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider


import androidx.compose.material3.Icon


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowCircleDown
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import coil3.compose.AsyncImage
import com.example.armusic.R
import com.example.armusic.domain.model.Song
import com.example.armusic.ui.screens.animation.PulsingBarsLoader
import com.example.armusic.ui.viewModel.AudioDownloadVM
import com.example.armusic.ui.viewModel.InfoSongVM
import com.example.armusic.ui.viewModel.MusicPlayerVM
import com.example.armusic.ui.viewModel.SearchTYViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MusicPlayer(songUrl: String,infoSongVM: InfoSongVM) {

    val context = LocalContext.current
    val playerVM: MusicPlayerVM = hiltViewModel()
    val scope = rememberCoroutineScope()
    val viewModel: SearchTYViewModel = hiltViewModel()
    val downloadVM: AudioDownloadVM = hiltViewModel()


    val infoSong by infoSongVM.songInfo.collectAsStateWithLifecycle(null)




    LaunchedEffect(Unit) {
        playerVM.playSong(songUrl)

    }
    MusicPlayerView(playerVM, downloadVM, songUrl,infoSongVM)


}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MusicPlayerView(
    player: MusicPlayerVM,
    downloadVM: AudioDownloadVM,
    songUrl: String,
    infoSongVM: InfoSongVM

) {
    val context = LocalContext.current
    val playerState by player.playerState.observeAsState()
    val isPlaying by player.isPlaying.observeAsState(true)
    val downloadState by downloadVM.downloadState.collectAsStateWithLifecycle(false)

    val infoSong by infoSongVM.songInfo.collectAsStateWithLifecycle(null)
    val isLoading by infoSongVM.isLoading.collectAsStateWithLifecycle(false)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 20.dp)
            .background(colorResource(R.color.backgroundBlue))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalArrangement = Arrangement.Center
        ) {


            if (isLoading) {

                Box(
                    modifier = Modifier
                        .height(400.dp)
                        .width(400.dp)
                        .padding(30.dp)

                        .background(
                            colorResource(R.color.pallet1sandy),
                            shape = RoundedCornerShape(36.dp)
                        )
                        .shadow(
                            spotColor = colorResource(R.color.pallet1sandy),
                            elevation = 15.dp,
                            shape = RoundedCornerShape(36.dp)
                        )
                ) {
                    Box(modifier = Modifier.align(Alignment.Center).wrapContentSize()){
                        PulsingBarsLoader()
                    }

                }



            } else {

                Box(
                    modifier = Modifier
                        .height(400.dp)
                        .width(400.dp)
                        .padding(30.dp)
                        .background(
                            colorResource(R.color.pallet1sandy),
                            shape = RoundedCornerShape(36.dp)
                        )
                        .shadow(
                            spotColor = colorResource(R.color.pallet1sandy),
                            elevation = 15.dp,
                            shape = RoundedCornerShape(36.dp)
                        )
                ) {


                    AsyncImage(
                        model = infoSong?.image,
                        contentDescription = "Imagen de reproductor",
                        contentScale = ContentScale.Crop,
                        clipToBounds = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(1.dp)
                            .background(
                                colorResource(R.color.pallet1sandy),
                                shape = RoundedCornerShape(36.dp)
                            )
                            .shadow(
                                spotColor = colorResource(R.color.pallet1sandy),
                                elevation = 15.dp,
                                shape = RoundedCornerShape(36.dp)
                            )
                            .size(400.dp),


                        )
                }




            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            infoSong?.let {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .padding(end = 10.dp, start = 20.dp),
                    fontSize = 25.sp,
                    color = colorResource(R.color.grayBlue),

                    )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        SeekBar(player)

        /**---------------------PARA DESCARGAR-----------------**/
        Icon(
            Icons.Rounded.ArrowCircleDown,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 20.dp)
                .width(30.dp)
                .height(30.dp)
                .clickable {
                    scope.launch {
                        if (infoSong != null){
                            downloadVM.downloadAudio(
                                songUrl,
                                File(context.getExternalFilesDir(null), "audioMUSIC"),
                                context,
                                infoSong!!
                            )

                        }else{
                            Toast.makeText(context, "infoSong esta vacio", Toast.LENGTH_LONG).show()
                        }

                        if (downloadState == true) {
                            Toast.makeText(context, "Se completo la descarga", Toast.LENGTH_LONG).show()
                        }
                    }



                },//corregir la compatibilidad de API
            contentDescription = "Download song",
            tint = colorResource(R.color.grayBlue)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val modifier =
                Modifier
                    .padding(start = 10.dp)
                    .width(80.dp)
                    .height(80.dp)

            val colorsButton = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.backgroundBlue),
                containerColor = colorResource(R.color.pallet1sandy),

                )


            /** ----------------CONTROLS---------------- **/

            Button(
                modifier = modifier,
                colors = colorsButton,
                onClick = { player.previous() }
            ) {
                Icon(
                    Icons.Rounded.SkipPrevious,
                    contentDescription = "Previous"
                )
            }
            ControlsPlayPause(player, isPlaying, modifier, colorsButton)
            Button(
                modifier = modifier,
                colors = colorsButton,
                onClick = { player.next() }
            ) {
                Icon(
                    Icons.Rounded.SkipNext,
                    contentDescription = "Next"
                )
            }
        }

    }
}

@Composable
fun ControlsPlayPause(
    player: MusicPlayerVM,
    isPlaying: Boolean,
    modifier: Modifier,
    buttonColor: ButtonColors
) {

    val visualPlayState = remember { mutableStateOf(isPlaying) }

    LaunchedEffect(isPlaying) {
        if (isPlaying != visualPlayState.value) {

            if (!isPlaying) {
                delay(300) //  retraso para evitar parpadeos
                // Vuelve a verificar si el estado sigue siendo el mismo
                if (!player.isPlaying.value!!) {
                    visualPlayState.value = false
                }
            } else {
                // Si cambia a reproducción, actualiza inmediatamente
                visualPlayState.value = true
            }
        }
    }


    Button(
        modifier = modifier,
        colors = buttonColor,
        onClick = {
            if (visualPlayState.value) player.pause() else player.resume()
        }
    ) {
        if (visualPlayState.value) {
            Icon(
                Icons.Rounded.Pause,
                contentDescription = "Pause"
            )
        } else {
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = "play"
            )
        }
    }
}


@Composable
fun SeekBar(vm: MusicPlayerVM) {

    val duration by vm.duration.observeAsState(1L)
    val currentTime by vm.currentTime.observeAsState(1L)


    Column(modifier = Modifier
        .height(60.dp)
        .fillMaxWidth()) {
        // seek bar
        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.dp, start = 25.dp),
            value = currentTime.toFloat(),
            onValueChange = {
                vm.isSeeking(true)
                vm.changeCurrentTime(it)

            },
            valueRange = 0f..duration.toFloat(),
            onValueChangeFinished = {
                vm.changeCurrentTime(currentTime.toFloat())
                vm.isSeeking(false)
            },
            colors = SliderDefaults.colors(
                activeTrackColor = colorResource(R.color.pallet1graybluish)
            )
        )
    }
}



@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL)

@Composable
fun MusicPlayerPreview() {
    val image = ""
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 20.dp)
            .background(colorResource(R.color.backgroundBlue))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalArrangement = Arrangement.Center
        ) {


            if (image.isNotEmpty()) {
                AsyncImage(
                    model = image,
                    contentDescription = "Imagen de reproductor",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)

                )
            } else {
                Box(
                    modifier = Modifier
                        .width(350.dp)
                        .height(350.dp)
                        .background(
                            colorResource(R.color.pallet1sandy),
                            shape = RoundedCornerShape(36.dp)
                        )
                        .shadow(
                            spotColor = colorResource(R.color.pallet1sandy),
                            elevation = 15.dp,
                            shape = RoundedCornerShape(36.dp)
                        )
                        .size(300.dp)
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = "Imagen de reproductor",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .background(
                                colorResource(R.color.pallet1sandy),
                                shape = RoundedCornerShape(36.dp)
                            )
                            .shadow(
                                spotColor = colorResource(R.color.pallet1sandy),
                                elevation = 15.dp,
                                shape = RoundedCornerShape(36.dp)
                            )
                            .size(300.dp)

                    )
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val modifier =
                Modifier
                    .padding(start = 10.dp)
                    .width(70.dp)
                    .height(70.dp)

            val colorsButton = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.backgroundBlue),
                containerColor = colorResource(R.color.pallet1sandy),

                )


        }
    }
}