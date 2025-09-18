package com.rahmat.belajar_ui.data.repository

import com.rahmat.belajar_ui.data.model.LoginRequest
import com.rahmat.belajar_ui.data.model.LoginResponse
import com.rahmat.belajar_ui.data.model.RegisterRequest
import com.rahmat.belajar_ui.data.model.RegisterResponse
import com.rahmat.belajar_ui.data.model.TopUpRequest
import com.rahmat.belajar_ui.data.model.TransferRequest
import com.rahmat.belajar_ui.data.model.User
import com.rahmat.belajar_ui.data.model.Wallet
import com.rahmat.belajar_ui.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return apiService.login(request)
    }

    suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        return apiService.register(request)
    }

    suspend fun logout(): Response<Unit> {
        return apiService.logout()
    }

    suspend fun getUser(): Response<User> {
        return apiService.getUser()
    }

    suspend fun getWallet(): Response<Wallet> {
        return apiService.getWallet()
    }

    suspend fun topUp(request: TopUpRequest): Response<Unit> {
        return apiService.topUp(request)
    }

    suspend fun transfer(request: TransferRequest): Response<Unit> {
        return apiService.transfer(request)
    }
}