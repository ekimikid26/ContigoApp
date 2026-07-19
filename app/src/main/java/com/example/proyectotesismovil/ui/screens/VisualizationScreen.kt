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
fun VisualizationScreen(
    viewModel: GeneralActivityViewModel,
    voice: String = "femenina",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechHelper(context) }
    
    LaunchedEffect(Unit) {
        speechHelper.setVoice(voice)
        speechHelper.speak("Visualización: Tu Lugar Seguro. Imagina que estás en un lugar donde te sientes completamente en paz. Puede ser una playa, un bosque o una habitación acogedora. Observa los colores, siente la temperatura del aire, escucha los sonidos suaves a tu alrededor. Quédate en este espacio el tiempo que necesites, respirando profundamente.")
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

    Scaffold(
        topBar = {
            if (!isImmersive) {
                TopAppBar(
                    title = { Text("Visualización") },
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
                    text = "Tu Lugar Seguro",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = (MaterialTheme.typography.headlineMedium.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Imagina que estás en un lugar donde te sientes completamente en paz. Puede ser una playa, un bosque o una habitación acogedora.\n\n" +
                           "Observa los colores, siente la temperatura del aire, escucha los sonidos suaves a tu alrededor.\n\n" +
                           "Quédate en este espacio el tiempo que necesites, respirando profundamente.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = (MaterialTheme.typography.bodyLarge.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = if (isImmersive) Color.LightGray else TextSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                Button(
                    onClick = { 
                        speechHelper.speak("Espero que este viaje mental te haya traído paz.")
                        viewModel.logActivity("Visualización", 300)
                        onBack()
                    },
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
