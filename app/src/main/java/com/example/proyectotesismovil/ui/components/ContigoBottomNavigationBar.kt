package com.example.proyectotesismovil.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun ContigoBottomNavigationBar(
    currentRoute: String,
    onNavigateToHome: () -> Unit,
    onNavigateToForYou: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val items = listOf(
        BottomNavItem("home", "Inicio", Icons.Default.Home),
        BottomNavItem("for_you", "Para Ti", Icons.Default.Favorite),
        BottomNavItem("history", "Historial", Icons.Default.BarChart),
        BottomNavItem("subscription", "Membresía", Icons.Default.CreditCard),
        BottomNavItem("patient_profile", "Perfil", Icons.Default.Person)
    )

    val actions = listOf(
        onNavigateToHome,
        onNavigateToForYou,
        onNavigateToHistory,
        onNavigateToSubscription,
        onNavigateToProfile
    )

    NavigationBar(containerColor = Color.White) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = actions[index],
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(item.label, fontSize = 10.sp)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6B9FD4),
                    selectedTextColor = Color(0xFF6B9FD4),
                    unselectedIconColor = Color(0xFF757575),
                    unselectedTextColor = Color(0xFF757575),
                    indicatorColor = Color(0xFF6B9FD4).copy(alpha = 0.1f)
                )
            )
        }
    }
}
