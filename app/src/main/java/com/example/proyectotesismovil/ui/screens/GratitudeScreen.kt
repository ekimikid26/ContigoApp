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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.components.ImmersiveModeToggle
import com.example.proyectotesismovil.ui.components.ImmersivePlayerBar
import com.example.proyectotesismovil.ui.theme.AccentLavender
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.GratitudeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GratitudeScreen(
    viewModel: GratitudeViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
    val isImmersive by viewModel.isImmersiveMode.collectAsState()

    var item1 by remember { mutableStateOf("") }
    var item2 by remember { mutableStateOf("") }
    var item3 by remember { mutableStateOf("") }
    
    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    Scaffold(
        topBar = {
            if (!isImmersive) {
                SmallTopAppBar(
                    title = { Text("Diario de Gratitud") },
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
                    text = "Hoy agradezco por...",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = (MaterialTheme.typography.headlineSmall.fontSize.value * fontSizeMultiplier).sp
                    ),
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                GratitudeField(item1, { item1 = it }, "Primera cosa", textColor)
                Spacer(modifier = Modifier.height(16.dp))
                GratitudeField(item2, { item2 = it }, "Segunda cosa", textColor)
                Spacer(modifier = Modifier.height(16.dp))
                GratitudeField(item3, { item3 = it }, "Tercera cosa", textColor)
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = { 
                        viewModel.saveGratitude(item1, item2, item3)
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    enabled = item1.isNotBlank() || item2.isNotBlank() || item3.isNotBlank()
                ) {
                    Text("Guardar y finalizar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun GratitudeField(value: String, onValueChange: (String) -> Unit, label: String, textColor: Color) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor
        )
    )
}
