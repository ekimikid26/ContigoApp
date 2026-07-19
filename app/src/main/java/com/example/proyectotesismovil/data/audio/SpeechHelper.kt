package com.example.proyectotesismovil.data.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import java.util.Locale

class SpeechHelper(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private var pendingVoice: String? = null

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "MX")
                isInitialized = true
                pendingVoice?.let { setVoice(it) }
            }
        }
    }

    fun setVoice(gender: String) {
        if (!isInitialized) {
            pendingVoice = gender
            return
        }

        val voices = tts?.voices ?: return
        val isFemale = gender.lowercase() == "femenina"

        // Buscar voz específica con coincidencia exacta
        val targetVoice = voices.find { voice ->
            val name = voice.name.lowercase()
            val isSpanish = name.contains("es-mx") || 
                            name.contains("es_mx") ||
                            name.contains("es-419") ||
                            name.contains("es_419")
            if (isFemale) {
                isSpanish && (
                    name.contains("-f-") ||
                    name.endsWith("-f") ||
                    name.contains("female") ||
                    name.contains("_f_") ||
                    (name.contains("mujer") || name.contains("woman"))
                ) && !name.contains("male").let { hasMale ->
                    // Excluir si "male" aparece pero NO como parte de "female"
                    hasMale && !name.contains("female")
                }
            } else {
                isSpanish && (
                    name.contains("-m-") ||
                    name.endsWith("-m") ||
                    (name.contains("male") && !name.contains("female")) ||
                    name.contains("_m_") ||
                    name.contains("hombre") || name.contains("man")
                )
            }
        }

        // Fallback: buscar por cualquier voz en español
        val fallbackVoice = targetVoice ?: voices.find { voice ->
            val name = voice.name.lowercase()
            (name.contains("es-mx") || name.contains("es_mx") ||
             name.contains("es-419") || name.contains("es_419")) &&
            if (isFemale) {
                voice.features?.contains("gender=female") == true ||
                name.contains("female") || name.contains("-f")
            } else {
                voice.features?.contains("gender=male") == true ||
                (!name.contains("female") && name.contains("male")) || 
                name.contains("-m")
            }
        }

        (fallbackVoice ?: targetVoice)?.let { tts?.voice = it }
    }

    fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
