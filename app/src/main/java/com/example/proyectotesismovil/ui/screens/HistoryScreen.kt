package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.theme.*
import com.example.proyectotesismovil.ui.state.ActivitySummary
import com.example.proyectotesismovil.ui.state.HistoryUiState
import com.example.proyectotesismovil.ui.components.ContigoBottomNavigationBar
import com.example.proyectotesismovil.ui.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToForYou: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToProfile: () -> Unit,
    currentRoute: String = "history"
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            ContigoBottomNavigationBar(
                currentRoute = currentRoute,
                onNavigateToHome = onNavigateToHome,
                onNavigateToForYou = onNavigateToForYou,
                onNavigateToHistory = { /* ya estamos aquí */ },
                onNavigateToSubscription = onNavigateToSubscription,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                Text(
                    text = "Tu historial",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D2D2D)
                )
                Text(
                    text = "Últimos 7 días",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            }

            item {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryBlue)
                        }
                    }
                    uiState.error != null -> {
                        ErrorCard(
                            message = uiState.error ?: "Error desconocido",
                            onRetry = { viewModel.retry() }
                        )
                    }
                    uiState.weeklyEmotions.all { it.emotionValue == 0f } -> {
                        EmptyStateCard()
                    }
                    else -> {
                        WeeklyEmotionPlaceholder()
                    }
                }
            }

            if (!uiState.isLoading && uiState.error == null) {
                item {
                    WeeklySummaryCards(uiState = uiState)
                }

                if (uiState.recentActivities.isNotEmpty()) {
                    item {
                        Text(
                            text = "Actividades recientes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D2D2D)
                        )
                    }

                    items(uiState.recentActivities) { activity ->
                        ActivityCard(activity = activity)
                    }
                }

                item {
                    MotivationalCard()
                }
            }
        }
    }
}

@Composable
fun WeeklyEmotionPlaceholder() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Gráfica Semanal", color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun WeeklySummaryCards(uiState: HistoryUiState) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                emoji = "📊",
                value = uiState.totalRegistrations.toString(),
                label = "Registros"
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                emoji = "📅",
                value = uiState.activeDays.toString(),
                label = "Días activos"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                emoji = "💙",
                value = uiState.mostFrequentEmotion?.replaceFirstChar { it.uppercase() } ?: "—",
                label = "Emoción frecuente"
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                emoji = "⭐",
                value = uiState.mostUsedActivity?.replaceFirstChar { it.uppercase() } ?: "—",
                label = "Actividad favorita"
            )
        }
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    emoji: String,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryBlue.copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF757575),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActivityCard(activity: ActivitySummary) {
    val dateFormat = SimpleDateFormat("dd MMM · HH:mm", Locale("es", "MX"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = activity.activityIcon,
                    fontSize = 22.sp
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.activityLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D2D2D)
                )
                Text(
                    text = dateFormat.format(Date(activity.completedAt)),
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
            }

            val minutes = activity.durationSeconds / 60
            val seconds = activity.durationSeconds % 60
            Text(
                text = if (minutes > 0) "${minutes}m" else "${seconds}s",
                fontSize = 13.sp,
                color = SecondaryGreen,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MotivationalCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AccentLavender.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "💜", fontSize = 28.sp)
            Column {
                Text(
                    text = "Cada registro cuenta",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D2D2D)
                )
                Text(
                    text = "Estás haciendo algo importante al cuidar de ti mismo cada día.",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryBlue.copy(alpha = 0.06f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "📊", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aún no hay registros esta semana",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D2D2D),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Indica cómo te sientes desde la pantalla 'Para Ti' para comenzar a ver tu historial aquí.",
                fontSize = 13.sp,
                color = Color(0xFF757575),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorCard(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3F3)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "😕", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color(0xFF757575),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Intentar de nuevo")
            }
        }
    }
}
