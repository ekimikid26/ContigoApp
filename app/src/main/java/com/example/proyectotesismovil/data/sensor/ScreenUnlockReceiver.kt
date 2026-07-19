package com.example.proyectotesismovil.data.sensor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScreenUnlockReceiver : BroadcastReceiver() {
    companion object {
        private var unlockCount = 0
        private var lastLoggedHour = -1
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_USER_PRESENT) {
            unlockCount++
            
            val calendar = java.util.Calendar.getInstance()
            val currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            
            if (lastLoggedHour != -1 && lastLoggedHour != currentHour) {
                logCount(context)
                unlockCount = 1
            }
            lastLoggedHour = currentHour
        }
    }

    private fun logCount(context: Context) {
        val count = unlockCount
        CoroutineScope(Dispatchers.IO).launch {
            val db = ContigoDatabase.getDatabase(context)
            db.biomarkerDao().insertBiometricReading(
                BiometricReadingEntity(
                    userId = "me",
                    screenUnlocks = count,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
