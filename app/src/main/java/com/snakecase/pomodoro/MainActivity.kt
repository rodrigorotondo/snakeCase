package com.snakecase.pomodoro

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snakecase.pomodoro.ui.theme.PomodoroTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        val imageButtonPausa = findViewById<ImageButton>(R.id.Pausa)
        imageButtonPausa.setOnClickListener {
            Toast.makeText(this, "Pausando Pomodoro", Toast.LENGTH_SHORT).show()
        }
        val imageButtonIniciar = findViewById<ImageButton>(R.id.Iniciar)
        imageButtonIniciar.setOnClickListener {
            Toast.makeText(this, "Reanudando Pomodoro", Toast.LENGTH_SHORT).show()
        }
        val imageButtonDetener = findViewById<ImageButton>(R.id.Detener)
        imageButtonDetener.setOnClickListener {
            Toast.makeText(this, "Deteniendo Pomodoro", Toast.LENGTH_SHORT).show()
        }

    }
}