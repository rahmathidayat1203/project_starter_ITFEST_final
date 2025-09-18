package com.rahmat.belajar_ui.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahmat.belajar_ui.data.model.TopUpRequest
import com.rahmat.belajar_ui.data.model.TransferRequest
import com.rahmat.belajar_ui.data.model.User
import com.rahmat.belajar_ui.data.model.Wallet
import com.rahmat.belajar_ui.data.repository.AuthRepository
import com.rahmat.belajar_ui.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var user by mutableStateOf<User?>(null)
        private set

    var wallet by mutableStateOf<Wallet?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var topUpSuccess by mutableStateOf(false)
        private set

    var transferSuccess by mutableStateOf(false)
        private set

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val userResponse = authRepository.getUser()
                if (userResponse.isSuccessful) {
                    user = userResponse.body()
                }

                val walletResponse = authRepository.getWallet()
                if (walletResponse.isSuccessful) {
                    wallet = walletResponse.body()
                }
            } catch (e: Exception) {
                errorMessage = "Failed to fetch data: ${e.message}"
            }
            finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            sessionManager.clearAuthToken()
        }
    }

    fun topUp(amount: Double) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            topUpSuccess = false
            try {
                val response = authRepository.topUp(TopUpRequest(amount))
                if (response.isSuccessful) {
                    topUpSuccess = true
                    fetchData() // Refresh data
                } else {
                    errorMessage = "Top-up failed"
                }
            } catch (e: Exception) {
                errorMessage = "Top-up failed: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun transfer(email: String, amount: Double) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            transferSuccess = false
            try {
                val response = authRepository.transfer(TransferRequest(email, amount))
                if (response.isSuccessful) {
                    transferSuccess = true
                    fetchData() // Refresh data
                } else {
                    errorMessage = "Transfer failed"
                }
            } catch (e: Exception) {
                errorMessage = "Transfer failed: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        topUpSuccess = false
        transferSuccess = false
        errorMessage = null
    }
}
