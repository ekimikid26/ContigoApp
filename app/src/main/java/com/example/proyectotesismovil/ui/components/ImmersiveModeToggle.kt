package com.example.proyectotesismovil.ui.components

import android.app.Activity
import android.view.WindowManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun ImmersiveModeToggle(
    isImmersive: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    IconButton(
        onClick = {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val controller = WindowCompat
                    .getInsetsController(window, view)
                if (!isImmersive) {
                    // ACTIVAR modo inmersivo
                    controller.hide(
                        WindowInsetsCompat.Type.systemBars()
                    )
                    controller.systemBarsBehavior =
                        WindowInsetsControllerCompat
                            .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    // Fondo de barras transparente
                    window.addFlags(
                        WindowManager.LayoutParams
                            .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    )
                    window.statusBarColor = 
                        android.graphics.Color.TRANSPARENT
                    window.navigationBarColor = 
                        android.graphics.Color.TRANSPARENT
                } else {
                    // DESACTIVAR modo inmersivo
                    controller.show(
                        WindowInsetsCompat.Type.systemBars()
                    )
                    window.statusBarColor = 
                        android.graphics.Color.WHITE
                    window.navigationBarColor = 
                        android.graphics.Color.WHITE
                }
            }
            onToggle()
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isImmersive)
                Icons.Default.FullscreenExit
            else Icons.Default.Fullscreen,
            contentDescription = if (isImmersive)
                "Salir de modo inmersivo"
            else "Activar modo inmersivo",
            tint = if (isImmersive) Color.White 
                   else Color(0xFF6B9FD4)
        )
    }
}
