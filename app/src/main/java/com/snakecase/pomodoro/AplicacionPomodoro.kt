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

data class IdImagenPomodoro(private var idImagen : Int) {

    fun setImagen(nuevaImagen : Int){
        idImagen = nuevaImagen
    }

    fun getImagen() : Int {
        return idImagen
    }
}

data class IdAudioPomodoro(private var idAudio : Int) {

    fun setAudio(nuevoAuido : Int) {
        idAudio = nuevoAuido
    }

    fun getAudio() : Int {
        return idAudio
    }
}

class AplicacionPomodoro {

    val colorVentana = ColorVentana(Color.White)
    val brilloVentana = BrilloVentana(1F)
    val timerPomodoro = Pomodoro(TipoTimer.ESTUDIO)
    val colorTexto = ColorTexto(Color.Black)
    val colorVentanaConfiguracion = ColorVentana(Color.White)
    val idImagenPrincipalPomodoro = IdImagenPomodoro(R.drawable.tomate_study)
    val idAudio = IdAudioPomodoro(R.raw.alarma1)

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
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 100.dp)
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
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 300.dp)
        ) {
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

    @Composable
    fun CrearImagenTomate() {

        Image(
            painter = painterResource(id = idImagenPrincipalPomodoro.getImagen()),
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
            }, shape = RectangleShape, modifier = Modifier
                .offset(x = 5.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)) {
                Text("Modo Oscuro                           >>", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp))
            }
        }
        if (activado) {
            Button(onClick = {
                colorTexto.setColorTexto(Color.Black)
                colorVentanaConfiguracion.setColorVentana(Color.White)
                activado = false
            }, shape = RectangleShape, modifier = Modifier.offset(x = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)){

                Text("Modo Oscuro                           >>", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp))

            }

        }

    }

    @Composable
    fun modificarBrilloAplicacion() {

        var color_aux = colorVentana.getColorVentana()
        if(colorVentana.getColorVentana() == Color.White){
            color_aux = Color.Gray
        }

        Text(
            text = "Ajustar Brillo",
            style = TextStyle(
                fontFamily = vt323FontFamily,
                fontSize = 18.sp,
                color = colorTexto.getColorTexto()
            )
        )
        Slider(
            value = brilloVentana.getBrillos(),
            onValueChange = { brilloVentana.setBrillos(it) },
            valueRange = 0.1f..1f,
            steps = 100,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) { detectTransformGestures { _, _, _, _ -> } },
            colors = SliderDefaults.colors(thumbColor = color_aux,
                activeTrackColor = color_aux)
        )
        Text(
            text = "Brillo Actual: ${(brilloVentana.getBrillos() * 100).toInt()} %",
            style = TextStyle(
                fontFamily = vt323FontFamily,
                fontSize = 16.sp,
                color = colorTexto.getColorTexto()
            )
        )
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
                                .border(BorderStroke(3.dp, color = Color.Gray))
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

        var color_aux = colorVentana.getColorVentana()
        if(colorVentana.getColorVentana() == Color.White){
            color_aux = Color.Gray
        }

        var value by remember { mutableStateOf(initialValue) }
        Column {
            Text(text = "$label: $value", color = colorTexto.getColorTexto(),
                fontSize = 20.sp,fontFamily = vt323FontFamily)
            Slider(
                value = value.toFloat(),
                onValueChange = { newValue ->
                    value = newValue.toInt()
                    onValueChange(value)
                },
                colors = SliderDefaults.colors(thumbColor = color_aux,
                    activeTrackColor = color_aux),
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
    fun crearBotonCambioImagen(navController : NavHostController) {
        Button(
            onClick = { navController.navigate("pantallaCambioImagen") },
            modifier = Modifier
                .offset(x = 5.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            shape = RectangleShape
        ) {
            Text("Cambiar Icono Del Pomodoro            >>",
                style = TextStyle(fontSize = 20.sp), fontFamily = vt323FontFamily)
        }
    }

    @Composable
    fun crearBotonIndividualCambioImagen(textoBoton : String, idImagen : Int) {

        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {idImagenPrincipalPomodoro.setImagen(idImagen)},
            modifier = Modifier
                .offset(x = -5.dp, y = 0.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            shape = RectangleShape
        ) {
            Text(textoBoton,
                style = TextStyle(fontSize = 23.sp), fontFamily = vt323FontFamily)
        }


    }

    @Composable
    fun crearBotonesCambioImagen() {

        crearBotonIndividualCambioImagen(textoBoton = "Tomate estudiando 1             >>", idImagen = R.drawable.tomate_study)
        crearBotonIndividualCambioImagen(textoBoton = "Tomate estudiando 2             >>", idImagen = R.drawable.tomate_study_2)
        crearBotonIndividualCambioImagen(textoBoton = "Tomate descansando 1             >>", idImagen = R.drawable.tomate_descanso)
        crearBotonIndividualCambioImagen(textoBoton = "Tomate descansando 2             >>", idImagen = R.drawable.tomate_descanso_2)
        crearBotonIndividualCambioImagen(textoBoton = "Tomate Italiano                 >>", idImagen = R.drawable.tomate_italiano)
        crearBotonIndividualCambioImagen(textoBoton = "Tomate peleando                 >>", idImagen = R.drawable.tomate_study)
    }

    @Composable
    fun crearBotonCambioTono(navController: NavHostController) {
        Button(
            onClick = { navController.navigate("pantallaCambioAudio") },
            modifier = Modifier
                .offset(x = 5.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            shape = RectangleShape
        ) {
            Text("Cambiar Audio                         >>",
                style = TextStyle(fontSize = 20.sp), fontFamily = vt323FontFamily)
        }
    }

    @Composable
    fun CrearBotonIndividualCambioAudio(textoBoton : String, id : Int) {

        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {idAudio.setAudio(id)},
            modifier = Modifier
                .offset(x = -5.dp, y = 0.dp)
                .border(BorderStroke(4.dp, color = Color.Gray)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
            shape = RectangleShape
        ) {
            Text(textoBoton,
                style = TextStyle(fontSize = 23.sp), fontFamily = vt323FontFamily)
        }
    }

    @Composable
    fun crearBotonesCambioAudio() {

        CrearBotonIndividualCambioAudio(textoBoton = "Audio 1             >>", id = R.raw.alarma1)
        CrearBotonIndividualCambioAudio(textoBoton = "Audio 2             >>", id = R.raw.alarma1)
        CrearBotonIndividualCambioAudio(textoBoton = "Audio 3             >>", id = R.raw.alarma1)
        CrearBotonIndividualCambioAudio(textoBoton = "Audio 4             >>", id = R.raw.alarma1)
        CrearBotonIndividualCambioAudio(textoBoton = "Audio 5             >>", id = R.raw.alarma1)
        CrearBotonIndividualCambioAudio(textoBoton = "Audio 6             >>", id = R.raw.alarma1)
    }

    @Composable
    fun PantallaCambioAudio(navController: NavHostController) {

        Column(modifier = Modifier
            .padding(top = 25.dp)
            .offset(x = 0.dp, y = -50.dp)
            .fillMaxSize()
            .drawBrightnessOverlay(brilloVentana.getBrillos())
            .background(color = colorVentanaConfiguracion.getColorVentana()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(onClick = {navController.navigate("pantallaColores")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
                    modifier = Modifier.offset(x = -140.dp, y = -30.dp))
                {
                    Text("<< Volver",modifier = Modifier.offset(x = 0.dp,y = 0.dp) ,color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                        style = TextStyle(fontSize = 18.sp))

                }
                Text("Cambiar Audio",modifier = Modifier.offset(x = 10.dp, y = -20.dp) ,color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 18.sp))

            }
            Text(text = "  Seleccine el Tono del pomodoro  ", color = Color.Black,
                modifier = Modifier
                    .offset(x = 6.dp)
                    .border(2.dp, color = Color.Gray),
                fontFamily = vt323FontFamily,
                style = TextStyle(fontSize = 25.sp, background = Color.LightGray),
            )
            crearBotonesCambioAudio()
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick ={ navController.navigate("PantallaPrincipal")},
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                border = BorderStroke(4.dp, Color.Gray)) {
                Text("Confirmar Audio", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp),)
            }

        }

    }


    @Composable
    fun PantallaCambioImagen(navController: NavHostController) {

        Column(modifier = Modifier
            .padding(top = 25.dp)
            .offset(x = 0.dp, y = -50.dp)
            .fillMaxSize()
            .drawBrightnessOverlay(brilloVentana.getBrillos())
            .background(color = colorVentanaConfiguracion.getColorVentana()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(onClick = {navController.navigate("pantallaColores")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black),
                    modifier = Modifier.offset(x = -140.dp, y = -30.dp))
                {
                    Text("<< Volver",modifier = Modifier.offset(x = 0.dp,y = 0.dp) ,color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                        style = TextStyle(fontSize = 18.sp))

                }
                Text("Cambiar Imagen",modifier = Modifier.offset(x = 10.dp, y = -20.dp) ,color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 18.sp))

            }
            Text(text = "  Seleccine Imagen del Pomodoro  ", color = Color.Black,
                modifier = Modifier
                    .offset(x = 6.dp)
                    .border(2.dp, color = Color.Gray),
                fontFamily = vt323FontFamily,
                style = TextStyle(fontSize = 25.sp, background = Color.LightGray),
            )
            crearBotonesCambioImagen()
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick ={ navController.navigate("PantallaPrincipal")},
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                border = BorderStroke(4.dp, Color.Gray)) {
                Text("Confirmar Imagen", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp),)
            }



        }

    }

    @Composable
    fun PantallaSeleccionColor(navController : NavHostController) {

        Column(modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxSize()
            .drawBrightnessOverlay(brilloVentana.getBrillos())
            .background(color = colorVentanaConfiguracion.getColorVentana()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                Button(onClick = {navController.navigate("pantallaConfiguracion")}, colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color.Transparent),
                    modifier = Modifier.offset(x = -15.dp, y = -60.dp)) {
                    Text("<< Configuración",modifier = Modifier.offset(y = 0.dp) ,color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                        style = TextStyle(fontSize = 18.sp))
                }
                Text(text = "Personalización", modifier = Modifier.offset(x = 140.dp, y = -50.dp), color = colorTexto.getColorTexto(),
                    fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 18.sp))
                Column(modifier = Modifier
                    .padding(8.dp)
                    .offset(x = 0.dp, y = -20.dp)) {
                    modificarBrilloAplicacion()
                    crearBotonModoOscuro()
                    Spacer(modifier = Modifier.size(30.dp))
                    crearBotonCambioImagen(navController)
                    Spacer(modifier = Modifier.size(30.dp))
                    crearBotonCambioTono(navController)
                }
            }

            Text(text = "  Color del fondo del pomodoro  ", color = Color.Black,
                modifier = Modifier
                    .offset(x = 6.dp)
                    .border(2.dp, color = Color.Gray),
                fontFamily = vt323FontFamily,
                style = TextStyle(fontSize = 25.sp, background = Color.LightGray),
            )
            Spacer(modifier = Modifier.size(30.dp))
            CrearBotonesColores { color ->
                colorVentana.setColorVentana(color)
            }

            Spacer(modifier = Modifier.size(30.dp))

            Button(onClick ={ navController.navigate("PantallaPrincipal")},
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                border = BorderStroke(4.dp, Color.Gray)) {
                Text("Confirmar Color", fontFamily = vt323FontFamily,
                    style = TextStyle(fontSize = 20.sp),)
            }

        }

    }

    @Composable
    fun PantallaConfiguracion(navController: NavHostController) {
        Column(
            modifier = Modifier
                .padding(top = 25.dp)
                .fillMaxSize()
                .drawBrightnessOverlay(brilloVentana.getBrillos())
                .background(color = colorVentanaConfiguracion.getColorVentana()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = { navController.navigate("pantallaPrincipal") },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.Black, containerColor = Color.Transparent),
                    modifier = Modifier.offset(x = -160.dp, y = -30.dp)
                ) {
                    Text("  << Volver", color = colorTexto.getColorTexto(), fontFamily = vt323FontFamily,
                        style = TextStyle(fontSize = 20.sp))
                }
                Text(
                    text = "Configuración",
                    modifier = Modifier.offset(x = 0.dp, y = -20.dp),
                    style = TextStyle(
                        fontFamily = vt323FontFamily,
                        fontSize = 20.sp,
                        color = colorTexto.getColorTexto()
                    )
                )
            }

            Spacer(modifier = Modifier.size(30.dp))

            Column(modifier = Modifier.padding(16.dp)) {
                ConfigSlider("Ciclos", timerPomodoro.cicloConteo,timerPomodoro::updateFocusCount, 1..12, 1)
                ConfigSlider("Estudio Time", timerPomodoro.estudioTime, timerPomodoro::updateFocusTime, 5..120, 5)
                ConfigSlider("Descanso Time", timerPomodoro.descansoTime, timerPomodoro::updateBreakTime, 5..60, 5)
                ConfigSlider("Descanso Largo Time", timerPomodoro.descansoLargoTime, timerPomodoro::updateLongBreakTime, 5..60, 5)
            }

            Spacer(modifier = Modifier.size(30.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    onClick = { navController.navigate("pantallaColores") },
                    modifier = Modifier
                        .offset(x = 10.dp, y = 0.dp)
                        .border(BorderStroke(4.dp, color = Color.Gray)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                    shape = RectangleShape
                ) {
                    Text("Personalización                   >>",
                        style = TextStyle(fontSize = 23.sp), fontFamily = vt323FontFamily)
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
                    CrearImagenTomate()
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
            composable("pantallaCambioImagen") {PantallaCambioImagen(navController)}
            composable("pantallaCambioAudio") {PantallaCambioAudio(navController)}
        }
    }

    fun Modifier.drawBrightnessOverlay(brightness: Float): Modifier = this.then(
        Modifier.graphicsLayer(alpha = brightness)
    )
}