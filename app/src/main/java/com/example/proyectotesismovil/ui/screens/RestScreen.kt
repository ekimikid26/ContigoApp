package com.example.proyectotesismovil.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestScreen(
    viewModel: GeneralActivityViewModel,
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

    var seconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            seconds++
        }
    }

    Scaffold(
        topBar = {
            if (!isImmersive) {
                SmallTopAppBar(
                    title = { Text("Descanso") },
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
                    text = "Tómate un respiro",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = (MaterialTheme.typography.headlineMedium.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Cierra los ojos y deja que tu cuerpo se relaje por unos minutos.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = (MaterialTheme.typography.bodyLarge.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = if (isImmersive) Color.LightGray else TextSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                Text(
                    text = String.format("%02d:%02d", seconds / 60, seconds % 60),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = (MaterialTheme.typography.displayLarge.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = PrimaryBlue
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                Button(
                    onClick = { 
                        if (isRunning) {
                            speechHelper.speak("Has terminado tu descanso. Espero que te sientas mejor.")
                            viewModel.logActivity("Descanso", seconds)
                        } else {
                            speechHelper.speak("Cierra los ojos y deja que tu cuerpo se relaje por unos minutos.")
                        }
                        isRunning = !isRunning 
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(if (isRunning) "Terminar descanso" else "Empezar descanso", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
