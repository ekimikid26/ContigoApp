package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectotesismovil.R
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    var currentPage by remember { mutableStateOf(0) }
    
    val pages = listOf(
        OnboardingPage(
            "Bienvenido a Contigo",
            "Un espacio seguro diseñado para acompañarte en tu bienestar emocional cada día.",
            R.drawable.logo_contigo
        ),
        OnboardingPage(
            "Tu bienestar, monitoreado con cuidado",
            "Entendemos tus patrones de actividad de forma pasiva para brindarte apoyo justo cuando lo necesitas.",
            R.drawable.logo_contigo
        ),
        OnboardingPage(
            "Actividades para cada momento",
            "Desde ejercicios de respiración hasta música relajante, tenemos herramientas para ayudarte a calmarte.",
            R.drawable.logo_contigo
        ),
        OnboardingPage(
            "Seguimiento semanal con GAD-7",
            "Utilizamos cuestionarios clínicos estándar para ayudarte a visualizar tu progreso y compartirlo con tu especialista.",
            R.drawable.logo_contigo
        )
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = pages[currentPage].imageRes),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = pages[currentPage].title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = pages[currentPage].description,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(64.dp))
        
        Button(
            onClick = {
                if (currentPage < pages.size - 1) currentPage++
                else onContinue()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text(if (currentPage < pages.size - 1) "Siguiente" else "Comenzar", fontWeight = FontWeight.Bold)
        }
    }
}

data class OnboardingPage(val title: String, val description: String, val imageRes: Int)
