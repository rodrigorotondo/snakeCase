package com.snakecase.pomodoro

import android.os.CountDownTimer

class ViewModel(private val pomodoro: Pomodoro) {
    private var contador = definirContador(pomodoro.obtenerTipoTimer())

    private fun definirContador(modoTimer: TipoTimer): CountDownTimer {
        when(modoTimer){
            TipoTimer.ESTUDIO -> {
                pomodoro.setearMinutos(25)
                return crearContador(25*60*1000)} //son 25 minutos en milisegundos (modo estudio)

            TipoTimer.DESCANSOCORTO -> {
                pomodoro.setearMinutos(5)
                return crearContador(5*60*1000 )}//son 5 minutos en milisegundos (modo descansoCorto

            else -> {
                pomodoro.setearMinutos(20)
                return crearContador(20*60*1000) }// son 20 minutos en milisegundos (modo descansoLargo)
        }
    }

    private fun crearContador(tiempoContador: Long): CountDownTimer {
        val tiempoUnSegundo = 1000L //el tiempo de un segundo en milisegundos
        val contador = object : CountDownTimer(tiempoContador,tiempoUnSegundo) {

            override fun onTick(millisUntilFinished: Long) {
                pomodoro.pasa1Segundo()
            }

            override fun onFinish() {
                if(!pomodoro.enPausa()) {
                    if (pomodoro.obtenerTipoTimer() == TipoTimer.ESTUDIO) {
                        pomodoro.incrementarCiclo()
                    }
                    pomodoro.actualizarTipoTimer()
                }
            }
        }
        return contador
    }

    fun empezar(){
        contador.start()
    }

    fun pausar(){
        pomodoro.pausar()
        contador.cancel()

    }

    fun reanudar(){
        contador = crearContador((pomodoro.obtenerMinutos()*60*1000 + pomodoro.obtenerSegundos()*1000).toLong())
    }

}