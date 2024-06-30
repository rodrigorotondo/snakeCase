package com.snakecase.pomodoro


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakecase.DataBaseManager
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController


@Composable
fun PantallaLeaderBoard(navController: NavHostController, viewModel: LoginViewModel) {
    val nombreUsuario = viewModel.obtenerUserName()
    val posicionInicial = viewModel.obtenerPosicionInicialLeaderBoard()
    var posicionActual = 0
    var leaderboard by remember { mutableStateOf<HashMap<String, Int>>(hashMapOf()) }
    var dataList by remember { mutableStateOf<List<Pair<String, Int>>>(listOf()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val dbManager = DataBaseManager(nombreUsuario)
        dbManager.obtenerLeaderBoard { result ->
            leaderboard = result
            dataList = leaderboard.toList().sortedByDescending { it.second }
            posicionActual = obtenerPosicionActual(dataList, nombreUsuario)
            crearAvisoPosicion(posicionInicial,posicionActual,context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "LeaderBoard:",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(dataList) { item ->
                LeaderboardItem(username = item.first, score = item.second)
            }
        }
    }
}

@Composable
fun LeaderboardItem(username: String, score: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = username,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = vt323FontFamily
        )
        Text(
            text = score.toString(),
            fontSize = 20.sp,
            fontFamily = vt323FontFamily,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}


fun obtenerPosicionActual(lista: List<Pair<String, Int>>, nombreUsuario: String): Int{
    var contar = true
    var posicion = 0
    for((clave,valor) in lista){
        if(clave != nombreUsuario && contar){

            posicion += 1
        }
        else{
            contar = false
        }
    }
    return posicion

}

fun crearAvisoPosicion(posicionInicial: Int, posicionActual: Int, context: Context){
    if(posicionActual < posicionInicial){
        Toast.makeText(context, "Felicidades, avanzaste ${posicionInicial - posicionActual} posiciones!! ", Toast.LENGTH_LONG).show()
    }else if(posicionActual > posicionInicial){
        Toast.makeText(context, "Descendiste ${posicionActual-posicionInicial} posiciones :( ", Toast.LENGTH_LONG).show()
    }
}


