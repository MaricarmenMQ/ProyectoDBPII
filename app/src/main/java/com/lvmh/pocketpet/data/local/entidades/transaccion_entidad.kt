package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "transacciones",
    foreignKeys = [
        ForeignKey(
            entity = categoria_entidad::class,
            parentColumns = ["id"],
            childColumns = ["id_categoria"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = cuenta_entidad::class,
            parentColumns = ["id"],
            childColumns = ["id_cuenta"],
            onDelete = ForeignKey.CASCADE
        )
    ],
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
    val tipo: String, // "ingreso" o "gasto"
    val monto: Double,
    val id_categoria: String,
    val id_cuenta: String,
    val descripcion: String,
    val fecha: Long = System.currentTimeMillis(),
    val imagenUri: String? = null,
    val notas: String = "",
    val etiquetas: String = "", // Separadas por comas
    val es_recurrente: Boolean = false,
    val creado_en: Long = System.currentTimeMillis(),
    val actualizado_en: Long = System.currentTimeMillis()
)