package com.example.proyectotesismovil.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.components.ImmersiveModeToggle
import com.example.proyectotesismovil.ui.components.ImmersivePlayerBar
import com.example.proyectotesismovil.ui.theme.AccentLavender
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.GeneralActivityViewModel
import com.example.proyectotesismovil.data.audio.SpeechHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickMovementScreen(
    viewModel: GeneralActivityViewModel,
    voice: String = "femenina",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechHelper(context) }
    
    LaunchedEffect(Unit) {
        speechHelper.setVoice(voice)
        speechHelper.speak("Movimiento Rápido. Acción 1. ¡Sácudete! Agita tus manos y pies con energía por unos segundos.")
    }
    
    DisposableEffect(Unit) {
        onDispose {
            speechHelper.shutdown()
        }
    }
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
    val isImmersive by viewModel.isImmersiveMode.collectAsState()
    
    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    var step by remember { mutableStateOf(1) }

    val stepText = when (step) {
        1 -> "¡Sácudete! Agita tus manos y pies con energía por unos segundos."
        2 -> "Salta un poco o camina rápidamente por la habitación."
        3 -> "Levanta las rodillas y mueve los brazos con fuerza."
        else -> "Has activado tu energía. ¡Sigue así!"
    }

    Scaffold(
        topBar = {
            if (!isImmersive) {
                TopAppBar(
                    title = { Text("Movimiento Rápido") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Regresar")
                        }
                    },
                    actions = {
                        ImmersiveModeToggle(
                            isImmersive = isImmersive,
                            onToggle = { viewModel.toggleImmersiveMode() }
                        )
                    }
                )
            }
        },
        containerColor = bgColor
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (isImmersive) {
                Box(modifier = Modifier.fillMaxSize().background(AccentLavender.copy(alpha = 0.25f)))
                ImmersiveModeToggle(
                    isImmersive = isImmersive,
                    onToggle = { viewModel.toggleImmersiveMode() },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (isImmersive) 32.dp else padding.calculateTopPadding() + 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!isImmersive) {
                    ImmersivePlayerBar(
                        isPlaying = isMusicPlaying,
                        onTogglePlay = { viewModel.toggleMusic(context) }
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                }

                Text(
                    text = if (step <= 3) "Acción $step" else "🏃‍♂️",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = stepText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = (MaterialTheme.typography.headlineSmall.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                Button(
                    onClick = { 
                        if (step < 4) {
                            step++
                            speechHelper.speak(when (step) {
                                2 -> "Bien. Ahora salta un poco o camina rápidamente por la habitación."
                                3 -> "Levanta las rodillas y mueve los brazos con fuerza."
                                4 -> "Has activado tu energía. ¡Sigue así!"
                                else -> ""
                            })
                        } else {
                            viewModel.logActivity("Movimiento", 60)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(if (step < 4) "Siguiente" else "Finalizar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
