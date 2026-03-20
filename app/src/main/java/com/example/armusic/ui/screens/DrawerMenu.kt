package com.example.armusic.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.armusic.R
import com.example.armusic.navigation.LocalNavController
import com.example.armusic.navigation.MyMusic
import com.example.armusic.ui.screens.animation.Title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navHost = LocalNavController.current
    val lifecycleOwner = LocalLifecycleOwner.current


    ModalNavigationDrawer(

        drawerContent = {
            ModalDrawerSheet(
            drawerContainerColor = colorResource(R.color.mygray)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    val textColor: Color = colorResource(R.color.grayBlue)
                    Spacer(Modifier.height(12.dp))
                    Text("Configuration", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, color = textColor)
                    HorizontalDivider(color = colorResource(R.color.pallet1graySteel))


                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = colorResource(R.color.mygray)),
                        label = {
                            Text("Mi Musica", color = textColor)
                                },
                        selected = false,
                        onClick = {

                            scope.launch {
                                navHost.navigate(MyMusic)
                                drawerState.snapTo(DrawerValue.Closed)
                            }

                        }

                    )
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = colorResource(R.color.mygray)),
                        label = { Text("Configuración", color = textColor) },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = colorResource(R.color.mygray)),
                        label = { Text("Item 3", color = textColor) },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = colorResource(R.color.mygray)),
                        label = { Text("Item 4", color = textColor) },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )

                }
            }
        },
        drawerState = drawerState

    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Title() },
                    elevation = 0.dp,
                    windowInsets = WindowInsets(top = 30.dp),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Image(painter = painterResource(R.drawable.iconmenu), contentDescription = "", modifier = Modifier.size(50.dp))

                        }
                    },
                    contentColor = colorResource(R.color.backgroundBlue),
                    backgroundColor = colorResource(R.color.backgroundBlue),
                    modifier = Modifier.drawBehind {

                        drawRect(Color.Blue)
                    }
                )
            },
            drawerBackgroundColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DrawerPreview(){
    DrawerMenu {  }
}