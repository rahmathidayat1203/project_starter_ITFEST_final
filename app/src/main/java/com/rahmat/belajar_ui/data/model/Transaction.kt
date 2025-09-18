package com.rahmat.belajar_ui.data.model

data class Transaction(
    val id: Int,
    val wallet_id: Int,
    val amount: Double,
    val type: String,
    val description: String,
    val created_at: String,
    val updated_at: String
)
