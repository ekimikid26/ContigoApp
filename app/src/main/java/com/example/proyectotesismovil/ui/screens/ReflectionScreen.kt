package com.example.proyectotesismovil.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.data.local.entity.ReflectionEntity
import com.example.proyectotesismovil.ui.components.ImmersiveModeToggle
import com.example.proyectotesismovil.ui.components.ImmersivePlayerBar
import com.example.proyectotesismovil.ui.theme.AccentLavender
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.ReflectionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReflectionScreen(
    viewModel: ReflectionViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
    val isImmersive by viewModel.isImmersiveMode.collectAsState()
    val history by viewModel.latestReflections.collectAsState()

    var text by remember { mutableStateOf("") }
    
    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    Scaffold(
        topBar = {
            if (!isImmersive) {
                TopAppBar(
                    title = { Text("Reflexión Diaria") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Regresar")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.clearHistory() }) {
                            Icon(Icons.Default.Delete, "Limpiar")
                        }
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (isImmersive) 32.dp else padding.calculateTopPadding() + 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!isImmersive) {
                    item {
                        ImmersivePlayerBar(
                            isPlaying = isMusicPlaying,
                            onTogglePlay = { viewModel.toggleMusic(context) }
                        )
                    }
                }

                item {
                    Text(
                        text = "¿Qué hay en tu mente hoy?",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontSizeMultiplier).sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        placeholder = { Text("Escribe aquí tus pensamientos...") },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Button(
                            onClick = {
                                if (text.isNotBlank()) {
                                    viewModel.saveReflection(text)
                                    text = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(Icons.Default.Send, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardar")
                        }
                    }
                }

                if (!isImmersive) {
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        Text(
                            text = "Historial reciente",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(history) { reflection ->
                        ReflectionHistoryCard(reflection)
                    }
                }
            }
        }
    }
}

@Composable
fun ReflectionHistoryCard(reflection: ReflectionEntity) {
    val date = Date(reflection.createdAt)
    val format = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = reflection.text,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = format.format(date),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}
