package com.snakecase.pomodoro

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build

class PomodoroService : Service() {

    private val binder = LocalBinder()
    private var timer: CountDownTimer? = null
    private var remainingTime: Long = 0

    inner class LocalBinder : Binder() {
        fun getService(): PomodoroService = this@PomodoroService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createInitialNotification())
    }

    private fun createInitialNotification(): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pomodoro_channel",
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "pomodoro_channel")
            .setContentTitle("Pomodoro Timer")
            .setContentText("Timer iniciado")
            .setSmallIcon(R.drawable.icono_app)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    fun startTimer(duration: Long) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateNotification()
            }

            override fun onFinish() {
                stopForeground(true)
            }
        }.start()
    }

    private fun updateNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pomodoro_channel",
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "pomodoro_channel")
            .setContentTitle("Pomodoro Timer")
            .setContentText("Tiempo restante: ${remainingTime / 1000} segundos")
            .setSmallIcon(R.drawable.icono_app)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
