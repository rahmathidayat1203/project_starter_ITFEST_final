package com.rahmat.belajar_ui.data.model

data class Wallet(
    val id: Int,
    val user_id: Int,
    val balance: Double,
    val created_at: String,
    val updated_at: String,
    val transactions: List<Transaction>
)
