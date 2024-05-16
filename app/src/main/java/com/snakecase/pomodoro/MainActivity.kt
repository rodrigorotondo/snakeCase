package com.snakecase.pomodoro

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.snakecase.pomodoro.ui.theme.PomodoroTheme
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
data class ColorVentana(private var color : Color) {


    fun setColor(nuevoColor : Color){
        color = nuevoColor
    }

    fun getColor() : Color {
        return color
    }
}

@Composable
fun crearBoton(context: Context, estado: String, id_imagen: Int, descripcion: String, onClick: () -> Unit) {
    Spacer(modifier = Modifier.width(64.dp))
    IconButton(
        onClick = {
            onClick()
            Toast.makeText(context, estado, Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.padding(top = 500.dp),
    ) {
        Image(painter = painterResource(id = id_imagen), contentDescription = descripcion)
    }
}
@Composable
fun crear_botones(context: Context) {
    var tiempo by remember { mutableStateOf(1500) }
    var timerActivo by remember { mutableStateOf(false) }

    LaunchedEffect(timerActivo) {
        if (timerActivo) {
            while (tiempo > 0) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    tiempo--
                }
            }
        }
    }

    fun pausarTimer() {
        timerActivo = false
    }

    fun reiniciarTimer() {
        tiempo = 1500 // Reiniciar a 25 minutos
        timerActivo = true
    }

    val minutos = tiempo / 60
    val segundos = tiempo % 60
    val tiempoFormateado = String.format("%02d:%02d", minutos, segundos)

    Text(text = tiempoFormateado)


    crearBoton(context = context, estado = "Pausado", id_imagen = android.R.drawable.ic_media_pause, descripcion = "Boton de Pausa", onClick = {
        pausarTimer()
    })

    crearBoton(context = context, estado = "Iniciado", id_imagen = android.R.drawable.ic_media_play, descripcion = "Boton de Play", onClick = {
        timerActivo = true
    })

    crearBoton(context = context, estado = "Reiniciado", id_imagen = android.R.drawable.ic_media_previous, descripcion = "Boton de Reiniciado", onClick = {
        reiniciarTimer()
    })
}
@Composable
fun CrearImagenTomate() {
    Image(
        painter = painterResource(id = R.drawable.tomate),
        contentDescription = "Tomate",
        modifier = Modifier.size(700.dp)
    )

}

@Composable
fun crearBotonColor(onNavigateBack: () -> Unit, colorVentana : ColorVentana, color : Color, stringColor : String) {

    Button(onClick = {
        onNavigateBack()
        colorVentana.setColor(color)
    }) {
        Text(text = stringColor)
    }

}

@Composable
fun crearBotonesColores(onNavigateBack: () -> Unit, colorVentana : ColorVentana){

    crearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Red, stringColor = "Rojo")
    crearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Blue, stringColor = "Azul")
    crearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Yellow, stringColor = "Amarillo")
    crearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Gray, stringColor = "Gris")
    crearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.White, stringColor = "Blanco")
    crearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = Color.Black, stringColor = "Negro")
    crearBotonColor(onNavigateBack = onNavigateBack, colorVentana = colorVentana, color = colorVentana.getColor(), stringColor = "Volver")


}


@Composable
fun pantallaConfiguracion(onNavigateBack: () -> Unit, colorVentana : ColorVentana) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Configuracion")
        Spacer(modifier = Modifier.height(20.dp))

        crearBotonesColores(onNavigateBack = onNavigateBack, colorVentana = colorVentana)
    }


}

@Composable
fun CrearBotonConfiguracion(context: Context, onNavigate: () -> Unit) {
    
    IconButton(onClick =  onNavigate ,
        modifier = Modifier
            .padding(top = 16.dp)
            .offset(x = 320.dp)) {
        Image(painter = painterResource(id = R.drawable.configuracion), contentDescription = "Imagen de Configuracion")
        
    }

}

@Composable
fun pantallaPrincipal(onNavigate: () -> Unit, colorVentana : ColorVentana) {
    PomodoroTheme {
        val context = LocalContext.current
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(color = colorVentana.getColor())) {
                CrearBotonConfiguracion(context, onNavigate)
                CrearImagenTomate()
                Row(){
                    crear_botones(context)
                }

            }
        }

    }



}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var colorVentana = ColorVentana(Color.Red)
        setContent {

            var pantallaActual by remember { mutableStateOf("main") }

            when (pantallaActual) {
                "main" -> pantallaPrincipal(onNavigate = {pantallaActual = "second"}, colorVentana)
                "second" ->pantallaConfiguracion(onNavigateBack = { pantallaActual = "main"}, colorVentana)
            }


        }


    }

    

}

