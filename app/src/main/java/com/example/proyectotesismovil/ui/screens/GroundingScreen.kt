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
fun GroundingScreen(
    viewModel: GeneralActivityViewModel,
    voice: String = "femenina",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechHelper(context) }
    
    LaunchedEffect(Unit) {
        speechHelper.setVoice(voice)
        speechHelper.speak("Técnica 5-4-3-2-1. Observa 5 cosas que puedas ver a tu alrededor.")
    }
    
    DisposableEffect(Unit) {
        onDispose {
            speechHelper.shutdown()
        }
    }
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
    val isImmersive by viewModel.isImmersiveMode.collectAsState()

    var currentStep by remember { mutableStateOf(5) }
    
    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    val stepText = when (currentStep) {
        5 -> "Observa 5 cosas que puedas ver a tu alrededor."
        4 -> "Reconoce 4 cosas que puedas tocar ahora mismo."
        3 -> "Identifica 3 sonidos que escuches en la distancia."
        2 -> "Percibe 2 olores que puedas notar en el ambiente."
        1 -> "Nota 1 cosa que puedas saborear o una sensación en tu boca."
        else -> "¡Excelente! Has vuelto al aquí y al ahora."
    }

    Scaffold(
        topBar = {
            if (!isImmersive) {
                TopAppBar(
                    title = { Text("Técnica 5-4-3-2-1") },
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
                    text = if (currentStep > 0) currentStep.toString() else "✨",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = (MaterialTheme.typography.displayLarge.fontSize.value * fontSizeMultiplier * 1.5f).sp
                    ),
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
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
                        if (currentStep > 0) {
                            currentStep--
                            speechHelper.speak(when (currentStep) {
                                4 -> "Muy bien. Ahora reconoce 4 cosas que puedas tocar ahora mismo."
                                3 -> "Identifica 3 sonidos que escuches en la distancia."
                                2 -> "Percibe 2 olores que puedas notar en el ambiente."
                                1 -> "Nota 1 cosa que puedas saborear o una sensación en tu boca."
                                0 -> "¡Excelente! Has vuelto al aquí y al ahora."
                                else -> ""
                            })
                        } else {
                            viewModel.logActivity("Grounding", 120)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(if (currentStep > 0) "Siguiente" else "Finalizar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
