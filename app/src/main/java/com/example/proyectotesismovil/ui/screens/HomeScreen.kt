package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.components.AlertOverlay
import com.example.proyectotesismovil.ui.components.ContigoBottomNavigationBar
import com.example.proyectotesismovil.ui.components.PullToRefreshContainer
import com.example.proyectotesismovil.ui.theme.AccentLavender
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.SecondaryGreen
import com.example.proyectotesismovil.ui.theme.TextPrimary
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    userName: String,
    viewModel: HomeViewModel,
    isWatchConnected: Boolean = false,
    isDataCollectionPaused: Boolean = false,
    onNavigateToHome: () -> Unit,
    onNavigateToForYou: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToGad7: () -> Unit,
    currentRoute: String = "home"
) {
    var showAlert by remember { mutableStateOf(false) }
    var showWatchDialog by remember { mutableStateOf(false) }

    if (showWatchDialog) {
        AlertDialog(
            onDismissRequest = { showWatchDialog = false },
            title = { Text("Conectar Smartwatch") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp), color = PrimaryBlue)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Buscando dispositivos cercanos...", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Asegúrate de que tu reloj tenga el Bluetooth activado.", style = MaterialTheme.typography.bodySmall, color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            },
            confirmButton = {
                TextButton(onClick = { showWatchDialog = false }) { Text("Cancelar") }
            }
        )
    }

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Scaffold(
        bottomBar = {
            ContigoBottomNavigationBar(
                currentRoute = currentRoute,
                onNavigateToHome = onNavigateToHome,
                onNavigateToForYou = onNavigateToForYou,
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToSubscription = onNavigateToSubscription,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { padding ->
        PullToRefreshContainer(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() }
        ) {
            if (showAlert) {
                AlertOverlay(
                    onDismiss = { showAlert = false },
                    onSeeResources = { 
                        showAlert = false
                        onNavigateToForYou()
                    },
                    observations = listOf(
                        "Tu ritmo cardíaco ha sido inusualmente alto en la última hora.",
                        "Has tenido menos actividad física de lo normal hoy."
                    )
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            ) {
                // Calibration or GAD-7 Banner
                item {
                    val needsGad7 by viewModel.needsGad7.collectAsState()
                    
                    if (needsGad7) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                                .clickable { onNavigateToGad7() },
                            colors = CardDefaults.cardColors(
                                containerColor = SecondaryGreen.copy(alpha = 0.12f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📋", fontSize = 32.sp)
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text(
                                        "Tu cuestionario semanal",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        "Tómate 2 minutos · Toca para responder",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    } else {
                        // Calibration Banner
                        val currentDay = 3 
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = AccentLavender.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Estamos aprendiendo tus patrones. Día $currentDay de 7.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = currentDay / 7f,
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                    color = AccentLavender,
                                    trackColor = AccentLavender.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }
                }

                if (isDataCollectionPaused) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "La recopilación de datos está pausada. Puedes reactivarla en tu perfil.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }

                item {
                    if (userName == "Usuario") {
                        // Skeleton or loading state
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray.copy(alpha = 0.3f))
                        )
                    } else {
                        Text(
                            text = "Buenos días, $userName",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isWatchConnected) SecondaryGreen else Color.Red)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isWatchConnected) "Reloj conectado" else "Reloj desconectado",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isWatchConnected) TextSecondary else Color.Red
                            )
                        }
                        
                        if (!isWatchConnected) {
                            TextButton(onClick = { showWatchDialog = true }) {
                                Text("Conectar Reloj", style = MaterialTheme.typography.bodySmall, color = PrimaryBlue)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Person, null, tint = PrimaryBlue)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "¿Cómo te sientes hoy?", style = MaterialTheme.typography.titleMedium)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tómate un momento para conectar contigo mismo.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    InsightCard(
                        color = PrimaryBlue,
                        text = "Tu ritmo cardíaco ha estado muy estable hoy, ¡qué bien!"
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    InsightCard(
                        color = AccentLavender,
                        text = "Has descansado un poco mejor anoche comparado con ayer."
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Hidden button to trigger alert for demo purposes
                    Button(
                        onClick = { showAlert = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simular Alerta", color = TextSecondary.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
fun InsightCard(color: Color, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
