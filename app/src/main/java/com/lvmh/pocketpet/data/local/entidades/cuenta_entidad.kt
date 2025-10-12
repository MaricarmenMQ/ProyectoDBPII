package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "cuentas",
    indices = [
        Index("usuario_id"),
        Index("tipo_cuenta")
    ]
)
data class cuenta_entidad(
    @PrimaryKey
    val id: String = "",
    val usuario_id: String = "",
    val nombre: String = "",
    val tipo_cuenta: String = "", // "efectivo", "banco", "tarjeta", "ahorros"
    val balance: Double = 0.0,
    val icono: String = "💳",
    val color: String = "#2196F3",
    val es_principal: Boolean = false,
    val incluir_en_balance_total: Boolean = true,
    val banco: String = "",
    val numero_cuenta: String = "",
    val notas: String = "",
    val esta_activa: Boolean = true,
    val creado_en: Long = System.currentTimeMillis(),
    val actualizado_en: Long = System.currentTimeMillis()
)

object cuentas_predeterminadas {
    val CUENTA_EFECTIVO = cuenta_entidad(
        id = "cuenta_efectivo_default",
        usuario_id = "sistema",
        nombre = "Efectivo",
        tipo_cuenta = "efectivo",
        balance = 0.0,
        icono = "💵",
        color = "#4CAF50",
        es_principal = true
    )

    val CUENTA_BANCO = cuenta_entidad(
        id = "cuenta_banco_default",
        usuario_id = "sistema",
        nombre = "Banco",
        tipo_cuenta = "banco",
        balance = 0.0,
        icono = "🏦",
        color = "#2196F3",
        es_principal = false
    )

    val TODAS = listOf(CUENTA_EFECTIVO, CUENTA_BANCO)
}