package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.ui.components.ContigoBottomNavigationBar
import com.example.proyectotesismovil.ui.components.PullToRefreshContainer
import com.example.proyectotesismovil.ui.viewmodel.PatientProfileViewModel

@Composable
fun PatientProfileScreen(
    viewModel: PatientProfileViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToForYou: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToAccess: () -> Unit,
    onNavigateToCancellation: () -> Unit,
    onLogout: () -> Unit,
    currentRoute: String = "patient_profile"
) {
    val user by viewModel.currentUser.collectAsState()
    val isConsentGiven by viewModel.isConsentGiven.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteOptions by remember { mutableStateOf(false) }
    var showFinalDeleteConfirm by remember { mutableStateOf(false) }
    var showDeactivateConfirm by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    var editNombre by remember { mutableStateOf("") }
    var editTelefono by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            editNombre = it.nombre
            editTelefono = it.telefono ?: ""
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar perfil") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editNombre,
                        onValueChange = { editNombre = it },
                        label = { Text("Nombre completo") }
                    )
                    OutlinedTextField(
                        value = editTelefono,
                        onValueChange = { editTelefono = it },
                        label = { Text("Número de celular") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateProfile(editNombre, editTelefono)
                    showEditDialog = false
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") }
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
                onNavigateToProfile = { /* ya estamos aquí */ }
            )
        }
    ) { padding ->
        PullToRefreshContainer(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            ) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user?.nombre?.firstOrNull()?.toString()?.uppercase() ?: "",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = user?.nombre ?: "Usuario", style = MaterialTheme.typography.titleLarge)
                        Text(text = user?.correo ?: "", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Text(text = "Edad: ${user?.edad ?: "--"} años", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(onClick = { showEditDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                            Text("Editar perfil")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(text = "Especialista asignado", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = user?.especialistaNombre ?: "Sin especialista asignado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    
                    if (user?.especialistaAsignado == null) {
                        var linkCode by remember { mutableStateOf("") }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = linkCode,
                            onValueChange = { if (it.length <= 6) linkCode = it },
                            label = { Text("Código de vinculación") },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Ej. 123456") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                        )
                        Button(
                            onClick = { viewModel.linkWithSpecialist(linkCode) },
                            modifier = Modifier.padding(top = 8.dp),
                            enabled = linkCode.length == 6,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Text("Vincular ahora")
                        }
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                    
                    Text(text = "Consentimiento de datos", style = MaterialTheme.typography.titleMedium)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(text = "Compartir datos con mi especialista", modifier = Modifier.weight(1f))
                        Switch(checked = isConsentGiven, onCheckedChange = { viewModel.updateConsent(it) })
                    }
                    Text(
                        text = "Tu especialista solo podrá ver tu historial emocional y de actividades. Tus reflexiones personales siempre serán privadas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                    
                    Text(text = "Derechos ARCO", style = MaterialTheme.typography.titleMedium, color = PrimaryBlue)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ArcoOption(
                        icon = Icons.Default.Info,
                        title = "Ver mis datos",
                        description = "Acceso a tu historial completo",
                        onClick = onNavigateToAccess
                    )
                    ArcoOption(
                        icon = Icons.Default.Edit,
                        title = "Corregir mis datos",
                        description = "Rectificación de información personal",
                        onClick = { showEditDialog = true }
                    )
                    ArcoOption(
                        icon = Icons.Default.Delete,
                        title = "Eliminar mis datos",
                        description = "Cancelación de registros o cuenta",
                        onClick = onNavigateToCancellation
                    )
                    ArcoOption(
                        icon = Icons.Default.Refresh,
                        title = "Pausar recopilación",
                        description = "Oposición al monitoreo automático",
                        onClick = { 
                            val current = user?.isDataCollectionPaused ?: false
                            viewModel.toggleDataCollection(!current)
                        },
                        trailing = {
                            Switch(
                                checked = user?.isDataCollectionPaused ?: false,
                                onCheckedChange = { viewModel.toggleDataCollection(it) }
                            )
                        }
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                    
                    Text(text = "Información Legal", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Tus datos son procesados conforme al aviso de privacidad de la aplicación, de acuerdo con la Ley Federal de Protección de Datos Personales en Posesión de los Particulares.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
                    
                    Button(
                        onClick = { showLogoutConfirm = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray.copy(alpha = 0.3f), contentColor = Color.Black),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Cerrar sesión")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TextButton(
                        onClick = { showDeleteOptions = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Eliminar o desactivar cuenta", color = Color.Red)
                    }
                }
            }
        }
    }

    if (showDeleteOptions) {
        AlertDialog(
            onDismissRequest = { showDeleteOptions = false },
            title = { Text("Gestionar cuenta") },
            text = { Text("¿Qué deseas hacer con tu cuenta? Desactivarla mantendrá tus datos guardados pero no podrás usar la app. Eliminarla borrará todo permanentemente.") },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showDeleteOptions = false; showDeactivateConfirm = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) { Text("Desactivar cuenta") }
                    
                    Button(
                        onClick = { showDeleteOptions = false; showFinalDeleteConfirm = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text("Eliminar definitivamente") }
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteOptions = false }) { Text("Cancelar") }
            }
        )
    }

    if (showDeactivateConfirm) {
        AlertDialog(
            onDismissRequest = { showDeactivateConfirm = false },
            title = { Text("Confirmar Desactivación") },
            text = { Text("Tu cuenta quedará inactiva. Podrás reactivarla contactando al administrador. ¿Continuar?") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.deactivateAccount()
                    showDeactivateConfirm = false 
                    onLogout()
                }) { Text("Desactivar", color = Color.Gray) }
            },
            dismissButton = {
                TextButton(onClick = { showDeactivateConfirm = false }) { Text("Cancelar") }
            }
        )
    }

    if (showFinalDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showFinalDeleteConfirm = false },
            title = { Text("⚠️ ELIMINACIÓN PERMANENTE") },
            text = { Text("¿Estás ABSOLUTAMENTE seguro? Se borrarán tus emociones, biométricos y perfil para siempre. Esta acción no tiene marcha atrás.") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.deleteAccountPermanently()
                    showFinalDeleteConfirm = false
                    onLogout()
                }) { Text("ELIMINAR TODO", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showFinalDeleteConfirm = false }) { Text("Cancelar") }
            }
        )
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = { viewModel.logout(); onLogout() }) { Text("Sí, cerrar") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun ArcoOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        trailing?.invoke()
    }
}
