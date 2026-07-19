package com.example.proyectotesismovil.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.components.ImmersiveModeToggle
import com.example.proyectotesismovil.ui.components.ImmersivePlayerBar
import com.example.proyectotesismovil.ui.theme.AccentLavender
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.GeneralActivityViewModel

data class PriorityTask(val id: Int, val text: String, var isCompleted: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityScreen(
    viewModel: GeneralActivityViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()
    val isImmersive by viewModel.isImmersiveMode.collectAsState()
    
    val bgColor = if (isImmersive) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.background
    val textColor = if (isImmersive) Color.White else MaterialTheme.colorScheme.onBackground
    val fontSizeMultiplier = if (isImmersive) 1.15f else 1f

    var tasks by remember { mutableStateOf(listOf<PriorityTask>()) }
    var newTaskText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (!isImmersive) {
                TopAppBar(
                    title = { Text("Lista de Prioridades") },
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
                    text = "¿Qué es lo más importante hoy?",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontSizeMultiplier).sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Enfócate en máximo 3 cosas para liberar presión.",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isImmersive) Color.LightGray else TextSecondary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newTaskText,
                        onValueChange = { if (tasks.size < 3) newTaskText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Añadir prioridad...") },
                        enabled = tasks.size < 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (newTaskText.isNotBlank()) {
                                tasks = tasks + PriorityTask(tasks.size, newTaskText)
                                newTaskText = ""
                            }
                        },
                        enabled = newTaskText.isNotBlank() && tasks.size < 3
                    ) {
                        Icon(Icons.Default.Add, null, tint = PrimaryBlue)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            textColor = textColor,
                            onToggle = {
                                tasks = tasks.map { if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it }
                            },
                            onDelete = {
                                tasks = tasks.filter { it.id != task.id }
                            }
                        )
                    }
                }
                
                Button(
                    onClick = { 
                        viewModel.logActivity("Prioridades", 200)
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Finalizar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: PriorityTask, textColor: Color, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })
            Text(
                text = task.text,
                modifier = Modifier.weight(1f),
                color = textColor,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
        }
    }
}
