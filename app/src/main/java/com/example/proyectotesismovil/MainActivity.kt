package com.example.proyectotesismovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.proyectotesismovil.ui.navigation.NavGraph
import com.example.proyectotesismovil.ui.theme.ProyectoTesisMovilTheme
import androidx.work.*
import com.example.proyectotesismovil.data.sensor.SyncBiometricsWorker
import com.example.proyectotesismovil.data.sensor.DailyCalibrationWorker
import com.example.proyectotesismovil.data.sensor.RiskCalculationWorker
import com.example.proyectotesismovil.data.notification.ContigoNotificationManager
import java.util.concurrent.TimeUnit
import android.util.Log

class MainActivity : ComponentActivity() {
    
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate started")
        super.onCreate(savedInstanceState)
        
        try {
            Log.d(TAG, "Initializing notification channels")
            ContigoNotificationManager.createNotificationChannels(this)
        } catch (e: Exception) {
            Log.e(TAG, "CRITICAL: Error creating notification channels", e)
        }

        try {
            Log.d(TAG, "Scheduling periodic workers")
            scheduleWorkers()
        } catch (e: Exception) {
            Log.e(TAG, "CRITICAL: Error scheduling workers", e)
        }

        setContent {
            Log.d(TAG, "Setting Compose content")
            ProyectoTesisMovilTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                    
                    intent?.getStringExtra("NAVIGATE_TO")?.let { dest ->
                        Log.d(TAG, "Navigating to deep link: $dest")
                        navController.navigate(dest)
                    }
                }
            }
        }
        Log.d(TAG, "onCreate finished")
    }

    private fun scheduleWorkers() {
        try {
            val workManager = WorkManager.getInstance(applicationContext)
            
            val syncRequest = PeriodicWorkRequestBuilder<SyncBiometricsWorker>(15, TimeUnit.MINUTES).build()
            workManager.enqueueUniquePeriodicWork("sync_biometrics", ExistingPeriodicWorkPolicy.KEEP, syncRequest)

            val calibrationRequest = PeriodicWorkRequestBuilder<DailyCalibrationWorker>(1, TimeUnit.DAYS).build()
            workManager.enqueueUniquePeriodicWork("daily_calibration", ExistingPeriodicWorkPolicy.KEEP, calibrationRequest)

            val riskRequest = PeriodicWorkRequestBuilder<RiskCalculationWorker>(30, TimeUnit.MINUTES).build()
            workManager.enqueueUniquePeriodicWork("risk_calculation", ExistingPeriodicWorkPolicy.KEEP, riskRequest)
            
            Log.d(TAG, "All workers scheduled successfully")
        } catch (e: Exception) {
            Log.e(TAG, "WorkManager initialization failed: ${e.message}", e)
        }
    }
}
