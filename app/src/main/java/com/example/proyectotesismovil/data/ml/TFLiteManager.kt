package com.example.proyectotesismovil.data.ml

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * Carga contigo_model.tflite (red destilada a partir del Random Forest
 * entrenado en acompanname-backend/ml/train_random_forest.py; ver
 * ml/distill_to_tflite.py para el proceso de destilación y el acuerdo
 * medido entre ambos modelos) y corre inferencia sobre un vector de
 * 8 biomarcadores ya normalizados con BiometricScaler.
 *
 * Orden esperado del vector de entrada (coincide con
 * BiometricScaler.FEATURE_ORDER y con scaler_params.json):
 * [heart_rate, hrv, spo2, stress_level, sleep_hours, activity_level,
 *  screen_unlocks, app_usage_minutes]
 *
 * Salida: vector de 4 probabilidades [Minimo, Leve, Moderado, Severo].
 */
class TFLiteManager(context: Context, modelName: String = "contigo_model.tflite") {

    companion object {
        const val NUM_FEATURES = 8
        const val NUM_RISK_CLASSES = 4
    }

    private var interpreter: Interpreter? = null
    var isModelLoaded: Boolean = false
        private set

    init {
        try {
            interpreter = Interpreter(loadModelFile(context, modelName))
            isModelLoaded = true
        } catch (e: Exception) {
            // No se traga el error en silencio: si el modelo no carga,
            // el resto del pipeline necesita saberlo para hacer fallback.
            isModelLoaded = false
            e.printStackTrace()
        }
    }

    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * @param normalizedFeatures vector de tamaño NUM_FEATURES, ya normalizado
     * con BiometricScaler.normalize(). Lanza IllegalStateException si el
     * modelo no se pudo cargar; el llamador debe manejarlo con un fallback.
     * @return probabilidades por clase, tamaño NUM_RISK_CLASSES.
     */
    fun runInference(normalizedFeatures: FloatArray): FloatArray {
        check(isModelLoaded) { "TFLite model no cargado: revisar assets/contigo_model.tflite" }
        require(normalizedFeatures.size == NUM_FEATURES) {
            "Se esperaban $NUM_FEATURES features, llegaron ${normalizedFeatures.size}"
        }
        val input = arrayOf(normalizedFeatures)
        val output = Array(1) { FloatArray(NUM_RISK_CLASSES) }
        interpreter?.run(input, output)
        return output[0]
    }

    fun close() {
        interpreter?.close()
        interpreter = null
        isModelLoaded = false
    }
}
