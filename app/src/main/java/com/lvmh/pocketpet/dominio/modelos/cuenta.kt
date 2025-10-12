package com.lvmh.pocketpet.dominio.modelos

data class cuenta(
    val id: String = "",
    val nombre: String,
    val tipoCuenta: TipoCuenta,
    val balance: Double,
    val icono: String = "💳",
    val color: String,
    val esPrincipal: Boolean = false,
    val incluirEnBalanceTotal: Boolean = true,
    val banco: String = "",
    val numeroCuenta: String = "",
    val notas: String = "",
    val estaActiva: Boolean = true
)

enum class TipoCuenta {
    EFECTIVO,
    BANCO,
    TARJETA,
    AHORROS,
    INVERSION;

    companion object {
        fun fromString(valor: String): TipoCuenta {
            return when (valor.lowercase()) {
                "efectivo" -> EFECTIVO
                "banco" -> BANCO
                "tarjeta" -> TARJETA
                "ahorros" -> AHORROS
                "inversion" -> INVERSION
                else -> EFECTIVO
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            EFECTIVO -> "efectivo"
            BANCO -> "banco"
            TARJETA -> "tarjeta"
            AHORROS -> "ahorros"
            INVERSION -> "inversion"
        }
    }
}