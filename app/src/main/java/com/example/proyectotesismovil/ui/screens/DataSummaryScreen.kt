package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.PatientProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataSummaryScreen(
    viewModel: PatientProfileViewModel,
    onBack: () -> Unit
) {
    val logs by viewModel.activityLogs.collectAsState()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Resumen de mis datos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            item {
                Text(text = "Métricas generales", style = MaterialTheme.typography.titleMedium, color = PrimaryBlue)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard("Actividades", logs.size.toString(), Modifier.weight(1f))
                    StatCard("Reflexiones", "0", Modifier.weight(1f)) // Simplified
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(text = "Últimos registros", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            items(logs.take(10)) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    ListItem(
                        headlineContent = { Text(log.activityType) },
                        supportingContent = { Text("Duración: ${log.durationSeconds}s") },
                        trailingContent = { Text(java.text.SimpleDateFormat("dd/MM/yy").format(java.util.Date(log.completedAt))) }
                    )
                }
            }
            
            if (logs.isEmpty()) {
                item {
                    Text(text = "No hay registros aún.", color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodySmall)
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = PrimaryBlue)
        }
    }
}
