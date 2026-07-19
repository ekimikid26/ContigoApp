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
fun StretchingScreen(
    viewModel: GeneralActivityViewModel,
    voice: String = "femenina",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechHelper(context) }
    
    LaunchedEffect(Unit) {
        speechHelper.setVoice(voice)
        speechHelper.speak("Estiramiento Suave. Paso 1. Estira los brazos hacia arriba, como si quisieras tocar el techo.")
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

    var currentStretch by remember { mutableStateOf(1) }

    val stretchText = when (currentStretch) {
        1 -> "Estira los brazos hacia arriba, como si quisieras tocar el techo."
        2 -> "Mueve el cuello suavemente de un lado a otro en semicírculos."
        3 -> "Rota los hombros hacia atrás para liberar la tensión acumulada."
        4 -> "Gira el torso suavemente hacia la derecha y luego a la izquierda."
        else -> "Te has movido y estirado. Tu cuerpo te lo agradece."
    }

    Scaffold(
        topBar = {
            if (!isImmersive) {
                TopAppBar(
                    title = { Text("Estiramiento Suave") },
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
                    text = if (currentStretch <= 4) "Paso $currentStretch" else "✨",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = stretchText,
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
                        if (currentStretch < 5) {
                            currentStretch++
                            speechHelper.speak(when (currentStretch) {
                                2 -> "Mueve el cuello suavemente de un lado a otro en semicírculos."
                                3 -> "Rota los hombros hacia atrás para liberar la tensión acumulada."
                                4 -> "Gira el torso suavemente hacia la derecha y luego a la izquierda."
                                5 -> "Te has movido y estirado. Tu cuerpo te lo agradece."
                                else -> ""
                            })
                        } else {
                            viewModel.logActivity("Estiramiento", 180)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(if (currentStretch < 5) "Siguiente" else "Finalizar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
