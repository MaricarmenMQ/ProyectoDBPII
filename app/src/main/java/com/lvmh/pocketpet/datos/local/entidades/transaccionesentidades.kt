package com.lvmh.pocketpet.datos.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacciones")
data class transaccionesentidades(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tipo: String,
    val monto: Double,
    val id_categoria: Long,
    val id_monto: Long,
    val descripcion: String,
    val fecha: Long,
    val imagenUri: String?
)