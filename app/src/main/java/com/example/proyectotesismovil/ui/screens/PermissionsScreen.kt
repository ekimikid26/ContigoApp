package com.example.proyectotesismovil.ui.screens

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.SecondaryGreen
import com.example.proyectotesismovil.ui.theme.TextSecondary

@Composable
fun PermissionsScreen(
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasUsageStats by remember { mutableStateOf(checkUsageStatsPermission(context)) }
    
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasUsageStats = checkUsageStatsPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            android.Manifest.permission.ACTIVITY_RECOGNITION,
            android.Manifest.permission.BODY_SENSORS
        )
    } else {
        emptyArray()
    }

    var permissionsState by remember {
        mutableStateOf(
            requiredPermissions.associateWith {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionsState = result
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Permisos necesarios",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Para ofrecerte una experiencia personalizada, necesitamos acceso a algunos sensores de tu dispositivo.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        PermissionItem(
            title = "Uso de aplicaciones",
            description = "Para entender tus patrones de actividad diaria.",
            isGranted = hasUsageStats,
            onClick = {
                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }
        )
        
        PermissionItem(
            title = "Sensores y Actividad",
            description = "Necesario para detectar reposo y actividad física.",
            isGranted = permissionsState.values.all { it },
            onClick = {
                launcher.launch(requiredPermissions)
            }
        )

        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = {
                val allPermissionsGranted = permissionsState.values.all { it }
                if (allPermissionsGranted) {
                    val serviceIntent = Intent(
                        context,
                        com.example.proyectotesismovil.data.sensor.BiometricForegroundService::class.java
                    )
                    context.startForegroundService(serviceIntent)
                    onContinue()
                } else {
                    // Opcional: Mostrar un Toast o mensaje indicando que faltan permisos
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text("Continuar")
        }
        
        TextButton(onClick = {
            onContinue()
        }) {
            Text("Omitir por ahora", color = TextSecondary)
        }
    }
}

@Composable
fun PermissionItem(title: String, description: String, isGranted: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isGranted) Icons.Default.CheckCircle else Icons.Default.Info,
                contentDescription = null,
                tint = if (isGranted) SecondaryGreen else Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}

fun checkUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}
