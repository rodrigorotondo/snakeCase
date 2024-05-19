package com.snakecase.pomodoro

data class Pomodoro(private var tipoTimer: TipoTimer) {

    private var ciclos = 0
    var minutosRestantes = tipoTimer.minutos
    var segundosRestantes = 0
    private var pausa = true

    var cicloConteo = 4
    var estudioTime = 25
    var descansoTime = 5
    var descansoLargoTime = 20

    fun obtenerTipoTimer(): TipoTimer {
        return tipoTimer
    }

    fun actualizarTipoTimer() {
        if (ciclos < 4) {
            if (tipoTimer == TipoTimer.ESTUDIO) {
                incrementarCiclo()
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
            segundosRestantes = 59
        }
        else{
            actualizarTipoTimer()
            minutosRestantes = tipoTimer.minutos
        }
    }

    fun pausar() {
        this.pausa = false
    }

    fun reanudar() {
        this.pausa = true

    }

    fun reiniciar(){
        this.segundosRestantes = 0
        this.minutosRestantes = tipoTimer.minutos
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
    fun updateFocusCount(nuevoConteo: Int) {
        if (nuevoConteo in 1..12) {
            cicloConteo = nuevoConteo
        }
    }
    fun updateFocusTime(nuevoTime: Int) {
        if (nuevoTime in 5..120) {
            estudioTime = nuevoTime
        }
    }

    fun updateBreakTime(nuevoTime: Int) {
        if (nuevoTime in 5..60) {
            descansoTime = nuevoTime
        }
    }
    fun updateLongBreakTime(nuevoTime: Int) {
        if (nuevoTime in 5..60) {
            descansoLargoTime = nuevoTime
        }
    }
}
