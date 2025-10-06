package com.lvmh.pocketpet.dominio.modelos

data class estado_mascota (
    val id: Long = 1,
    val salud: Int, // 0-100
    val nivel: Int,
    val coincideanimacion: String,
    val ultima_actualizacion: Long
)
enum class estado_salud_mascota {
    CRITICO,    // 0-20
    ALERTA,       // 21-40
    ESTABLE,      // 41-60
    SALUDABLE,     // 61-80
    PROSPERO   // 81-100
}
