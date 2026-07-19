package com.example.proyectotesismovil.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.components.ImmersiveModeToggle
import com.example.proyectotesismovil.ui.theme.AccentLavender
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.MusicViewModel

data class Track(
    val id: Int,
    val title: String,
    val author: String,
    val resId: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    viewModel: MusicViewModel,
    onBack: () -> Unit
) {
    val isImmersive by viewModel.isImmersiveMode.collectAsState()
    
    val tracks = remember {
        listOf(
            Track(1, "Serenidad", "Contigo Audio"),
            Track(2, "Olas de Paz", "Naturaleza Real"),
            Track(3, "Enfoque Profundo", "Instrumental"),
            Track(4, "Descanso Estelar", "Sintetizador")
        )
    }
    
    var selectedTrack by remember { mutableStateOf(tracks[0]) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.3f) }

    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    Scaffold(
        topBar = {
            if (!isImmersive) {
                TopAppBar(
                    title = { Text("Música Relajante") },
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
                // Player Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isImmersive) 320.dp else 240.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.1f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isImmersive) 100.dp else 80.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MusicNote, null, tint = Color.White, modifier = Modifier.size(48.dp))
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = selectedTrack.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontSizeMultiplier).sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(text = selectedTrack.author, style = MaterialTheme.typography.bodyMedium, color = if (isImmersive) Color.LightGray else TextSecondary)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Controls
                Slider(
                    value = progress,
                    onValueChange = { progress = it },
                    colors = SliderDefaults.colors(thumbColor = PrimaryBlue, activeTrackColor = PrimaryBlue)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { isPlaying = !isPlaying }, modifier = Modifier.size(64.dp)) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                if (!isImmersive) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Explorar pistas",
                        modifier = Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(tracks) { track ->
                            TrackItem(
                                track = track,
                                isSelected = track == selectedTrack,
                                onClick = { selectedTrack = track }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrackItem(track: Track, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) PrimaryBlue.copy(alpha = 0.1f) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = if (isSelected) PrimaryBlue else Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = track.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(text = track.author, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}
