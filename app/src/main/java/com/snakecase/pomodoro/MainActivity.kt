package com.snakecase.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val aplicacion = AplicacionPomodoro()

        setContent {
            aplicacion.EjecutarAplicacion(savedInstanceState = savedInstanceState)

        }
    }
}