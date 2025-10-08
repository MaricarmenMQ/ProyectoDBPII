package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Transaccion")
data class transaccion_entidad(
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