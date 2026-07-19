package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.viewmodel.PatientProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteRecordsScreen(
    viewModel: PatientProfileViewModel,
    onBack: () -> Unit
) {
    val logs by viewModel.activityLogs.collectAsState()
    var showAllDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Eliminar registros") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Button(
                onClick = { showAllDeleteConfirm = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminar todos mis datos")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(text = "Registros individuales", style = MaterialTheme.typography.titleMedium)
            
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(logs) { log ->
                    ListItem(
                        headlineContent = { Text(log.activityType) },
                        supportingContent = { Text(java.text.SimpleDateFormat("dd/MM/yy").format(java.util.Date(log.completedAt))) },
                        trailingContent = {
                            IconButton(onClick = { /* Delete single log */ }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAllDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showAllDeleteConfirm = false },
            title = { Text("Eliminar todo") },
            text = { Text("Esta acción eliminará permanentemente todo tu historial. No podrá deshacerse.") },
            confirmButton = {
                TextButton(onClick = { 
                    // viewModel.deleteAllData()
                    showAllDeleteConfirm = false 
                }) { Text("Eliminar todo", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showAllDeleteConfirm = false }) { Text("Cancelar") }
            }
        )
    }
}
