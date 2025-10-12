package com.lvmh.pocketpet.dominio.modelos

data class transaccion(
    val id: Long = 0,
    val tipo: Tipotransaccion,
    val monto: Double,
    val idCategoria: String,
    val idCuenta: String,
    val descripcion: String,
    val fecha: Long = System.currentTimeMillis(),
    val imagenUri: String? = null,
    val notas: String = "",
    val etiquetas: List<String> = emptyList(),
    val esRecurrente: Boolean = false
) {
    val esIngreso: Boolean
        get() = tipo == Tipotransaccion.Ingreso

    val esGasto: Boolean
        get() = tipo == Tipotransaccion.Gasto
}

enum class Tipotransaccion {
    Ingreso,
    Gasto;

    companion object {
        fun fromString(valor: String): Tipotransaccion {
            return when (valor.lowercase()) {
                "ingreso" -> Ingreso
                "gasto" -> Gasto
                else -> Gasto
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            Ingreso -> "ingreso"
            Gasto -> "gasto"
        }
    }
}