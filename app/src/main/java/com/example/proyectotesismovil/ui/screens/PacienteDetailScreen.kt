package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.domain.model.User
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.viewmodel.EspecialistaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteDetailScreen(
    pacienteUid: String,
    viewModel: EspecialistaViewModel,
    onBack: () -> Unit
) {
    var patient by remember { mutableStateOf<User?>(null) }
    var hasConsent by remember { mutableStateOf(false) }

    LaunchedEffect(pacienteUid) {
        viewModel.getPatientDetails(pacienteUid) {
            patient = it
        }
        viewModel.observePatientConsent(pacienteUid).collect {
            hasConsent = it
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Paciente") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            if (!hasConsent) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Yellow.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Este paciente aún no ha autorizado compartir sus datos. Solo podrás ver su información básica.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = patient?.nombre ?: "Cargando...",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Edad: ${patient?.edad ?: "--"} años",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            var showHistorial by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showHistorial = !showHistorial },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PrimaryBlue.copy(alpha = 0.06f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "📋 Historial del paciente",
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                        )
                        Icon(
                            if (showHistorial) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    if (showHistorial) {
                        Spacer(Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Medicamentos: ${patient?.medicamentos ?: "--"}",
                            fontSize = 13.sp
                        )
                        Text(
                            "Alergias: ${patient?.alergias ?: "--"}",
                            fontSize = 13.sp
                        )
                        Text(
                            "Historial médico: ${patient?.historialMedico ?: "--"}",
                            fontSize = 13.sp
                        )
                        Text(
                            "Plan de tratamiento: ${patient?.planTratamiento ?: "--"}",
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            if (hasConsent) {
                Text(text = "Historial Emocional (Últimos 7 días)", style = MaterialTheme.typography.titleMedium)
                // Placeholder for Chart
                Box(modifier = Modifier.fillMaxWidth().height(150.dp).padding(vertical = 16.dp)) {
                    Text("Aquí se mostrará la gráfica de lineas...", color = Color.Gray)
                }
            }

            Text(text = "Notas clínicas", style = MaterialTheme.typography.titleMedium)
            var clinicalNote by remember { mutableStateOf("") }
            OutlinedTextField(
                value = clinicalNote,
                onValueChange = { clinicalNote = it },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                placeholder = { Text("Agregar nota clínica sobre el historial del paciente...") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    patient?.id?.let { viewModel.saveClinicalNote(it, clinicalNote) }
                    clinicalNote = ""
                }, 
                modifier = Modifier.fillMaxWidth(), 
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Guardar nota")
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            TextButton(
                onClick = { 
                    viewModel.unlinkPatient(pacienteUid)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Desvincular Paciente", color = Color.Red)
            }
        }
    }
}
