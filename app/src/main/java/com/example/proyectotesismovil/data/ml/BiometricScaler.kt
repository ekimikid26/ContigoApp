package com.example.proyectotesismovil.data.ml

import com.example.proyectotesismovil.data.local.entity.BiometricReadingEntity

/**
 * Normaliza biomarcadores con los mismos parámetros (mean/scale) del
 * StandardScaler usado al entrenar el Random Forest y al destilar la
 * red TFLite. Estos valores vienen de
 * acompanname-backend/ml/models/scaler_params.json, generado por
 * ml/distill_to_tflite.py. Si el modelo se reentrena, este archivo
 * debe regenerarse y estos valores deben actualizarse a mano.
 */
object BiometricScaler {

    // Orden fijo: debe coincidir con TFLiteManager y con el entrenamiento en Python.
    val FEATURE_ORDER = listOf(
        "heart_rate", "hrv", "spo2", "stress_level",
        "sleep_hours", "activity_level", "screen_unlocks", "app_usage_minutes"
    )

    private val MEAN = floatArrayOf(
        87.62960801209f, 36.17461216226258f, 96.49107655904251f, 5.266929752966564f,
        6.0433280809487675f, 0.3508527443648824f, 27.944f, 159.2705f
    )

    private val SCALE = floatArrayOf(
        16.766298514806792f, 16.468350966552848f, 1.4550975416731555f, 2.7476726721288793f,
        1.5918411987551488f, 0.23266452682888009f, 15.047852471366138f, 72.60183420375824f
    )

    /**
     * Convierte una lectura biométrica en el vector de 8 features
     * normalizado que espera el modelo TFLite. Los valores nulos se
     * imputan con la media de entrenamiento (equivalente a "sin desviación"
     * para esa feature), igual que se haría con datos faltantes en el
     * pipeline de Python.
     */
    fun normalize(reading: BiometricReadingEntity): FloatArray {
        val raw = floatArrayOf(
            reading.heartRate ?: MEAN[0],
            reading.hrv ?: MEAN[1],
            reading.spO2 ?: MEAN[2],
            reading.stressLevel ?: MEAN[3],
            reading.sleepHours ?: MEAN[4],
            reading.activityLevel ?: MEAN[5],
            (reading.screenUnlocks ?: MEAN[6].toInt()).toFloat(),
            (reading.appUsageMinutes ?: MEAN[7].toInt()).toFloat()
        )
        return FloatArray(raw.size) { i -> (raw[i] - MEAN[i]) / SCALE[i] }
    }

    /** Promedia varias lecturas y normaliza el resultado (útil para ventanas de tiempo). */
    fun normalizeAverage(readings: List<BiometricReadingEntity>): FloatArray {
        require(readings.isNotEmpty()) { "No se puede normalizar una lista vacía de lecturas" }
        val avgHr = readings.mapNotNull { it.heartRate }.average().takeIf { !it.isNaN() }?.toFloat()
        val avgHrv = readings.mapNotNull { it.hrv }.average().takeIf { !it.isNaN() }?.toFloat()
        val avgSpo2 = readings.mapNotNull { it.spO2 }.average().takeIf { !it.isNaN() }?.toFloat()
        val avgStress = readings.mapNotNull { it.stressLevel }.average().takeIf { !it.isNaN() }?.toFloat()
        val avgSleep = readings.mapNotNull { it.sleepHours }.average().takeIf { !it.isNaN() }?.toFloat()
        val avgActivity = readings.mapNotNull { it.activityLevel }.average().takeIf { !it.isNaN() }?.toFloat()
        val avgUnlocks = readings.mapNotNull { it.screenUnlocks }.average().takeIf { !it.isNaN() }?.toFloat()
        val avgUsage = readings.mapNotNull { it.appUsageMinutes }.average().takeIf { !it.isNaN() }?.toFloat()

        val raw = floatArrayOf(
            avgHr ?: MEAN[0], avgHrv ?: MEAN[1], avgSpo2 ?: MEAN[2], avgStress ?: MEAN[3],
            avgSleep ?: MEAN[4], avgActivity ?: MEAN[5], avgUnlocks ?: MEAN[6], avgUsage ?: MEAN[7]
        )
        return FloatArray(raw.size) { i -> (raw[i] - MEAN[i]) / SCALE[i] }
    }
}
