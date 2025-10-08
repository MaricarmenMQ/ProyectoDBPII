package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mascota")
data class mascota_entidad(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 1,

    val salud: Int = 50,
    val nivel: Int = 1,

    val coincidencia_animacion: String = "ESTABLE",

    val ultima_actualizacion: Long = System.currentTimeMillis(),

    val nombre: String = "FinanPet"
)


enum class estado_salud_mascota {
    CRITICO,
    ALERTA,
    ESTABLE,
    SALUDABLE,
    PROSPERO;

    companion object {
        fun desde_salud(salud: Int): estado_salud_mascota {
            return when (salud) {
                in 0..20 -> CRITICO
                in 21..40 -> ALERTA
                in 41..60 -> ESTABLE
                in 61..80 -> SALUDABLE
                else -> PROSPERO
            }
        }
    }
}