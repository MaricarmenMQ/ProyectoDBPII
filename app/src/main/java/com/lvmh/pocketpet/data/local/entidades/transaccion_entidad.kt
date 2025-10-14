package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "transacciones",
    indices = [
        Index("id_categoria"),
        Index("id_cuenta"),
        Index("tipo"),
        Index("fecha")
    ]
)
data class transaccion_entidad(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val id_categoria: String = "",
    val id_cuenta: String = "",
    val tipo: String = "gasto", // "ingreso" o "gasto"
    val monto: Double = 0.0,
    val descripcion: String = "",
    val fecha: Long = System.currentTimeMillis(),
    val imagenUri: String? = null,
    val notas: String = "",
    val etiquetas: String = "", // Separadas por comas
    val es_recurrente: Boolean = false,
    val creado_en: Long = System.currentTimeMillis(),
    val actualizado_en: Long = System.currentTimeMillis()
) {
    val es_ingreso: Boolean
        get() = tipo.lowercase() == "ingreso"

    val es_gasto: Boolean
        get() = tipo.lowercase() == "gasto"

    val lista_etiquetas: List<String>
        get() = if (etiquetas.isBlank()) emptyList()
        else etiquetas.split(",").map { it.trim() }
}

enum class TipoTransaccion {
    INGRESO,
    GASTO;

    companion object {
        fun fromString(valor: String): TipoTransaccion {
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