package com.rahmat.belajar_ui.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahmat.belajar_ui.data.model.RegisterRequest
import com.rahmat.belajar_ui.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var passwordConfirmation by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var registrationSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onNameChange(newName: String) {
        name = newName
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onPasswordConfirmationChange(newPasswordConfirmation: String) {
        passwordConfirmation = newPasswordConfirmation
    }

    fun register() {
        if (password != passwordConfirmation) {
            errorMessage = "Passwords do not match"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            registrationSuccess = false

            try {
                val request = RegisterRequest(name, email, password, password_confirmation = password)
                val response = authRepository.register(request)

                if (response.isSuccessful) {
                    registrationSuccess = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage = "Registration failed: ${response.code()} - ${errorBody ?: "Unknown error"}"
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
        registrationSuccess = false
        errorMessage = null
    }
}
