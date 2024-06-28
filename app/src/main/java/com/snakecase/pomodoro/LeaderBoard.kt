package com.snakecase.pomodoro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.snakecase.DataBaseManager

@Composable

fun PantallaLeaderBoard(navController : NavHostController, nombreUsuario: String){
    var DB = DataBaseManager(nombreUsuario)
    val leaderboard by remember{
        mutableStateOf( DB.obtenerLeadearBoard())}
    val dataList by remember {
        mutableStateOf(leaderboard.toList())
    }
    Text(text = "tamanio de la lista: ${leaderboard.size}")
    LazyColumn {
        items(dataList){item ->
            Box(){

                Text(text = "${dataList.size}")
            }


        }
    }

}