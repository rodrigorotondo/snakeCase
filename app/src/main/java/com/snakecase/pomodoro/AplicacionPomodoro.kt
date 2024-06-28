package com.snakecase.pomodoro

import android.content.Context
import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.concurrent.timer


class AplicacionPomodoro {

    val ventanaPrincipal = VentanaPrincipal()
    val ventanaConfiguracion = VentanaConfiguracion()
    val ventanaPersonalizacion = VentanaPersonalizacion()
    val ventanaCambioImagen = VentanaCambioImagen()
    val ventanaCambioAudio = VentanaCambioAudio()

    @Composable
    fun EjecutarAplicacion(savedInstanceState: Bundle?) {


        val navController = rememberNavController()
        var colorVentana = Ventana.ColorVentana(Color.White)
        var brilloVentana = Ventana.BrilloVentana(1F)
        var timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
        var colorTexto = Ventana.ColorTexto(Color.Black)
        var colorVentanaConfiguracion = Ventana.ColorVentana(Color.White)
        var idImagenPrincipalPomodoro = Ventana.IdImagenPomodoro(R.drawable.tomate_study)
        var idAudio = Ventana.IdAudioPomodoro(R.raw.alarma1)

        NavHost(navController = navController, startDestination = "pantallaLeaderBoard") {
            composable("pantallaPrincipal") { ventanaPrincipal.PantallaPrincipal(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio)}
            composable("pantallaColores") { ventanaPersonalizacion.PantallaSeleccionColor(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio) }
            composable("pantallaConfiguracion") {ventanaConfiguracion.PantallaConfiguracion(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio)}
            composable("pantallaCambioImagen") {ventanaCambioImagen.PantallaCambioImagen(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio)}
            composable("pantallaCambioAudio") {ventanaCambioAudio.PantallaCambioAudio(navController, colorVentana, brilloVentana, timerPomodoro, colorTexto, colorVentanaConfiguracion, idImagenPrincipalPomodoro, idAudio)}
            composable("login") { PantallaLogin(navController)}
            composable("registrarUsuario") { PantallaRegistrarUsuario(navController)}
            composable("pantallaLeaderBoard"){ PantallaLeaderBoard(navController, "usuario")}
        }
    }
}