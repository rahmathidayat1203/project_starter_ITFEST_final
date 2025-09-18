package com.rahmat.belajar_ui.data.remote

import com.rahmat.belajar_ui.data.model.LoginRequest
import com.rahmat.belajar_ui.data.model.LoginResponse
import com.rahmat.belajar_ui.data.model.RegisterRequest
import com.rahmat.belajar_ui.data.model.RegisterResponse
import com.rahmat.belajar_ui.data.model.TopUpRequest
import com.rahmat.belajar_ui.data.model.TransferRequest
import com.rahmat.belajar_ui.data.model.User
import com.rahmat.belajar_ui.data.model.Wallet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("logout")
    suspend fun logout(): Response<Unit>

    @GET("user")
    suspend fun getUser(): Response<User>

    @GET("wallet")
    suspend fun getWallet(): Response<Wallet>

    @POST("wallet/top-up")
    suspend fun topUp(@Body request: TopUpRequest): Response<Unit>

    @POST("wallet/transfer")
    suspend fun transfer(@Body request: TransferRequest): Response<Unit>
}