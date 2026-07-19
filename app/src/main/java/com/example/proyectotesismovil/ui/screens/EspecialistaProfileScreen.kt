package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.viewmodel.EspecialistaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EspecialistaProfileScreen(
    viewModel: EspecialistaViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToPacientes: () -> Unit,
    onNavigateToAlertas: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showEditDialog) {
        var editNombre by remember { mutableStateOf(user?.nombre ?: "") }
        var editInstitucion by remember { mutableStateOf(user?.institucionActual ?: "") }
        
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Perfil") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = editNombre, onValueChange = { editNombre = it }, label = { Text("Nombre completo") })
                    OutlinedTextField(value = editInstitucion, onValueChange = { editInstitucion = it }, label = { Text("Institución actual") })
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.updateProfile(editNombre, editInstitucion)
                    showEditDialog = false 
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Eliminar Cuenta") },
            text = { Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción es irreversible y se perderán todos tus datos.") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.deleteAccount()
                    onLogout()
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil Profesional") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToPacientes,
                    icon = { Icon(Icons.Default.Group, "Pacientes") },
                    label = { Text("Pacientes") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToAlertas,
                    icon = { Icon(Icons.Default.Notifications, "Alertas") },
                    label = { Text("Alertas") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Person, "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            Text(text = "Información del Especialista", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))
            
            ProfileField(label = "Nombre completo", value = user?.nombre ?: "Cargando...")
            ProfileField(label = "Correo electrónico", value = user?.correo ?: "Cargando...")
            ProfileField(label = "Cédula profesional", value = user?.cedulaProfesional ?: "--")
            ProfileField(label = "Cédula especialidad", value = user?.cedulaEspecialidad ?: "--")
            ProfileField(label = "Especialidad", value = user?.tipoEspecialidad ?: "--")
            ProfileField(label = "Institución", value = user?.institucionActual ?: "--")
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = { viewModel.logout(); onLogout() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Cerrar sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(
                onClick = { showDeleteConfirm = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Eliminar cuenta", color = Color.Red)
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
