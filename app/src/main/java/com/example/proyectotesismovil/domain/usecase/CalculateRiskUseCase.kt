package com.example.proyectotesismovil.domain.usecase

import com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity
import com.example.proyectotesismovil.data.ml.BiometricScaler
import com.example.proyectotesismovil.data.ml.TFLiteManager

/**
 * Nivel de riesgo GAD estimado a partir de biomarcadores fisiológicos y de
 * comportamiento. Aligned with GAD-7:
 * 0: Mínimo, 1: Leve, 2: Moderado, 3: Severo (índice de clase del modelo,
 * NO el puntaje GAD-7 directo — el mapeo a rango 0-21 se hace en la UI si
 * se necesita mostrar un puntaje).
 *
 * Usa el modelo TFLite real (destilado del Random Forest entrenado, ver
 * data/ml/TFLiteManager y acompanname-backend/ml/distill_to_tflite.py).
 * Si el modelo no está disponible (falta el archivo en assets, error de
 * carga, etc.), cae a una heurística simple basada en frecuencia cardiaca
 * y lo deja explícito con [RiskResult.usedFallback] en vez de fallar
 * en silencio.
 */
class CalculateRiskUseCase(private val tfLiteManager: TFLiteManager? = null) {

    data class RiskResult(
        val riskClass: Int,          // 0=Minimo, 1=Leve, 2=Moderado, 3=Severo
        val classProbabilities: FloatArray?, // null si se usó el fallback
        val usedFallback: Boolean
    )

    fun execute(biomarkers: List<BiometricReadingEntity>): RiskResult {
        if (biomarkers.isEmpty()) {
            return RiskResult(riskClass = 0, classProbabilities = null, usedFallback = true)
        }

        val manager = tfLiteManager
        if (manager != null && manager.isModelLoaded) {
            val normalized = BiometricScaler.normalizeAverage(biomarkers)
            val probs = manager.runInference(normalized)
            val predictedClass = probs.indices.maxByOrNull { probs[it] } ?: 0
            return RiskResult(riskClass = predictedClass, classProbabilities = probs, usedFallback = false)
        }

        // Fallback explícito: solo se usa si el modelo real no cargó.
        val avgHr = biomarkers.mapNotNull { it.heartRate }.takeIf { it.isNotEmpty() }?.average() ?: 0.0
        val fallbackClass = when {
            avgHr < 70 -> 0
            avgHr < 90 -> 1
            avgHr < 110 -> 2
            else -> 3
        }
        return RiskResult(riskClass = fallbackClass, classProbabilities = null, usedFallback = true)
    }
}
