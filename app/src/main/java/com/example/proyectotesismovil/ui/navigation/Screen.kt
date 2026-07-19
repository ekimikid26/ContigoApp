package com.example.proyectotesismovil.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Consent : Screen("consent")
    object Home : Screen("home")
    object ForYou : Screen("for_you")
    object History : Screen("history")
    object Gad7 : Screen("gad7")
    object Subscription : Screen("subscription")
    
    // Auth
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Permissions : Screen("permissions")
    
    // Patient Profile
    object PatientProfile : Screen("patient_profile")
    object DataSummary : Screen("data_summary")
    object DeleteRecords : Screen("delete_records")
    
    // Specialist
    object EspecialistaHome : Screen("especialista_home")
    object PacienteDetail : Screen("paciente_detail/{pacienteUid}") {
        fun createRoute(pacienteUid: String) = "paciente_detail/$pacienteUid"
    }
    object EspecialistaProfile : Screen("especialista_profile")
    
    // Admin
    object AdminHome : Screen("admin_home")
    
    // Activities
    object Breathing : Screen("breathing")
    object Rest : Screen("rest")
    object Music : Screen("music")
    object Reflection : Screen("reflection")
    object Grounding : Screen("grounding")
    object Visualization : Screen("visualization")
    object Gratitude : Screen("gratitude")
    object Priority : Screen("priority")
    object Stretching : Screen("stretching")
    object QuickMovement : Screen("quick_movement")
    object ReleaseWriting : Screen("release_writing")
}
