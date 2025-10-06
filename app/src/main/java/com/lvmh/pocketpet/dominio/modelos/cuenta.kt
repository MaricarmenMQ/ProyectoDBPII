package com.lvmh.pocketpet.dominio.modelos

data class cuenta (
    val id: Long = 0,
    val nombre: String,
    val balance: Double,
    val color: String,
    val esdefault: Boolean = false
)