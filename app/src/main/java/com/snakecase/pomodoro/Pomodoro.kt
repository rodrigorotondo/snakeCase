package com.snakecase.pomodoro

data class Pomodoro(private var tipoTimer: TipoTimer) {
    private var ciclos = 0
    private var minutosRestantes = 0
    private var segundosRestantes = 0
    private var pausa = false

    fun obtenerTipoTimer(): TipoTimer {
        return tipoTimer
    }

    fun actualizarTipoTimer() {
        if (ciclos < 4) {
            if (tipoTimer == TipoTimer.ESTUDIO) {
                tipoTimer = TipoTimer.DESCANSOCORTO
            } else {
                tipoTimer = TipoTimer.ESTUDIO

            }
        } else {
            tipoTimer = TipoTimer.DESCANSOLARGO
            ciclos = 0
        }
    }

    fun incrementarCiclo() {
        ciclos = ciclos + 1
    }


    fun pasa1Segundo() {
        if (segundosRestantes > 0) {
            segundosRestantes = segundosRestantes - 1
        } else if (minutosRestantes > 0) {
            minutosRestantes = minutosRestantes - 1
        } else {

        }
    }

    fun pausar() {
        this.pausa = false
    }

    fun reanudar() {
        this.pausa = true

    }

    fun enPausa(): Boolean {
        return pausa
    }

    fun obtenerMinutos(): Int {
        return this.minutosRestantes
    }

    fun obtenerSegundos(): Int {
        return this.segundosRestantes
    }

    fun setearMinutos(minutos: Int) {
        this.minutosRestantes = minutos

    }
}
