package com.example.proyectotesismovil.data.sensor

import android.app.usage.UsageStatsManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class SmartphoneSensorManager(private val context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val db = ContigoDatabase.getDatabase(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    private var lastActivityLogTime: Long = 0
    private var sleepStartTime: Long = 0
    private var lowMagnitudeCounter: Int = 0

    fun startListening() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val magnitude = sqrt((x * x + y * y + z * z).toDouble())

            processActivity(magnitude)
            processSleepEstimation(magnitude)
        }
    }

    private fun processActivity(magnitude: Double) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastActivityLogTime > 5 * 60 * 1000) {
            val level = when {
                magnitude < 10.5 -> 0f // Reposo
                magnitude < 13.0 -> 1f // Leve
                else -> 2f // Intensa
            }
            
            scope.launch {
                db.biomarkerDao().insertBiometricReading(
                    BiometricReadingEntity(
                        userId = "me",
                        activityLevel = level,
                        timestamp = currentTime
                    )
                )
            }
            lastActivityLogTime = currentTime
        }
    }

    private fun processSleepEstimation(magnitude: Double) {
        val calendar = java.util.Calendar.getInstance()
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        
        if (hour >= 22 || hour < 8) {
            if (magnitude < 10.2) {
                lowMagnitudeCounter++
                if (lowMagnitudeCounter >= 180) { // Approx 30 mins if called every 10s (but it's SENSOR_DELAY_NORMAL)
                    if (sleepStartTime == 0L) sleepStartTime = System.currentTimeMillis()
                }
            } else {
                lowMagnitudeCounter = 0
            }
        } else {
            if (sleepStartTime != 0L) {
                val durationHours = (System.currentTimeMillis() - sleepStartTime) / 3600000f
                scope.launch {
                    db.biomarkerDao().insertBiometricReading(
                        BiometricReadingEntity(
                            userId = "me",
                            sleepHours = durationHours,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
                sleepStartTime = 0
            }
        }
    }

    fun logAppUsage() {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 3600000 // Last hour
        
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
        if (stats != null) {
            var totalMinutes = 0
            for (usageStat in stats) {
                totalMinutes += (usageStat.totalTimeInForeground / 60000).toInt()
            }
            
            scope.launch {
                db.biomarkerDao().insertBiometricReading(
                    BiometricReadingEntity(
                        userId = "me",
                        appUsageMinutes = totalMinutes,
                        timestamp = endTime
                    )
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
