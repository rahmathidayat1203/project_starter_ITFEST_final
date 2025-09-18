package com.rahmat.belajar_ui.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahmat.belajar_ui.data.model.LoginRequest
import com.rahmat.belajar_ui.data.repository.AuthRepository
import com.rahmat.belajar_ui.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var loginSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun login() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            loginSuccess = false

            try {
                val request = LoginRequest(email, password)
                val response = authRepository.login(request)

                if (response.isSuccessful) {
                    response.body()?.access_token?.let { sessionManager.saveAuthToken(it) }
                    loginSuccess = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage = "Login failed: ${response.code()} - ${errorBody ?: "Unknown error"}"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.localizedMessage ?: "Unknown network issue"}"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        isLoading = false
        loginSuccess = false
        errorMessage = null
    }
}
