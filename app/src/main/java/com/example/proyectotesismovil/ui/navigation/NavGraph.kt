package com.example.proyectotesismovil.ui.navigation

import android.content.Intent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.ui.screens.*
import com.example.proyectotesismovil.ui.viewmodel.*

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500)) },
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onContinue = { navController.navigate(Screen.Consent.route) }
            )
        }
        
        composable(Screen.Consent.route) {
            ConsentScreen(
                onAccepted = { 
                    navController.navigate(Screen.Permissions.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Splash.route) {
            val vm: AuthViewModel = viewModel(factory = factory)
            SplashScreen(viewModel = vm, onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        
        composable(Screen.Login.route) {
            val vm: AuthViewModel = viewModel(factory = factory)
            LoginScreen(
                viewModel = vm,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onLoginSuccess = { role ->
                    val dest = when (role) {
                        "paciente" -> Screen.Home.route
                        "especialista" -> Screen.EspecialistaHome.route
                        "administrador" -> Screen.AdminHome.route
                        else -> Screen.Home.route
                    }
                    navController.navigate(dest) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.ForgotPassword.route) {
            val vm: AuthViewModel = viewModel(factory = factory)
            ForgotPasswordScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        
        composable(Screen.Register.route) {
            val vm: AuthViewModel = viewModel(factory = factory)
            RegisterScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { role ->
                    val dest = if (role == "paciente") Screen.Consent.route else Screen.EspecialistaHome.route
                    navController.navigate(dest) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Permissions.route) {
            PermissionsScreen(
                onContinue = { navController.navigate(Screen.Home.route) }
            )
        }

        composable(Screen.Home.route) {
            val authVm: AuthViewModel = viewModel(factory = factory)
            val homeVm: HomeViewModel = viewModel(factory = factory)
            val user by authVm.currentUser.collectAsState()
            
            LaunchedEffect(Unit) {
                authVm.logoutEvent.collect {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            
            HomeScreen(
                userName = user?.nombre ?: "Usuario",
                viewModel = homeVm,
                isWatchConnected = false,
                isDataCollectionPaused = user?.isDataCollectionPaused ?: false,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToForYou = { navController.navigate(Screen.ForYou.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSubscription = { navController.navigate(Screen.Subscription.route) },
                onNavigateToProfile = { navController.navigate(Screen.PatientProfile.route) },
                onNavigateToGad7 = { navController.navigate(Screen.Gad7.route) }
            )
        }

        composable("gad7") {
            Gad7Screen(
                onComplete = {
                    navController.navigate("home") {
                        popUpTo("gad7") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.PatientProfile.route) {
            val authVm: AuthViewModel = viewModel(factory = factory)
            val vm: PatientProfileViewModel = viewModel(factory = factory)

            LaunchedEffect(Unit) {
                authVm.logoutEvent.collect {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            PatientProfileScreen(
                viewModel = vm,
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onNavigateToForYou = { navController.navigate(Screen.ForYou.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSubscription = { navController.navigate(Screen.Subscription.route) },
                onNavigateToAccess = { navController.navigate(Screen.DataSummary.route) },
                onNavigateToCancellation = { navController.navigate(Screen.DeleteRecords.route) },
                onLogout = { authVm.logout() }
            )
        }

        composable(Screen.Subscription.route) {
            val vm: SubscriptionViewModel = viewModel(factory = factory)
            SubscriptionScreen(
                viewModel = vm,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onNavigateToForYou = { 
                    navController.navigate(Screen.ForYou.route) 
                },
                onNavigateToHistory = { 
                    navController.navigate(Screen.History.route) 
                },
                onNavigateToProfile = { 
                    navController.navigate(Screen.PatientProfile.route) 
                },
                currentRoute = Screen.Subscription.route
            )
        }
        
        composable(Screen.DataSummary.route) {
            val vm: PatientProfileViewModel = viewModel(factory = factory)
            DataSummaryScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        
        composable(Screen.DeleteRecords.route) {
            val vm: PatientProfileViewModel = viewModel(factory = factory)
            DeleteRecordsScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Screen.EspecialistaHome.route) {
            val vm: EspecialistaViewModel = viewModel(factory = factory)
            EspecialistaHomeScreen(
                viewModel = vm,
                onNavigateToDetail = { uid -> navController.navigate(Screen.PacienteDetail.createRoute(uid)) },
                onNavigateToProfile = { navController.navigate(Screen.EspecialistaProfile.route) }
            )
        }
        
        composable(Screen.EspecialistaProfile.route) {
            val authVm: AuthViewModel = viewModel(factory = factory)
            val vm: EspecialistaViewModel = viewModel(factory = factory)
            
            LaunchedEffect(Unit) {
                authVm.logoutEvent.collect {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            EspecialistaProfileScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onLogout = { authVm.logout() },
                onNavigateToPacientes = { 
                    navController.navigate(Screen.EspecialistaHome.route) 
                },
                onNavigateToAlertas = { 
                    // No hay ruta de alertas aún, pero pasamos el callback
                }
            )
        }
        
        composable(Screen.PacienteDetail.route) { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("pacienteUid") ?: ""
            val vm: EspecialistaViewModel = viewModel(factory = factory)
            PacienteDetailScreen(pacienteUid = uid, viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable(Screen.AdminHome.route) {
            val authVm: AuthViewModel = viewModel(factory = factory)
            val vm: AdminViewModel = viewModel(factory = factory)

            LaunchedEffect(Unit) {
                authVm.logoutEvent.collect {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            AdminHomeScreen(
                viewModel = vm,
                onLogout = { authVm.logout() }
            )
        }

        composable(Screen.ForYou.route) {
            val vm: ForYouViewModel = viewModel(factory = factory)
            ForYouScreen(
                viewModel = vm,
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSubscription = { navController.navigate(Screen.Subscription.route) },
                onNavigateToProfile = { navController.navigate(Screen.PatientProfile.route) },
                onNavigateToActivity = { route -> 
                    // Si es una actividad con narración, ir primero a selección de voz
                    val voicedActivities = listOf(
                        Screen.Breathing.route,
                        Screen.Rest.route,
                        Screen.Stretching.route,
                        Screen.Visualization.route,
                        Screen.QuickMovement.route,
                        Screen.Grounding.route
                    )
                    if (voicedActivities.contains(route)) {
                        navController.navigate("voice_selection/$route")
                    } else {
                        navController.navigate(route)
                    }
                },
                currentRoute = Screen.ForYou.route
            )
        }

        composable("voice_selection/{targetRoute}") { backStackEntry ->
            val targetRoute = backStackEntry.arguments?.getString("targetRoute") ?: ""
            val title = when (targetRoute) {
                Screen.Breathing.route -> "Respiración"
                Screen.Rest.route -> "Descanso guiado"
                Screen.Stretching.route -> "Estiramientos suaves"
                Screen.Visualization.route -> "Visualización guiada"
                Screen.QuickMovement.route -> "Movimiento rápido"
                Screen.Grounding.route -> "Técnica 5-4-3-2-1"
                else -> "Actividad"
            }
            VoiceSelectionScreen(
                activityTitle = title,
                onVoiceSelected = { voice ->
                    navController.navigate("$targetRoute?voice=$voice") {
                        popUpTo("voice_selection/$targetRoute") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.History.route) {
            val authVm: AuthViewModel = viewModel(factory = factory)
            val user by authVm.currentUser.collectAsState()
            val db = ContigoDatabase.getDatabase(context)
            
            val historyVm: HistoryViewModel = viewModel(
                factory = HistoryViewModelFactory(
                    emotionalStateDao = db.emotionalStateDao(),
                    activityLogDao = db.activityLogDao(),
                    userId = user?.uid ?: ""
                )
            )
            HistoryScreen(
                viewModel = historyVm,
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onNavigateToForYou = { navController.navigate(Screen.ForYou.route) },
                onNavigateToSubscription = { navController.navigate(Screen.Subscription.route) },
                onNavigateToProfile = { navController.navigate(Screen.PatientProfile.route) },
                currentRoute = Screen.History.route
            )
        }

        composable(
            route = Screen.Breathing.route + "?voice={voice}",
            arguments = listOf(navArgument("voice") { defaultValue = "femenina"; type = NavType.StringType })
        ) { backStackEntry ->
            val voice = backStackEntry.arguments?.getString("voice") ?: "femenina"
            val vm: BreathingViewModel = viewModel(factory = factory)
            BreathingScreen(viewModel = vm, voice = voice, onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.Rest.route + "?voice={voice}",
            arguments = listOf(navArgument("voice") { defaultValue = "femenina"; type = NavType.StringType })
        ) { backStackEntry ->
            val voice = backStackEntry.arguments?.getString("voice") ?: "femenina"
            val vm: GeneralActivityViewModel = viewModel(factory = factory)
            RestScreen(viewModel = vm, voice = voice, onBack = { navController.popBackStack() })
        }
        composable(Screen.Music.route) {
            val vm: MusicViewModel = viewModel(factory = factory)
            MusicScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(Screen.Reflection.route) {
            val vm: ReflectionViewModel = viewModel(factory = factory)
            ReflectionScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.Grounding.route + "?voice={voice}",
            arguments = listOf(navArgument("voice") { defaultValue = "femenina"; type = NavType.StringType })
        ) { backStackEntry ->
            val voice = backStackEntry.arguments?.getString("voice") ?: "femenina"
            val vm: GeneralActivityViewModel = viewModel(factory = factory)
            GroundingScreen(viewModel = vm, voice = voice, onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.Visualization.route + "?voice={voice}",
            arguments = listOf(navArgument("voice") { defaultValue = "femenina"; type = NavType.StringType })
        ) { backStackEntry ->
            val voice = backStackEntry.arguments?.getString("voice") ?: "femenina"
            val vm: GeneralActivityViewModel = viewModel(factory = factory)
            VisualizationScreen(viewModel = vm, voice = voice, onBack = { navController.popBackStack() })
        }
        composable(Screen.Gratitude.route) {
            val vm: GratitudeViewModel = viewModel(factory = factory)
            GratitudeScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(Screen.Priority.route) {
            val vm: GeneralActivityViewModel = viewModel(factory = factory)
            PriorityScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.Stretching.route + "?voice={voice}",
            arguments = listOf(navArgument("voice") { defaultValue = "femenina"; type = NavType.StringType })
        ) { backStackEntry ->
            val voice = backStackEntry.arguments?.getString("voice") ?: "femenina"
            val vm: GeneralActivityViewModel = viewModel(factory = factory)
            StretchingScreen(viewModel = vm, voice = voice, onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.QuickMovement.route + "?voice={voice}",
            arguments = listOf(navArgument("voice") { defaultValue = "femenina"; type = NavType.StringType })
        ) { backStackEntry ->
            val voice = backStackEntry.arguments?.getString("voice") ?: "femenina"
            val vm: GeneralActivityViewModel = viewModel(factory = factory)
            QuickMovementScreen(viewModel = vm, voice = voice, onBack = { navController.popBackStack() })
        }
        composable(Screen.ReleaseWriting.route) {
            ReleaseWritingScreen(onBack = { navController.popBackStack() })
        }
    }
}
