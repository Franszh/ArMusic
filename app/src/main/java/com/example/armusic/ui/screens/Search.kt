package com.example.armusic.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil3.compose.AsyncImage
import com.example.armusic.R
import com.example.armusic.data.repository.Result
import com.example.armusic.domain.model.Song
import com.example.armusic.navigation.LocalNavController
import com.example.armusic.navigation.MPlayer
import com.example.armusic.ui.mappers.toUi
import com.example.armusic.ui.model.SongUi
import com.example.armusic.ui.screens.customBackgrounds.GlassmorphismCard
import com.example.armusic.ui.viewModel.InfoSongVM

import com.example.armusic.ui.viewModel.SearchTYViewModel
import com.example.armusic.ui.viewModel.SearchTxtVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Search(infoSongVM: InfoSongVM) {
    val searchTxtVM = SearchTxtVM()
    val text by searchTxtVM.text.observeAsState("")


    var pressed by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    //Para realizar la busqueda
    val viewModel: SearchTYViewModel = hiltViewModel()
    val search by viewModel.searchResult.observeAsState()






        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.backgroundBlue))
        ) {


            TextField(value = text, onValueChange = { searchTxtVM.setText(it) }, modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(50.dp)
                .background(
                    colorResource(R.color.secondaryGray), shape = RoundedCornerShape(36.dp)
                ),
                shape = RoundedCornerShape(36.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                label = { Text("Search") })
            Button(modifier = Modifier
                .background(
                    colorResource(R.color.Pallet1Brown),
                    shape = RoundedCornerShape(33.dp)

                )
                .align(Alignment.CenterHorizontally)
                .height(50.dp)
                .width(130.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(R.color.Pallet1Brown),
                    disabledContainerColor = colorResource(id = R.color.Pallet1Brown),
                    containerColor = colorResource(R.color.Pallet1Brown)
                ),
                onClick = {
                    coroutineScope.launch {
                        viewModel.getVideos(text)
                        pressed = true
                    }

                }) {
                Text(text = "Search", color = colorResource(R.color.pallet1Cream), fontSize = 20.sp)
            }

            if (pressed) {


                search?.let { results ->

                    ShowSearchItems(
                        results.map { it.toUi() },
                        viewModel
                    ) // Solo se ejecuta si search no es null
                } ?: Text(text = "No results found") // Mensaje en caso de que sea null

            }

        }



}


@Composable
fun PreviewSearch() {


}




@Composable
fun ShowSearchItems(list: List<SongUi>, viewModel: SearchTYViewModel) {

    val infoSongVM: InfoSongVM = hiltViewModel()
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()

    ) {

        LazyColumn {
            items(list) { item ->
                SearchItem(item.title, item.image, item.url, viewModel, infoSongVM)
            }
        }
    }
    val navHost = LocalNavController.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val infoSong by infoSongVM.songInfo.collectAsStateWithLifecycle(null)
    val songInfoError by infoSongVM.error.observeAsState("")
    //conseguir el url de la cancion


    val errorGettingUrl by viewModel.error.observeAsState("")
    LaunchedEffect(Unit) {

        viewModel.urlSongResult.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect {
                if (errorGettingUrl == "") {

                    navHost.navigate(MPlayer(it))

                } else if (songInfoError != "") {

                    Toast.makeText(context, songInfoError, Toast.LENGTH_LONG)
                        .show()


                } else {


                    Toast.makeText(context, errorGettingUrl, Toast.LENGTH_LONG)
                        .show()


                }
            }
    }



}

@Composable
fun SearchItem(title: String, image: String, urlSong: String, geturlVM: SearchTYViewModel, infoSongVM: InfoSongVM) {

    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val isloading by infoSongVM.isLoading.collectAsStateWithLifecycle(false)

    Spacer(modifier = Modifier.height(5.dp))
    GlassmorphismCard {
        Row(


        ) {
            Spacer(modifier = Modifier.width(0.5.dp))
            AsyncImage(
                model = image, contentDescription = "",
                modifier =
                Modifier
                    .width(150.dp)
                    .height(170.dp)
                    .align(Alignment.CenterVertically)
                    .clip(shape = RoundedCornerShape(13.dp))
                    .clickable {
                        if (urlSong.isNotEmpty() && image.isNotEmpty()) {

                            coroutine.launch {

                                geturlVM.getUrlSongVM(urlSong)
                                infoSongVM.getInfoSong(urlSong)
                                infoSongVM.setLoading(true)

                            }


                        } else {

                            coroutine.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "No se encuentra el URL",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }

                        }

                    },
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                color = Color(0xD7FFFFFF),
                modifier = Modifier.padding(top = 20.dp, start = 20.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}



