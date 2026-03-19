package com.example.armusic.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.armusic.R
import com.example.armusic.domain.model.Playlist
import com.example.armusic.ui.screens.customBackgrounds.GradientGlassCard
import com.example.armusic.ui.theme.DegradeBlue
import com.example.armusic.ui.theme.SteelSmookeBlue
import com.example.armusic.ui.theme.StuneBlue
import com.example.armusic.ui.viewModel.MyMusicVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyMusicScreen(){
    val myMusicVM: MyMusicVM = hiltViewModel()
    val playlist by myMusicVM.stateList.collectAsStateWithLifecycle(emptyList())
    val error by myMusicVM.error.collectAsStateWithLifecycle("")
    val loading by myMusicVM.loading.collectAsStateWithLifecycle(false)
    myMusicVM.getListFromLocal()
    val lista = listOf("all", "Downloads","History","Another")
    val playlistsAux = listOf("Downloads","History","AnotherList")



    Box( modifier = Modifier
        .background(StuneBlue)
        .padding(10.dp)
        .fillMaxWidth()
        .fillMaxHeight()

    ) {
        Column {
            LazyRow(
                contentPadding = PaddingValues(20.dp)
            ) {
                if (playlist.isEmpty()){
                    items(lista){item->
                        ItemLabel(item)
                    }
                }
                items(playlist){item->
                    ItemLabel(item.name) //tarjetas para filtrar
                }
            }
            Spacer(Modifier.padding(bottom = 20.dp))
            LazyColumn() {
                items(playlist){item->
                    PlaylistItem(item.name)
                }
            }
        }





    }


}


@Composable
fun ItemLabel(text: String){
    Spacer(Modifier.padding(start = 5.dp))
    Column(
        modifier = Modifier
            .background(SteelSmookeBlue, shape = RoundedCornerShape(8.dp))
            .wrapContentSize()
            .padding(7.dp)
    ) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}

@Composable
fun PlaylistItem(name: String){
    GradientGlassCard(
        modifier = Modifier.height(100.dp).padding(start = 7.dp), colorBackground = DegradeBlue
        ) {
        Row(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {


            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "",
                contentScale = ContentScale.None,
                modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)).height(88.dp).width(88.dp)
            )
            Text(
                text = name,
                modifier = Modifier.padding(start = 20.dp),
                color = Color.LightGray
            )
        }
    }
    Spacer(modifier = Modifier.padding(bottom = 10.dp))


}
