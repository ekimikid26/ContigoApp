package com.example.proyectotesismovil.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectotesismovil.domain.model.Emotion
import com.example.proyectotesismovil.domain.model.EmotionalActivity
import com.example.proyectotesismovil.ui.components.ContigoBottomNavigationBar
import com.example.proyectotesismovil.ui.theme.*
import com.example.proyectotesismovil.ui.viewmodel.ForYouViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouScreen(
    viewModel: ForYouViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToActivity: (String) -> Unit,
    currentRoute: String = "for_you"
) {
    val selectedEmotion by viewModel.selectedEmotion.collectAsState()
    val activities by viewModel.activities.collectAsState()

    Scaffold(
        bottomBar = {
            ContigoBottomNavigationBar(
                currentRoute = currentRoute,
                onNavigateToHome = onNavigateToHome,
                onNavigateToForYou = { /* ya estamos aquí */ },
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToSubscription = onNavigateToSubscription,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Para ti",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "¿Cómo te sientes ahora mismo?",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(Emotion.values()) { emotion ->
                    EmotionChip(
                        emotion = emotion,
                        isSelected = selectedEmotion == emotion,
                        onClick = { viewModel.selectEmotion(emotion) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (selectedEmotion != null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(activities) { activity ->
                        ActivityCard(
                            activity = activity,
                            onClick = { onNavigateToActivity(activity.route) }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Selecciona una emoción para ver recomendaciones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun EmotionChip(
    emotion: Emotion,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryBlue else Color.White,
        label = "chipBackground"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else TextPrimary,
        label = "chipText"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        label = "chipScale"
    )

    Surface(
        modifier = Modifier
            .scale(scale)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)) else null,
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emotion.emoji, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = emotion.displayName,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ActivityCard(
    activity: EmotionalActivity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = getActivityIcon(activity.iconType),
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(32.dp)
                )
                
                if (activity.isRecommended) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd),
                        shape = RoundedCornerShape(8.dp),
                        color = SecondaryGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Recomendado",
                            color = SecondaryGreen,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            Column {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = activity.duration,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 3,
                lineHeight = 16.sp
            )
        }
    }
}

fun getActivityIcon(type: String): ImageVector {
    return when (type) {
        "breath" -> Icons.Default.Favorite
        "rest" -> Icons.Default.Star
        "music" -> Icons.Default.PlayArrow
        "reflection" -> Icons.Default.Edit
        "grounding" -> Icons.Default.Search
        "quick_move" -> Icons.Default.Check
        "release_writing" -> Icons.Default.Close
        "gratitude" -> Icons.Default.FavoriteBorder
        "priority" -> Icons.Default.List
        "stretching" -> Icons.Default.Face
        "visualization" -> Icons.Default.Home
        else -> Icons.Default.Info
    }
}
