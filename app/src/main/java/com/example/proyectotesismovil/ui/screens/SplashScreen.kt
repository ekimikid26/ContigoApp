package com.example.proyectotesismovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectotesismovil.ui.theme.PrimaryBlue
import com.example.proyectotesismovil.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.proyectotesismovil.R

@Composable
fun SplashScreen(
    viewModel: AuthViewModel,
    onNavigate: (String) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        delay(2000)
        val user = currentUser
        if (user != null) {
            when (user.rol) {
                "paciente" -> onNavigate("home")
                "especialista" -> onNavigate("especialista_home")
                "administrador" -> onNavigate("admin_home")
                else -> onNavigate("login")
            }
        } else {
            onNavigate("login")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.logo_contigo),
                    contentDescription = "Logo de Contigo",
                    modifier = Modifier.size(120.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Contigo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }
    }
}
