package com.snakecase.pomodoro

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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


data class ColorVentana(private var colorConstructor : Color) {

    var color by mutableStateOf(colorConstructor)

    fun setColorVentana(nuevoColor : Color){
        color = nuevoColor
    }

    fun getColorVentana() : Color {
        return color
    }
}

data class BrilloVentana(var valor : Float) {

    var brillo by  mutableStateOf(valor)

    fun setBrillos(nuevoBrillo : Float){
        brillo = nuevoBrillo
    }

    fun getBrillos() : Float {
        return brillo
    }

}

data class ColorTexto(private var colorTexto : Color) {

    fun setColorTexto(nuevoColorTexto : Color){
        colorTexto = nuevoColorTexto
    }

    fun getColorTexto() : Color {
        return colorTexto
    }

}

class AplicacionPomodoro {

    val colorVentana = ColorVentana(Color.White)
    val brilloVentana = BrilloVentana(1F)
    val timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
    val colorTexto = ColorTexto(Color.Black)
    val colorVentanaConfiguracion = ColorVentana(Color.White)

    @Composable
    fun CrearBoton(
        context: Context,
        estado: String,
        idImagen: Int,
        descripcion: String,
        onClick: () -> Unit
    ) {
        IconButton(
            onClick = {
                onClick()
                Toast.makeText(context, estado, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
        ) {
            Image(
                painter = painterResource(id = idImagen),
                contentDescription = descripcion,
                modifier = Modifier.size(80.dp)
            )
        }
    }



    @Composable
    fun CrearBotones(context: Context) {
        var minutos by remember { mutableIntStateOf(timerPomodoro.obtenerMinutos()) }
        var segundos by remember { mutableIntStateOf(timerPomodoro.obtenerSegundos()) }
        var timerActivo by remember { mutableStateOf(!timerPomodoro.enPausa()) }
        val reproductorSonido = MediaPlayer.create(context, R.raw.alarma1)

        LaunchedEffect(timerActivo) {
            if (timerActivo) {
                while (minutos >= 0 || segundos >= 0) {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        timerPomodoro.pasa1Segundo()
                        minutos = timerPomodoro.obtenerMinutos()
                        segundos = timerPomodoro.obtenerSegundos()
                        if (minutos == 0 && segundos == 0) {
                            timerPomodoro.pasa1Segundo()
                            minutos = timerPomodoro.obtenerMinutos()
                            segundos = timerPomodoro.obtenerSegundos()
                            timerPomodoro.pausar()
                            timerActivo = timerPomodoro.enPausa()
                            reproductorSonido.start()
                        }
                    }
                }
            }
        }

        fun pausarTimer() {
            timerPomodoro.pausar()
            timerActivo = timerPomodoro.enPausa()
        }

        fun reanudarTimer() {
            timerPomodoro.reanudar()
            timerActivo = timerPomodoro.enPausa()
        }

        fun reiniciarTimer() {
            timerPomodoro.reiniciar()
            timerPomodoro.pausar()
            timerActivo = timerPomodoro.enPausa()
            minutos = timerPomodoro.obtenerMinutos()
            segundos = timerPomodoro.obtenerSegundos()
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${minutos.toString().padStart(2, '0')}:",
                        fontSize = 70.sp,
                        fontFamily = vt323FontFamily,
                        color = colorTexto.getColorTexto()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = segundos.toString().padStart(2, '0'),
                        fontSize = 70.sp,
                        fontFamily = vt323FontFamily,
                        color = colorTexto.getColorTexto()
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    CrearBoton(
                        context = context,
                        estado = if (timerActivo) "Pausado" else "Iniciado",
                        idImagen = if (timerActivo) R.drawable.pause else R.drawable.play,
                        descripcion = "Botón de Play/Pausa",
                        onClick = {
                            if (timerActivo) pausarTimer() else reanudarTimer()
                        }
                    )

                    CrearBoton(
                        context = context,
                        estado = "Reiniciado",
                        idImagen = R.drawable.backwards,
                        descripcion = "Botón de Reiniciar",
                        onClick = {
                            reiniciarTimer()
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun CrearImagenTomate(modifier: Modifier) {
        Image(
            painter = painterResource(id = R.drawable.tomate_study),
            contentDescription = "Tomate",
            modifier = Modifier.size(750.dp)
        )

    }

    @Composable
    fun crearBotonModoOscuro() {

        var activado by remember { mutableStateOf(false)}

        Spacer(modifier = Modifier.size(30.dp))
        if (!activado) {
            Button(onClick = {
                colorTexto.setColorTexto(Color.White)
                colorVentanaConfiguracion.setColorVentana(Color.Black)
                activado = true
            }, shape = RectangleShape, modifier = Modifier.offset(x = 5.dp)) {
                Text("Modo Oscuro                                                      >>", style = TextStyle(fontSize = 15.sp))
            }
        }
        if (activado) {
            Button(onClick = {
                colorTexto.setColorTexto(Color.Black)
                colorVentanaConfiguracion.setColorVentana(Color.White)
                activado = false
            }, shape = RectangleShape, modifier = Modifier.offset(x = 5.dp)) {

                Text("Modo Oscuro                                                      >>", style = TextStyle(fontSize = 15.sp))

            }

        }

    }

    @Composable
    fun modificarBrilloAplicacion() {



        Text(text = "Ajustar Brillo", style = MaterialTheme.typography.bodyMedium, color = colorTexto.getColorTexto())
        Slider(value = brilloVentana.getBrillos(), onValueChange = {brilloVentana.setBrillos(it)},
            valueRange = 0.1f..1f, steps = 100, modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) { detectTransformGestures { _, _, _, _ -> } })
        Text(text = "Brillo Actual: ${(brilloVentana.getBrillos() * 100).toInt()} %", color = colorTexto.getColorTexto())

    }

    @Composable
    fun CrearBotonesColores(onColorSelected: (Color) -> Unit) {
        val colores = listOf(
            Color(0xFFFFFFFF), Color(0xFF81C784), Color(0xFFFFC0CB), Color(0xFFFFD54F),
            Color(0xFFE6CCFF), Color(0xFFFFB74D), Color(0xFF4DB6AC), Color(0xFF7986CB),
            Color(0xFF4DD0E1), Color(0xFFAED581), Color(0xFF9575CD), Color(0xFF64B5F6)
        )

        var selectedColor by remember { mutableStateOf<Color?>(null) }

        Column {
            repeat(4) { rowIndex ->
                Row {
                    repeat(3) { colIndex ->
                        val colorIndex = rowIndex * 3 + colIndex
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(colores[colorIndex])
                                .clickable {
                                    selectedColor = colores[colorIndex]
                                    onColorSelected(colores[colorIndex])
                                }
                        ) {
                            if (selectedColor == colores[colorIndex]) {
                                Text("✔️", color = Color.White, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ConfigSlider(label: String, initialValue: Int, onValueChange: (Int) -> Unit, range: IntRange, step: Int) {
        var value by remember { mutableStateOf(initialValue) }
        Column {
            Text(text = "$label: $value", color = colorTexto.getColorTexto())
            Slider(
                value = value.toFloat(),
                onValueChange = { newValue ->
                    value = newValue.toInt()
                    onValueChange(value)
                },
                valueRange = range.first.toFloat()..range.last.toFloat(),
                steps = (range.last - range.first) / step - 1
            )
        }
    }

    @Composable
    fun CrearBotonConfiguracion(navController : NavHostController) {
        IconButton(
            onClick =  {navController.navigate("pantallaConfiguracion")},
            modifier = Modifier
                .padding(top = 50.dp, end = 16.dp)
                .size(80.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.settingicon),
                contentDescription = "Imagen de Configuracion",
                modifier = Modifier.size(80.dp)
            )
        }
    }



    @Composable
    fun PantallaSeleccionColor(navController : NavHostController) {

        Column(modifier = Modifier
            .fillMaxSize()
            .drawBrightnessOverlay(brilloVentana.getBrillos())
            .background(color = colorVentanaConfiguracion.getColorVentana()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(onClick = {navController.navigate("pantallaConfiguracion")}, colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color.Transparent),
                    modifier = Modifier.offset(x = -15.dp, y = -145.dp)) {
                    Text("<< Configuración", color = colorTexto.getColorTexto())
                }
                Text(text = "Personalización", modifier = Modifier.offset(x = 140.dp, y = -130.dp), color = colorTexto.getColorTexto())
                Column(modifier = Modifier
                    .padding(8.dp)
                    .offset(x = 0.dp, y = -100.dp)) {
                    modificarBrilloAplicacion()
                    crearBotonModoOscuro()
                }
            }


            Text("Color de Fondo del Pomodoro", color = colorTexto.getColorTexto())
            CrearBotonesColores { color ->
                colorVentana.setColorVentana(color)
            }

            Spacer(modifier = Modifier.size(30.dp))

            Button(onClick ={ navController.navigate("PantallaPrincipal")} ) {
                Text("Confirmar Color")
            }

        }

    }

    @Composable
    fun PantallaConfiguracion(navController : NavHostController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .drawBrightnessOverlay(brilloVentana.getBrillos())
                .background(color = colorVentanaConfiguracion.getColorVentana()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(onClick = {navController.navigate("pantallaPrincipal")}, colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color.Transparent),
                    modifier = Modifier.offset(x = -160.dp, y = -10.dp)) {
                    Text("<< Volver", color = colorTexto.getColorTexto())
                }
                Text(text = "Configuración", modifier =  Modifier.offset(x = 0.dp, y=5.dp), color = colorTexto.getColorTexto())


            }

            Spacer(modifier = Modifier.size(30.dp))


            Column(modifier = Modifier.padding(16.dp)) {
                ConfigSlider("Ciclos", timerPomodoro.cicloConteo, timerPomodoro::updateFocusCount, 1..12, 1, )
                ConfigSlider("Estudio Time", timerPomodoro.estudioTime, timerPomodoro::updateFocusTime, 5..120, 5)
                ConfigSlider("Descanso Time", timerPomodoro.descansoTime, timerPomodoro::updateBreakTime, 5..60, 5)
                ConfigSlider("Descanso Largo Time", timerPomodoro.descansoLargoTime, timerPomodoro::updateLongBreakTime, 5..60, 5)
            }

            Spacer(modifier = Modifier.size(30.dp))
            Column(modifier = Modifier
                .fillMaxSize(), horizontalAlignment = Alignment.Start) {
                Button(onClick ={ navController.navigate("pantallaColores")}, modifier = Modifier.offset(x = 10.dp, y = 0.dp), shape = RectangleShape) {
                    Text("Personalización                                                    >>", style = TextStyle(fontSize = 15.sp))

                }
            }

        }
    }

    @Composable
    fun PantallaPrincipal(navController: NavHostController) {
        PomodoroTheme {
            val context = LocalContext.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBrightnessOverlay(brilloVentana.getBrillos()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(color = colorVentana.getColorVentana())
                ) {
                    CrearBotonConfiguracion(navController)
                    CrearImagenTomate(modifier = Modifier.padding(top = 100.dp))
                    Spacer(modifier = Modifier.height(30.dp))
                    CrearBotones(context)
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }


    @Composable
    fun ejecutarAplicacion(savedInstanceState: Bundle?) {


        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "pantallaPrincipal") {
            composable("pantallaPrincipal") { PantallaPrincipal(navController)}
            composable("pantallaColores") { PantallaSeleccionColor(navController) }
            composable("pantallaConfiguracion") {PantallaConfiguracion(navController)}
        }
    }

    fun Modifier.drawBrightnessOverlay(brightness: Float): Modifier = this.then(
        Modifier.graphicsLayer(alpha = brightness)
    )
}