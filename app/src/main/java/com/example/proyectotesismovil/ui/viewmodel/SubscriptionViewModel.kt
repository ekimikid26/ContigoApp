package com.example.proyectotesismovil.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectotesismovil.data.repository.PaymentRepositoryImpl
import com.example.proyectotesismovil.domain.model.Subscription
import com.example.proyectotesismovil.domain.model.SubscriptionPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SubscriptionUiState(
    val isLoading: Boolean = false,
    val plans: List<SubscriptionPlan> = emptyList(),
    val currentSubscription: Subscription? = null,
    val selectedPlan: SubscriptionPlan? = null,
    val error: String? = null,
    val successMessage: String? = null,
    val paymentInProgress: Boolean = false
)

class SubscriptionViewModel(
    private val repository: PaymentRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubscriptionUiState())
    val uiState: StateFlow<SubscriptionUiState> = _uiState.asStateFlow()

    init {
        loadPlans()
        loadMySubscription()
    }

    fun loadPlans() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getPlans().fold(
                onSuccess = { plans ->
                    _uiState.value = _uiState.value.copy(
                        plans = plans,
                        isLoading = false
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            )
        }
    }

    fun loadMySubscription() {
        viewModelScope.launch {
            repository.getMySubscription().fold(
                onSuccess = { sub ->
                    _uiState.value = _uiState.value.copy(
                        currentSubscription = sub
                    )
                },
                onFailure = {}
            )
        }
    }

    fun selectPlan(plan: SubscriptionPlan) {
        _uiState.value = _uiState.value.copy(selectedPlan = plan)
    }

    /**
     * Crea la suscripción en el backend (Mercado Pago) y abre el
     * checkout (init_point) en una Chrome Custom Tab. El usuario
     * autoriza el cobro ahí; Mercado Pago notifica al backend por
     * webhook y este actualiza el estado en Supabase. Cuando el
     * usuario regresa a la app, refrescamos el estado con
     * loadMySubscription().
     */
    fun initiatePayment(context: Context) {
        val plan = _uiState.value.selectedPlan ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                paymentInProgress = true,
                error = null
            )

            repository.createSubscription(plan.id).fold(
                onSuccess = { initResponse ->
                    _uiState.value = _uiState.value.copy(
                        paymentInProgress = false
                    )
                    val customTabsIntent = CustomTabsIntent.Builder().build()
                    customTabsIntent.launchUrl(
                        context,
                        Uri.parse(initResponse.initPoint)
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message,
                        paymentInProgress = false
                    )
                }
            )
        }
    }

    fun cancelSubscription() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.cancelSubscription().fold(
                onSuccess = { message ->
                    _uiState.value = _uiState.value.copy(
                        successMessage = message,
                        currentSubscription = null,
                        isLoading = false
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            successMessage = null
        )
    }
}
