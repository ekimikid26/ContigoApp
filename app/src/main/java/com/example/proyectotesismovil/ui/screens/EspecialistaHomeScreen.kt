package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyectotesismovil.ui.components.PullToRefreshContainer
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.viewmodel.EspecialistaViewModel

@Composable
fun EspecialistaHomeScreen(
    viewModel: EspecialistaViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val patients by viewModel.patients.collectAsState()
    val invitations by viewModel.invitations.collectAsState()
    val alerts by viewModel.alerts.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    var showInviteDialog by remember { mutableStateOf(false) }
    var patientEmail by remember { mutableStateOf("") }
    var generatedCode by remember { mutableStateOf<String?>(null) }

    if (showInviteDialog) {
        AlertDialog(
            onDismissRequest = { showInviteDialog = false; generatedCode = null },
            title = { Text("Invitar Paciente") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (generatedCode == null) {
                        Text("Ingresa el correo del paciente para generar un código de vinculación.")
                        OutlinedTextField(
                            value = patientEmail,
                            onValueChange = { patientEmail = it },
                            label = { Text("Correo del paciente") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Comparte este código con tu paciente:", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = generatedCode ?: "",
                            style = MaterialTheme.typography.displayMedium,
                            color = PrimaryBlue,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            },
            confirmButton = {
                if (generatedCode == null) {
                    Button(onClick = {
                        viewModel.generateInvitation(patientEmail) { code ->
                            generatedCode = code
                        }
                    }) { Text("Generar Código") }
                } else {
                    TextButton(onClick = { showInviteDialog = false; generatedCode = null; patientEmail = "" }) { Text("Listo") }
                }
            },
            dismissButton = {
                if (generatedCode == null) {
                    TextButton(onClick = { showInviteDialog = false }) { Text("Cancelar") }
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(onClick = { showInviteDialog = true }, containerColor = PrimaryBlue) {
                    Icon(Icons.Default.Add, "Invitar", tint = Color.White)
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.List, "Mis Pacientes") },
                    label = { Text("Pacientes") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { 
                        BadgedBox(badge = { if (alerts.isNotEmpty()) Badge { Text(alerts.size.toString()) } }) {
                            Icon(Icons.Default.Notifications, "Alertas")
                        }
                    },
                    label = { Text("Alertas") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = onNavigateToProfile,
                    icon = { Icon(Icons.Default.Person, "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        PullToRefreshContainer(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() }
        ) {
            Column(modifier = Modifier.padding(padding).padding(24.dp)) {
                when (selectedTab) {
                    0 -> {
                        Text(text = "Mis Pacientes", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LazyColumn {
                            items(patients) { patient ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    onClick = { onNavigateToDetail(patient.uid) }
                                ) {
                                    ListItem(
                                        headlineContent = { Text(patient.nombre) },
                                        supportingContent = { Text("Último registro: Reciente") },
                                        trailingContent = {
                                            Button(onClick = { onNavigateToDetail(patient.uid) }) {
                                                Text("Ver detalle")
                                            }
                                        }
                                    )
                                }
                            }
                            
                            if (patients.isEmpty()) {
                                item {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Aún no tienes pacientes vinculados.",
                                            color = Color.Gray,
                                            style = MaterialTheme.typography.titleMedium,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        Text(text = "Alertas de Pacientes", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LazyColumn {
                            items(alerts) { alert ->
                                val patient = patients.find { it.id == alert.user_id }
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (alert.risk_level == "SEVERE") Color.Red.copy(alpha = 0.1f) else Color.White
                                    )
                                ) {
                                    ListItem(
                                        headlineContent = { Text("Alerta: ${alert.risk_level}") },
                                        supportingContent = { Text("Paciente: ${patient?.nombre ?: "ID: ${alert.user_id}"} · ${alert.generated_at}") },
                                        trailingContent = {
                                            if (patient != null) {
                                                TextButton(onClick = { onNavigateToDetail(patient.uid) }) {
                                                    Text("Ver Paciente")
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                            
                            if (alerts.isEmpty()) {
                                item {
                                    Text("No hay alertas registradas.", color = Color.Gray, modifier = Modifier.padding(vertical = 32.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
