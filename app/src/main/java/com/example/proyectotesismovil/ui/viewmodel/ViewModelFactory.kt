package com.example.proyectotesismovil.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectotesismovil.data.local.ContigoDatabase
import com.example.proyectotesismovil.data.local.TokenManager
import com.example.proyectotesismovil.data.remote.ApiClient
import com.example.proyectotesismovil.data.remote.api.*
import com.example.proyectotesismovil.data.repository.*
import com.example.proyectotesismovil.domain.repository.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    
    private val TAG = "ViewModelFactory"

    private val database: ContigoDatabase by lazy {
        try {
            ContigoDatabase.getDatabase(context)
        } catch (e: Exception) {
            Log.e(TAG, "Database initialization failed", e)
            throw e
        }
    }

    private val tokenManager: TokenManager by lazy {
        TokenManager(context)
    }

    private val retrofit by lazy {
        try {
            ApiClient.getRetrofit(tokenManager, context)
        } catch (e: Exception) {
            Log.e(TAG, "Retrofit initialization failed", e)
            throw e
        }
    }

    private val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    private val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
    private val vinculacionApi: VinculacionApi by lazy { retrofit.create(VinculacionApi::class.java) }
    private val notaApi: NotaApi by lazy { retrofit.create(NotaApi::class.java) }
    private val biometricApi: BiometricApi by lazy { retrofit.create(BiometricApi::class.java) }
    private val activityApi: ActivityApi by lazy { retrofit.create(ActivityApi::class.java) }
    private val emotionalApi: EmotionalStateApi by lazy { retrofit.create(EmotionalStateApi::class.java) }
    private val alertApi: AlertApi by lazy { retrofit.create(AlertApi::class.java) }
    private val calibrationApi: CalibrationApi by lazy { retrofit.create(CalibrationApi::class.java) }
    private val paymentApi: PaymentApi by lazy { retrofit.create(PaymentApi::class.java) }
    
    private val emotionalRepository: EmotionalActivityRepository by lazy {
        EmotionalActivityRepositoryImpl(
            activityApi,
            emotionalApi,
            database.activityLogDao(),
            database.reflectionDao(),
            database.emotionalStateDao(),
            database.gratitudeDao(),
        )
    }

    private val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            authApi,
            userApi,
            tokenManager,
            database,
            context
        )
    }

    private val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userApi)
    }

    private val vinculacionRepository: VinculacionRepository by lazy {
        VinculacionRepositoryImpl(vinculacionApi)
    }

    private val notaRepository: NotaRepository by lazy {
        NotaRepositoryImpl(notaApi)
    }

    private val biometricRepository by lazy {
        BiometricRepositoryImpl(biometricApi, database.biomarkerDao())
    }

    private val alertRepository: AlertRepository by lazy {
        AlertRepositoryImpl(alertApi)
    }

    private val calibrationRepository: CalibrationRepository by lazy {
        CalibrationRepositoryImpl(calibrationApi)
    }

    private val paymentRepository: PaymentRepositoryImpl by lazy {
        PaymentRepositoryImpl(paymentApi)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            when {
                modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                    AuthViewModel(authRepository) as T
                }
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(database.gad7Dao(), tokenManager) as T
                }
                modelClass.isAssignableFrom(ForYouViewModel::class.java) -> {
                    ForYouViewModel(emotionalRepository) as T
                }
                modelClass.isAssignableFrom(BreathingViewModel::class.java) -> {
                    BreathingViewModel(emotionalRepository) as T
                }
                modelClass.isAssignableFrom(ReflectionViewModel::class.java) -> {
                    ReflectionViewModel(emotionalRepository) as T
                }
                modelClass.isAssignableFrom(MusicViewModel::class.java) -> {
                    MusicViewModel(emotionalRepository) as T
                }
                modelClass.isAssignableFrom(GratitudeViewModel::class.java) -> {
                    GratitudeViewModel(emotionalRepository) as T
                }
                modelClass.isAssignableFrom(GeneralActivityViewModel::class.java) -> {
                    GeneralActivityViewModel(emotionalRepository) as T
                }
                modelClass.isAssignableFrom(PatientProfileViewModel::class.java) -> {
                    PatientProfileViewModel(authRepository, userRepository, vinculacionRepository, emotionalRepository, context) as T
                }
                modelClass.isAssignableFrom(EspecialistaViewModel::class.java) -> {
                    EspecialistaViewModel(authRepository, vinculacionRepository, notaRepository, alertRepository) as T
                }
                modelClass.isAssignableFrom(AdminViewModel::class.java) -> {
                    AdminViewModel(authRepository, userRepository, vinculacionRepository) as T
                }
                modelClass.isAssignableFrom(SubscriptionViewModel::class.java) -> {
                    SubscriptionViewModel(paymentRepository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating ViewModel ${modelClass.simpleName}", e)
            throw e
        }
    }
}
