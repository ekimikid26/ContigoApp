package com.example.proyectotesismovil.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.components.ImmersiveModeToggle
import com.example.proyectotesismovil.ui.components.ImmersivePlayerBar
import com.example.proyectotesismovil.ui.theme.AccentLavender
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.SecondaryGreen
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.BreathingViewModel
import com.example.proyectotesismovil.data.audio.SpeechHelper
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingScreen(
    viewModel: BreathingViewModel,
    voice: String = "femenina",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechHelper(context) }
    
    LaunchedEffect(Unit) {
        speechHelper.setVoice(voice)
    }
    
    DisposableEffect(Unit) {
        onDispose {
            speechHelper.shutdown()
        }
    }
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
    val isImmersive by viewModel.isImmersiveMode.collectAsState()

    var isRunning by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(180) } // 3 minutes
    var currentPhase by remember { mutableStateOf("Inhala") }
    var phaseProgress by remember { mutableStateOf(0f) }
    var isFinished by remember { mutableStateOf(false) }

    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    val phaseColor by animateColorAsState(
        targetValue = when (currentPhase) {
            "Inhala" -> PrimaryBlue
            "Mantén" -> AccentLavender
            "Exhala" -> SecondaryGreen
            else -> PrimaryBlue
        },
        animationSpec = tween(durationMillis = 1000),
        label = "phaseColor"
    )

    val scale by animateFloatAsState(
        targetValue = when (currentPhase) {
            "Inhala" -> 1.2f
            "Mantén" -> 1.2f
            "Exhala" -> 0.8f
            else -> 1f
        },
        animationSpec = tween(
            durationMillis = when (currentPhase) {
                "Inhala" -> 4000
                "Mantén" -> 4000
                "Exhala" -> 6000
                else -> 1000
            },
            easing = LinearEasing
        ),
        label = "scale"
    )

    LaunchedEffect(isRunning) {
        if (isRunning && !isFinished) {
            while (timeRemaining > 0) {
                currentPhase = "Inhala"
                speechHelper.speak("Inhala profundamente")
                repeat(40) { 
                    if (!isRunning) return@LaunchedEffect
                    delay(100)
                    phaseProgress += 1/40f
                }
                phaseProgress = 0f
                
                currentPhase = "Mantén"
                speechHelper.speak("Mantén el aire")
                repeat(40) {
                    if (!isRunning) return@LaunchedEffect
                    delay(100)
                    phaseProgress += 1/40f
                }
                phaseProgress = 0f

                currentPhase = "Exhala"
                speechHelper.speak("Exhala lentamente")
                repeat(60) {
                    if (!isRunning) return@LaunchedEffect
                    delay(100)
                    phaseProgress += 1/60f
                }
                phaseProgress = 0f
            }
        } else {
            speechHelper.stop()
        }
    }

    LaunchedEffect(isRunning) {
        while (isRunning && timeRemaining > 0) {
            delay(1000)
            timeRemaining--
            if (timeRemaining == 0) {
                isFinished = true
                isRunning = false
                speechHelper.speak("Buen trabajo, has terminado tu sesión de respiración.")
                viewModel.onActivityComplete(180)
            }
        }
    }

    Scaffold(
        topBar = {
            if (!isImmersive) {
                SmallTopAppBar(
                    title = { Text("Respiración Guiada") },
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
                    .padding(if (isImmersive) 32.dp else padding.calculateTopPadding() + 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isImmersive) {
                    ImmersivePlayerBar(
                        isPlaying = isMusicPlaying,
                        onTogglePlay = { viewModel.toggleMusic(context) }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                Text(
                    text = if (isFinished) "¡Buen trabajo!" else currentPhase,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = (MaterialTheme.typography.headlineMedium.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = if (isFinished) SecondaryGreen else phaseColor,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(280.dp)) {
                    // Circular progress
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = phaseColor.copy(alpha = 0.1f),
                            radius = size.minDimension / 2 * scale
                        )
                        drawArc(
                            color = phaseColor,
                            startAngle = -90f,
                            sweepAngle = 360 * phaseProgress,
                            useCenter = false,
                            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formatTime(timeRemaining),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontSize = (MaterialTheme.typography.displayMedium.fontSize.value * fontSizeMultiplier).sp
                            ),
                            color = textColor
                        )
                        Text(
                            text = "tiempo restante",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isImmersive) Color.LightGray else TextSecondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(64.dp))
                
                if (!isFinished) {
                    Button(
                        onClick = { isRunning = !isRunning },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) Color.LightGray else PrimaryBlue)
                    ) {
                        Text(if (isRunning) "Pausar" else "Comenzar", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Finalizar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
