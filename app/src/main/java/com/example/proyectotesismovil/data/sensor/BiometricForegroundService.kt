package com.example.proyectotesismovil.data.sensor

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.proyectotesismovil.data.notification.ContigoNotificationManager
import kotlinx.coroutines.*

class BiometricForegroundService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var sensorManager: SmartphoneSensorManager
    private val NOTIFICATION_ID = 1001

    override fun onCreate() {
        super.onCreate()
        sensorManager = SmartphoneSensorManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = ContigoNotificationManager.buildForegroundNotification(this)
        startForeground(NOTIFICATION_ID, notification)
        
        sensorManager.startListening()
        
        serviceScope.launch {
            while (isActive) {
                delay(3600000) // Every hour
                sensorManager.logAppUsage()
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.stopListening()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
