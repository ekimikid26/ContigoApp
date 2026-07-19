package com.example.proyectotesismovil.data.sensor

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.data.ml.TFLiteManager
import com.example.proyectotesismovil.domain.usecase.CalculateRiskUseCase
import com.example.proyectotesismovil.domain.usecase.RiskCalculationEngine
import com.example.proyectotesismovil.data.remote.api.AlertApi
import com.example.proyectotesismovil.data.remote.api.AlertRequest
import com.example.proyectotesismovil.data.remote.ApiClient
import com.example.proyectotesismovil.data.local.TokenManager
import com.example.proyectotesismovil.data.notification.ContigoNotificationManager
import com.example.proyectotesismovil.domain.usecase.RiskLevel
import kotlinx.coroutines.flow.firstOrNull

/**
 * Worker periódico de cálculo de riesgo. Combina dos señales:
 *
 * 1. RiskCalculationEngine: motor estadístico basado en z-score contra la
 *    línea base personal del usuario (calibración). Es determinista y no
 *    depende de un modelo entrenado.
 * 2. CalculateRiskUseCase + TFLiteManager: modelo de ML (red destilada del
 *    Random Forest entrenado, ver data/ml/TFLiteManager). Antes de esta
 *    integración, este modelo existía en el proyecto pero ningún código
 *    lo invocaba en producción.
 *
 * Se usa el nivel de riesgo más alto entre ambas señales (enfoque
 * conservador: cualquiera de las dos puede disparar una alerta), y se
 * registra en logcat cuál señal fue la que decidió, para poder mostrar
 * evidencia real en el Capítulo IV de la tesis.
 */
class RiskCalculationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): androidx.work.ListenableWorker.Result {
        val tokenManager = TokenManager(applicationContext)
        val uid = tokenManager.getUid().firstOrNull() ?: return androidx.work.ListenableWorker.Result.failure()

        val db = ContigoDatabase.getDatabase(applicationContext)
        val calibrationDao = db.calibrationDao()
        val biometricDao = db.biomarkerDao()
        val engine = RiskCalculationEngine()

        val tfLiteManager = try {
            TFLiteManager(applicationContext)
        } catch (e: Exception) {
            null
        }
        val mlUseCase = CalculateRiskUseCase(tfLiteManager)

        val baseline = calibrationDao.getBaseline(uid)
        val recentReadings = biometricDao.getAllReadings().firstOrNull().orEmpty()
        val latestReading = recentReadings.firstOrNull()

        if (latestReading == null) {
            tfLiteManager?.close()
            return androidx.work.ListenableWorker.Result.success()
        }

        // Señal 1: estadística, contra línea base personal (si ya existe).
        val baselineRiskLevel = baseline?.let { engine.calculateRisk(latestReading, it) }

        // Señal 2: modelo entrenado (o fallback explícito si el modelo no cargó).
        val mlResult = mlUseCase.execute(recentReadings.take(10))
        val mlRiskLevel = when (mlResult.riskClass) {
            0 -> RiskLevel.NORMAL
            1 -> RiskLevel.MILD
            2 -> RiskLevel.MODERATE
            else -> RiskLevel.SEVERE
        }

        val ordinal = { level: RiskLevel -> level.ordinal }
        val finalRiskLevel = listOfNotNull(baselineRiskLevel, mlRiskLevel)
            .maxByOrNull(ordinal) ?: RiskLevel.NORMAL

        android.util.Log.d(
            "RiskCalculationWorker",
            "baseline=$baselineRiskLevel ml=$mlRiskLevel (fallback=${mlResult.usedFallback}, " +
                "probs=${mlResult.classProbabilities?.joinToString()}) -> final=$finalRiskLevel"
        )

        if (finalRiskLevel != RiskLevel.NORMAL) {
            ContigoNotificationManager.showRiskNotification(applicationContext, finalRiskLevel)

            val api = ApiClient.getRetrofit(tokenManager, applicationContext).create(AlertApi::class.java)
            try {
                api.saveAlert(AlertRequest(finalRiskLevel.name))
            } catch (e: Exception) {
                // Fallo de red no debe tumbar el worker; la alerta local ya se mostró.
            }
        }

        tfLiteManager?.close()
        return androidx.work.ListenableWorker.Result.success()
    }
}
