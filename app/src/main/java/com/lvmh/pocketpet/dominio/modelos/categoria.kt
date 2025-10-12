package com.lvmh.pocketpet.dominio.modelos

data class categoria(
    val id: String = "",
    val nombre: String,
    val descripcion: String = "",
    val icono: String,
    val color: String,
    val tipo: TipoCategoria,
    val esPredeterminada: Boolean = false,
    val orden: Int = 0,
    val estaActiva: Boolean = true
)

enum class TipoCategoria {
    INGRESO,
    GASTO;

    companion object {
        fun fromString(valor: String): TipoCategoria {
            return when (valor.lowercase()) {
                "ingreso" -> INGRESO
                "gasto" -> GASTO
                else -> GASTO
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            INGRESO -> "ingreso"
            GASTO -> "gasto"
        }
    }
}