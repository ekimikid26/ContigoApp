package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.proyectotesismovil.ui.theme.*
import com.example.proyectotesismovil.domain.model.Subscription
import com.example.proyectotesismovil.domain.model.SubscriptionPlan
import com.example.proyectotesismovil.ui.viewmodel.SubscriptionViewModel
import com.example.proyectotesismovil.ui.components.ContigoBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToForYou: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    currentRoute: String = "subscription"
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var showCancelDialog by remember { mutableStateOf(false) }

    // Cuando el usuario vuelve de Mercado Pago (Custom Tab) a la app,
    // refrescamos el estado de la suscripción: el webhook ya debió
    // haber actualizado el estado en Supabase mientras estaba fuera.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadMySubscription()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Membresía",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            ContigoBottomNavigationBar(
                currentRoute = currentRoute,
                onNavigateToHome = onNavigateToHome,
                onNavigateToForYou = onNavigateToForYou,
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToSubscription = { /* ya estamos aquí */ },
                onNavigateToProfile = onNavigateToProfile
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    text = "Elige el plan que mejor se adapte a ti",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            }

            // Suscripción activa o pendiente de confirmación
            uiState.currentSubscription?.let { sub ->
                if (sub.planId != "basico" || uiState.plans.none { it.id == "premium" }) {
                    item {
                        ActiveSubscriptionCard(
                            subscription = sub,
                            onCancel = { showCancelDialog = true }
                        )
                    }
                }
            }

            // Mensaje de error
            uiState.error?.let { error ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF3F3)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = error,
                            modifier = Modifier.padding(12.dp),
                            color = Color(0xFFB00020),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Mensaje de éxito
            uiState.successMessage?.let { msg ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF0FFF4)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SecondaryGreen
                            )
                            Text(
                                text = msg,
                                color = Color(0xFF2D6A4F),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Solo mostrar planes si no hay suscripción activa/pendiente, o si es el plan básico para permitir upgrade
            if (uiState.currentSubscription == null || uiState.currentSubscription?.planId == "basico") {
                item {
                    Text(
                        text = if (uiState.currentSubscription?.planId == "basico") "Mejora tu plan" else "Planes disponibles",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D2D2D)
                    )
                }

                items(uiState.plans.filter { plan -> 
                    // No mostrar el plan actual si es el básico
                    plan.id != uiState.currentSubscription?.planId
                }) { plan ->
                    PlanCard(
                        plan = plan,
                        isSelected = uiState.selectedPlan?.id == plan.id,
                        onSelect = { viewModel.selectPlan(plan) }
                    )
                }

                if (uiState.selectedPlan != null) {
                    item {
                        Button(
                            onClick = { viewModel.initiatePayment(context) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBlue
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !uiState.paymentInProgress
                        ) {
                            if (uiState.paymentInProgress) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.Default.CreditCard,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Pagar ${uiState.selectedPlan?.formattedPrice}",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "🔒 Pago seguro procesado por Mercado Pago.\n" +
                                   "Tarjeta, OXXO o transferencia, según " +
                                   "disponibilidad.",
                            fontSize = 12.sp,
                            color = Color(0xFF757575),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancelar suscripción") },
            text = {
                Text(
                    "¿Estás seguro de que deseas cancelar tu suscripción? " +
                    "Perderás el acceso a las funcionalidades de tu plan " +
                    "al final del período actual."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.cancelSubscription()
                        showCancelDialog = false
                    }
                ) {
                    Text("Sí, cancelar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Volver")
                }
            }
        )
    }
}

@Composable
fun ActiveSubscriptionCard(
    subscription: Subscription,
    onCancel: () -> Unit
) {
    val isPending = subscription.status == "pending"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPending)
                Color(0xFFFFF8E1)
            else SecondaryGreen.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isPending) Color(0xFFF9A825) else SecondaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    if (isPending)
                        "Autorización pendiente: ${subscription.planName}"
                    else
                        "Plan activo: ${subscription.planName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF2D2D2D)
                )
            }
            Spacer(Modifier.height(4.dp))
            if (isPending) {
                Text(
                    "Completa la autorización del cobro en Mercado " +
                    "Pago para activar tu suscripción.",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            } else {
                Text(
                    "Vigente hasta: ${
                        subscription.endDate?.take(10) ?: "N/A"
                    }",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            }
            if (!isPending) {
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, Color.Red.copy(alpha = 0.5f)
                    )
                ) {
                    Text("Cancelar suscripción", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun PlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val featuresMap = mapOf(
        "basico" to listOf(
            "✅ App móvil completa",
            "✅ Monitoreo de biomarcadores",
            "✅ Módulo de actividades TCC",
            "✅ Historial emocional",
        ),

        "premium" to listOf(
            "✅ Personalización de umbrales de alerta",
            "✅ Reportes exportables en PDF",
            "✅ Capacitación para equipo",
            "✅ Soporte técnico prioritario",
            "✅ Mantenimiento y actualizaciones prioritarias"
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) PrimaryBlue 
                        else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                PrimaryBlue.copy(alpha = 0.06f)
            else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 0.dp else 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = plan.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2D2D2D)
                    )
                    Text(
                        text = plan.formattedPrice,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
                if (isSelected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Seleccionado",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(8.dp))

            featuresMap[plan.id]?.forEach { feature ->
                Text(
                    text = feature,
                    fontSize = 13.sp,
                    color = Color(0xFF2D2D2D),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}
