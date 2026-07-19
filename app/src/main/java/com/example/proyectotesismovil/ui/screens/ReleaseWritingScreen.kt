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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectotesismovil.ui.components.ImmersiveModeToggle
import com.example.proyectotesismovil.ui.components.ImmersivePlayerBar
import com.example.proyectotesismovil.ui.theme.AccentLavender
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.GeneralActivityViewModel
import com.example.proyectotesismovil.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReleaseWritingScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: GeneralActivityViewModel = viewModel(factory = ViewModelFactory(context))
    
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
    val isImmersive by viewModel.isImmersiveMode.collectAsState()

    var text by remember { mutableStateOf("") }
    
    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    Scaffold(
        topBar = {
            if (!isImmersive) {
                TopAppBar(
                    title = { Text("Escritura de Descarga") },
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
                    text = "Suelta todo en el papel digital",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontSizeMultiplier).sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Escribe sin filtros, sin juzgarte. Al salir, este texto no se guardará en ningún lado.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = if (isImmersive) Color.LightGray else TextSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    placeholder = { Text("Desahógate aquí...") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    )
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = { 
                        viewModel.logActivity("Escritura descarga", 300)
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Finalizar y soltar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
