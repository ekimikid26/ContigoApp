package com.example.proyectotesismovil.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.proyectotesismovil.MainActivity
import com.example.proyectotesismovil.R
import com.example.proyectotesismovil.domain.usecase.RiskLevel

object ContigoNotificationManager {

    private const val CHANNEL_ID_MONITORING = "contigo_monitoring"
    private const val CHANNEL_ID_RISK = "contigo_risk"
    private const val CHANNEL_ID_GAD7 = "contigo_gad7"
    private const val CHANNEL_ID_FOREGROUND = "contigo_foreground"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_FOREGROUND,
                    "Monitoreo activo",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Contigo está monitoreando tu bienestar"
                }
            )

            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_RISK,
                    "Observaciones de bienestar",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notificaciones de patrones de bienestar"
                }
            )

            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_GAD7,
                    "Recordatorio semanal",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Recordatorio para completar el cuestionario"
                }
            )
        }
    }

    fun showRiskNotification(context: Context, riskLevel: RiskLevel) {
        val (title, message) = when (riskLevel) {
            RiskLevel.MILD -> Pair(
                "Hemos notado algunos cambios",
                "Tus patrones de hoy son un poco diferentes a lo habitual. ¿Cómo te sientes?"
            )
            RiskLevel.MODERATE -> Pair(
                "Un momento para ti",
                "Parece que ha sido un día intenso. Te tenemos algunas actividades que pueden ayudarte."
            )
            RiskLevel.SEVERE -> Pair(
                "Estamos contigo",
                "Notamos que tus patrones de hoy se alejan bastante de tu línea habitual. Abre la app cuando puedas."
            )
            else -> return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("NAVIGATE_TO", "for_you")
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_RISK)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Using existing icon for now
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(riskLevel.ordinal, notification)
    }

    fun showGad7ReminderNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("NAVIGATE_TO", "gad7")
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 100, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_GAD7)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tu cuestionario semanal te espera")
            .setContentText("Tómate 2 minutos para responder el cuestionario de esta semana. Tu progreso nos importa.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(100, notification)
    }

    fun buildForegroundNotification(context: Context) =
        NotificationCompat.Builder(context, CHANNEL_ID_FOREGROUND)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Contigo")
            .setContentText("Monitoreando tu bienestar en segundo plano")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
}
