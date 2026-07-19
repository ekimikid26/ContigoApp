package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.domain.model.User
import com.example.proyectotesismovil.ui.components.PullToRefreshContainer
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.SecondaryGreen
import com.example.proyectotesismovil.ui.viewmodel.AdminViewModel

@Composable
fun AdminHomeScreen(
    viewModel: AdminViewModel,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val users by viewModel.users.collectAsState()
    val vinculaciones by viewModel.vinculaciones.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    var showEditUserDialog by remember { mutableStateOf<User?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf<User?>(null) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.List, "Usuarios") },
                    label = { Text("Usuarios") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Person, "Vinculaciones") },
                    label = { Text("Vínculos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { showLogoutConfirmDialog = true },
                    icon = { Icon(Icons.Default.Logout, "Cerrar sesión") },
                    label = { Text("Salir") }
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Gestión de Usuarios", style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(users) { user ->
                                ListItem(
                                    headlineContent = { Text(user.nombre) },
                                    supportingContent = {
                                        Text(
                                            text = user.rol.uppercase(),
                                            color = if (user.rol == "paciente") PrimaryBlue 
                                                    else SecondaryGreen,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    trailingContent = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = { showEditUserDialog = user }) {
                                                Icon(
                                                    Icons.Default.Visibility,
                                                    contentDescription = "Consultar/Editar",
                                                    tint = PrimaryBlue
                                                )
                                            }
                                            IconButton(onClick = { showDeleteConfirmDialog = user }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Eliminar",
                                                    tint = Color.Red
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                            
                            item {
                                Spacer(Modifier.height(24.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    "Resumen",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Spacer(Modifier.height(8.dp))
                                StatItem(
                                    "Pacientes registrados",
                                    users.count { it.rol == "paciente" }.toString()
                                )
                                StatItem(
                                    "Especialistas registrados",
                                    users.count { it.rol == "especialista" }.toString()
                                )
                                StatItem(
                                    "Usuarios activos",
                                    users.count { it.activo }.toString()
                                )
                                StatItem(
                                    "Vinculaciones activas",
                                    vinculaciones.count { it.activa }.toString()
                                )
                            }
                        }
                    }
                    1 -> {
                        var showVincularDialog by remember { 
                            mutableStateOf(false) 
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Vinculaciones activas",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Button(
                                onClick = { showVincularDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryBlue
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text("Nueva")
                            }
                        }
                        Spacer(Modifier.height(16.dp))

                        if (vinculaciones.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No hay vinculaciones activas",
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn {
                                items(vinculaciones) { v ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = 
                                                Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    "Paciente: ${v.paciente_id}",
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    "Especialista: ${v.especialista_id}",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                                Text(
                                                    "Consentimiento: ${
                                                        if (v.consentimiento_dado) 
                                                            "✅ Dado" else "⏳ Pendiente"
                                                    }",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            IconButton(
                                                onClick = { 
                                                    viewModel.desvincular(v.id) 
                                                }
                                            ) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Desvincular",
                                                    tint = Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (showVincularDialog) {
                            VincularDialog(
                                viewModel = viewModel,
                                onDismiss = { showVincularDialog = false }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEditUserDialog != null) {
        val user = showEditUserDialog!!
        var nombre by remember { mutableStateOf(user.nombre) }
        var correo by remember { mutableStateOf(user.correo) }
        var rol by remember { 
            mutableStateOf(user.rol) 
        }
        var activo by remember { mutableStateOf(user.activo) }
        var expandedRol by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showEditUserDialog = null },
            title = { Text("Consultar/Editar Usuario") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    @OptIn(ExperimentalMaterial3Api::class)
                    ExposedDropdownMenuBox(
                        expanded = expandedRol,
                        onExpandedChange = { expandedRol = it }
                    ) {
                        OutlinedTextField(
                            value = rol.replaceFirstChar { 
                                it.uppercase() 
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Rol") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults
                                    .TrailingIcon(expanded = expandedRol)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedRol,
                            onDismissRequest = { expandedRol = false }
                        ) {
                            listOf(
                                "paciente", 
                                "especialista"
                            ).forEach { option ->
                                DropdownMenuItem(
                                    text = { 
                                        Text(option.replaceFirstChar { 
                                            it.uppercase() 
                                        }) 
                                    },
                                    onClick = {
                                        rol = option
                                        expandedRol = false
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Estado: ${if (activo) "Activo" else "Inactivo"}")
                        Switch(
                            checked = activo,
                            onCheckedChange = { activo = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PrimaryBlue
                            )
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateUserFull(
                        user.uid, nombre, correo, rol, activo
                    )
                    showEditUserDialog = null
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showEditUserDialog = null 
                }) { Text("Cancelar") }
            }
        )
    }

    if (showDeleteConfirmDialog != null) {
        val user = showDeleteConfirmDialog!!
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text("Eliminar Usuario") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${user.nombre}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.deleteUser(user.id)
                    showDeleteConfirmDialog = null 
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = null }) { Text("Cancelar") }
            }
        )
    }

    if (showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas salir del panel de administración?") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.logout()
                    onLogout()
                }) { Text("Salir") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VincularDialog(
    viewModel: AdminViewModel,
    onDismiss: () -> Unit
) {
    val users by viewModel.users.collectAsState()
    val pacientes = users.filter { it.rol == "paciente" }
    val especialistas = users.filter { it.rol == "especialista" }
    
    var selectedPaciente by remember { mutableStateOf<User?>(null) }
    var selectedEspecialista by remember { 
        mutableStateOf<User?>(null) 
    }
    var expandedP by remember { mutableStateOf(false) }
    var expandedE by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Vinculación") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedP,
                    onExpandedChange = { expandedP = it }
                ) {
                    OutlinedTextField(
                        value = selectedPaciente?.nombre 
                            ?: "Seleccionar paciente",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Paciente") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults
                                .TrailingIcon(expanded = expandedP)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedP,
                        onDismissRequest = { expandedP = false }
                    ) {
                        pacientes.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.nombre) },
                                onClick = {
                                    selectedPaciente = p
                                    expandedP = false
                                }
                            )
                        }
                    }
                }
                ExposedDropdownMenuBox(
                    expanded = expandedE,
                    onExpandedChange = { expandedE = it }
                ) {
                    OutlinedTextField(
                        value = selectedEspecialista?.nombre 
                            ?: "Seleccionar especialista",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Especialista") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults
                                .TrailingIcon(expanded = expandedE)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedE,
                        onDismissRequest = { expandedE = false }
                    ) {
                        especialistas.forEach { e ->
                            DropdownMenuItem(
                                text = { Text(e.nombre) },
                                onClick = {
                                    selectedEspecialista = e
                                    expandedE = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val p = selectedPaciente
                    val e = selectedEspecialista
                    if (p != null && e != null) {
                        viewModel.vincular(p.uid, e.uid)
                        onDismiss()
                    }
                },
                enabled = selectedPaciente != null && 
                          selectedEspecialista != null
            ) { Text("Vincular") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun StatItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Bold, color = PrimaryBlue)
    }
}
