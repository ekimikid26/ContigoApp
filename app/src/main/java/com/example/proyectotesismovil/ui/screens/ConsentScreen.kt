package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary
import com.example.proyectotesismovil.security.SecurePreferences
import androidx.compose.ui.platform.LocalContext

@Composable
fun ConsentScreen(onAccepted: () -> Unit) {
    val context = LocalContext.current
    var showPrivacyPolicy by remember { mutableStateOf(false) }

    if (showPrivacyPolicy) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicy = false },
            title = { Text("Política de Privacidad") },
            text = {
                Box(modifier = Modifier.heightIn(max = 400.dp).verticalScroll(rememberScrollState())) {
                    Text(
                        text = "Contigo se compromete a proteger la privacidad de sus usuarios. " +
                                "Recopilamos datos biométricos (ritmo cardíaco) y estados emocionales para " +
                                "proporcionar apoyo personalizado. Tus datos están cifrados y nunca se " +
                                "venderán a terceros. Puedes retirar tu consentimiento en cualquier momento " +
                                "desde la configuración del perfil.\n\n" +
                                "[El texto completo será proporcionado por el usuario más adelante]",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyPolicy = false }) { Text("Cerrar") }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Antes de comenzar",
                style = MaterialTheme.typography.titleLarge
            )
            
            Text(
                text = "Queremos que sepas cómo cuidamos tu información",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            ConsentItem(
                icon = Icons.Default.Lock,
                text = "Tus datos son privados y están cifrados"
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ConsentItem(
                icon = Icons.Default.Favorite,
                text = "Solo usamos tu información para acompañarte mejor"
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ConsentItem(
                icon = Icons.Default.Person,
                text = "Tú decides qué compartir y cuándo"
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    SecurePreferences.saveBoolean(context, "informed_consent", true)
                    onAccepted()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(text = "Entendido, continuar")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(
                onClick = { showPrivacyPolicy = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Leer política de privacidad completa",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryBlue
                )
            }
        }
    }
}

@Composable
fun ConsentItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
