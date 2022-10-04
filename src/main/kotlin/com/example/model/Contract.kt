package com.example.model

data class Contract(
    val id: String,
    val customerId: String,
    val amountE2: Long,
    val paid: Boolean
)
