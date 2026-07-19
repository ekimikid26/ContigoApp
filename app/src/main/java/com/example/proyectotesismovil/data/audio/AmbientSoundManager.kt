package com.example.proyectotesismovil.data.audio

import android.content.Context
import android.media.MediaPlayer
import com.example.proyectotesismovil.R

object AmbientSoundManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackIndex = 0

    // List of ambient tracks: Name to Resource ID
    val tracks = listOf(
        "Lluvia suave" to R.raw.ambient_rain,
        "Bosque en calma" to R.raw.ambient_forest,
        "Olas del mar" to R.raw.ambient_waves,
        "Viento entre árboles" to R.raw.ambient_wind
    )

    fun play(context: Context, trackIndex: Int = currentTrackIndex) {
        try {
            stop()
            currentTrackIndex = trackIndex
            mediaPlayer = MediaPlayer.create(context, tracks[trackIndex].second)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun nextTrack(context: Context) {
        val nextIndex = (currentTrackIndex + 1) % tracks.size
        play(context, nextIndex)
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun getCurrentTrackName(): String = tracks[currentTrackIndex].first

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
}
