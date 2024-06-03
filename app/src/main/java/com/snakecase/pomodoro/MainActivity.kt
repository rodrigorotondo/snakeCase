package com.snakecase.pomodoro

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snakecase.pomodoro.ui.theme.PomodoroTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


data class ColorVentana(private var color : Color) {


    fun setColor(nuevoColor : Color){
        color = nuevoColor
    }

    fun getColor() : Color {
        return color
    }
}



@Composable
fun CrearBoton(context: Context, estado: String, idImagen: Int, descripcion: String, onClick: () -> Unit) {
    Spacer(modifier = Modifier.width(64.dp))
    IconButton(
        onClick = {
            onClick()
            Toast.makeText(context, estado, Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.padding(top = 500.dp)
    ) {
        Image(painter = painterResource(id = idImagen), contentDescription = descripcion, modifier = Modifier.size(100.dp))
    }
}


@Composable
fun CrearBotones(context: Context, pomodoro: Pomodoro) {

    var minutos by remember { mutableIntStateOf(pomodoro.obtenerMinutos()) }
    var segundos by remember { mutableIntStateOf(pomodoro.obtenerSegundos()) }
    var timerActivo by remember { mutableStateOf(!pomodoro.enPausa()) }
    val reproductorSonido = MediaPlayer.create(context ,R.raw.alarma1)

    LaunchedEffect(timerActivo) {
        if (timerActivo) {
            while (minutos >= 0 || segundos >= 0) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    pomodoro.pasa1Segundo()
                    minutos = pomodoro.obtenerMinutos()
                    segundos = pomodoro.obtenerSegundos()
                    if(minutos == 0 && segundos == 0){
                        pomodoro.pasa1Segundo()
                        minutos = pomodoro.obtenerMinutos()
                        segundos = pomodoro.obtenerSegundos()
                        pomodoro.pausar()
                        timerActivo = pomodoro.enPausa()
                        reproductorSonido.start()
                    }
                }
            }
        }
    }

    fun pausarTimer() {
        pomodoro.pausar()
        timerActivo = pomodoro.enPausa()
    }

    fun reanudarTimer() {
        pomodoro.reanudar()
        timerActivo = pomodoro.enPausa()
    }

    fun reiniciarTimer() {
        pomodoro.reiniciar()
        pomodoro.pausar()
        timerActivo = pomodoro.enPausa()
        minutos = pomodoro.obtenerMinutos()
        segundos = pomodoro.obtenerSegundos()
    }


    val tiempoFormateado = String.format("%02d:%02d", minutos, segundos)

    Box {
        Text(text = tiempoFormateado, fontSize = 70.sp, modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
            .padding(100.dp)
            .padding(top = 230.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(50.dp))
            CrearBoton(context = context, estado = if (timerActivo) "Pausado" else "Iniciado", idImagen = if (timerActivo) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play, descripcion = "Botón de Play/Pausa", onClick = {
                if(timerActivo) pausarTimer() else reanudarTimer()
            })

            CrearBoton(context = context, estado = "Reiniciado", idImagen = android.R.drawable.ic_media_previous, descripcion = "Botón de Reiniciar", onClick = {
                reiniciarTimer()
            })
        }
    }

}
@Composable
fun CrearImagenTomate() {
    Image(
        painter = painterResource(id = R.drawable.tomate),
        contentDescription = "Tomate",
        modifier = Modifier.size(750.dp)
    )

}

@Composable
fun CrearBotonColor(onNavigateBack: () -> Unit, colorVentana : ColorVentana, color : Color, stringColor : String) {

    Button(onClick = {
        onNavigateBack()
        colorVentana.setColor(color)
    }) {
        Text(text = stringColor)
    }

}

@Composable
fun CrearBotonesColores(onNavigateBack: () -> Unit, colorVentana : ColorVentana){

    CrearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Red, stringColor = "Rojo")
    CrearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Blue, stringColor = "Azul")
    CrearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Yellow, stringColor = "Amarillo")
    CrearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Gray, stringColor = "Gris")
    CrearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.White, stringColor = "Blanco")
    CrearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Black, stringColor = "Negro")
    CrearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = colorVentana.getColor(), stringColor = "Volver")


}


@Composable
fun PantallaConfiguracion(onNavigateBack: () -> Unit, colorVentana : ColorVentana) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Configuracion")
        Spacer(modifier = Modifier.height(20.dp))

        CrearBotonesColores(onNavigateBack = onNavigateBack, colorVentana = colorVentana)
    }


}

@Composable
fun CrearBotonConfiguracion(context: Context, onNavigate: () -> Unit) {

    IconButton(onClick =  onNavigate ,
        modifier = Modifier
            .padding(top = 50.dp)
            .offset(x = 320.dp)) {
        Image(painter = painterResource(id = R.drawable.configuracion), contentDescription = "Imagen de Configuracion")

    }

}



@Composable
fun PantallaPrincipal(onNavigate: () -> Unit, colorVentana : ColorVentana, pomodoro: Pomodoro) {
    PomodoroTheme {
        val context = LocalContext.current
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(color = colorVentana.getColor())) {
                CrearBotonConfiguracion(context, onNavigate)
                CrearImagenTomate()
                CrearBotones(context, pomodoro)

            }
        }

    }



}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val colorVentana = ColorVentana(Color.Red)
        val timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
        setContent {

            var pantallaActual by remember { mutableStateOf("login") }

            when (pantallaActual) {
                "login" -> PantallaLogin(onNavigate = {pantallaActual = "main"})
                "main" -> PantallaPrincipal(onNavigate = {pantallaActual = "second"}, colorVentana, timerPomodoro)
                "second" ->PantallaConfiguracion(onNavigateBack = { pantallaActual = "main"}, colorVentana)
            }


        }


    }



}

