package com.example.proyectotesismovil.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.SecondaryGreen
import com.example.proyectotesismovil.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Gad7Screen(
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    val questions = listOf(
        "Sentirse nervioso/a, ansioso/a o al límite",
        "No poder dejar de preocuparse o no poder controlar la preocupación",
        "Preocuparse demasiado por diferentes cosas",
        "Dificultad para relajarse",
        "Estar tan inquieto/a que es difícil permanecer sentado/a",
        "Irritarse o enojarse con facilidad",
        "Sentir miedo, como si algo terrible fuera a pasar"
    )
    
    val options = listOf("Ningún día", "Varios días", "Más de la mitad", "Casi todos")
    val answers = remember { mutableStateListOf(*Array(7) { -1 }) }
    var showResult by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cuestionario Semanal") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        if (!showResult) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Durante las últimas dos semanas, ¿con qué frecuencia te han molestado los siguientes problemas?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                questions.forEachIndexed { index, question ->
                    Text(
                        text = "${index + 1}. $question",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Column(modifier = Modifier.padding(vertical = 12.dp)) {
                        options.forEachIndexed { optIndex, option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = answers[index] == optIndex,
                                    onClick = { answers[index] = optIndex }
                                )
                                Text(text = option, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                }

                Button(
                    onClick = {
                        val score = answers.sum()
                        val level = when {
                            score <= 4 -> "Mínima"
                            score <= 9 -> "Leve"
                            score <= 14 -> "Moderada"
                            else -> "Severa"
                        }
                        showResult = true
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    enabled = answers.all { it != -1 }
                ) {
                    Text("Finalizar cuestionario", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            val score = answers.sum()
            ResultDisplay(score, onComplete)
        }
    }
}

@Composable
fun ResultDisplay(score: Int, onFinish: () -> Unit) {
    val message = when {
        score <= 4 -> "Tus respuestas de esta semana muestran un nivel de calma general."
        score <= 9 -> "Tus respuestas indican que esta semana fue un poco más desafiante de lo habitual."
        score <= 14 -> "Esta semana parece haber sido bastante intensa. Recuerda que tu especialista está ahí para apoyarte."
        else -> "Tus respuestas sugieren que has tenido una semana muy difícil. Te recomendamos hablar con tu especialista lo antes posible."
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("✨", fontSize = 48.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            "Cuestionario completado",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = TextSecondary
        )
        Spacer(Modifier.height(48.dp))
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Entendido", fontWeight = FontWeight.Bold)
        }
    }
}
