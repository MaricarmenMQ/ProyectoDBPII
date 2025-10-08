package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logros")
data class logro_entidad(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // IDENTIFICADOR
    val codigo_logro: String,

    // INFORmm
    val titulo: String,
    val descripcion: String,
    val icono: String,
    val categoria: String,

    // RECOMPENSAS
    val puntos_experiencia: Int,
    val salud_mascota: Int,

    // ESTADO
    val desbloqueado: Boolean = false,
    val fecha_desbloqueo: Long? = null,
   //PROGRESO
    val progreso_actual: Int = 0,
    val progreso_total: Int = 1,

    // RAREZA
    val rareza: String = "COMUN"
)


enum class categoria_logro {
    PRINCIPIANTE,  // Primeros pasos en la app
    AHORRO,        // Relacionados con ahorrar dinero
    DISCIPLINA,    // Registrar transacciones constantemente
    MASCOTA,       // Relacionados con cuidar la mascota
    ESPECIAL       // Logros únicos o temporales
}

enum class rareza_logro(val color: String, val emoji: String) {
    COMUN("#95A5A6", "⭐"),      // Gris
    RARO("#3498DB", "✨"),       // Azul
    EPICO("#9B59B6", "💎"),     // Morado
    LEGENDARIO("#F39C12", "👑") // Dorado
}


data class definicion_logro(
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val icono: String,
    val categoria: categoria_logro,
    val puntos_xp: Int,
    val salud_mascota: Int,
    val progreso_total: Int = 1,
    val rareza: rareza_logro = rareza_logro.COMUN
)


object logros_disponibles {

    val LOGROS = listOf(
        // === PRINCIPIANTE ===
        definicion_logro(
            codigo = "PRIMERA_TRANSACCION",
            titulo = "¡Primer Paso!",
            descripcion = "Registra tu primera transacción",
            icono = "ic_logro_primer_paso",
            categoria = categoria_logro.PRINCIPIANTE,
            puntos_xp = 50,
            salud_mascota = 5,
            rareza = rareza_logro.COMUN
        ),

        definicion_logro(
            codigo = "PRIMERA_CATEGORIA",
            titulo = "Organizador",
            descripcion = "Crea tu primera categoría personalizada",
            icono = "ic_logro_organizador",
            categoria = categoria_logro.PRINCIPIANTE,
            puntos_xp = 30,
            salud_mascota = 3,
            rareza = rareza_logro.COMUN
        ),

        definicion_logro(
            codigo = "NOMBRAR_MASCOTA",
            titulo = "Nuevo Amigo",
            descripcion = "Dale un nombre a tu mascota",
            icono = "ic_logro_amigo",
            categoria = categoria_logro.MASCOTA,
            puntos_xp = 20,
            salud_mascota = 10,
            rareza = rareza_logro.COMUN
        ),

        // === AHORRO ===
        definicion_logro(
            codigo = "AHORRO_10K",
            titulo = "Ahorrador Novato",
            descripcion = "Ahorra S/ 10,000",
            icono = "ic_logro_ahorro_novato",
            categoria = categoria_logro.AHORRO,
            puntos_xp = 100,
            salud_mascota = 15,
            rareza = rareza_logro.RARO
        ),

        definicion_logro(
            codigo = "AHORRO_50K",
            titulo = "Ahorrador Experto",
            descripcion = "Ahorra S/ 50,000",
            icono = "ic_logro_ahorro_experto",
            categoria = categoria_logro.AHORRO,
            puntos_xp = 300,
            salud_mascota = 25,
            rareza = rareza_logro.EPICO
        ),

        definicion_logro(
            codigo = "AHORRO_100K",
            titulo = "Maestro del Ahorro",
            descripcion = "¡Ahorraste S/ 100,000!",
            icono = "ic_logro_maestro_ahorro",
            categoria = categoria_logro.AHORRO,
            puntos_xp = 500,
            salud_mascota = 40,
            rareza = rareza_logro.LEGENDARIO
        ),

        // === DISCIPLINA ===
        definicion_logro(
            codigo = "RACHA_7_DIAS",
            titulo = "Semana Perfecta",
            descripcion = "Registra transacciones 7 días seguidos",
            icono = "ic_logro_racha_7",
            categoria = categoria_logro.DISCIPLINA,
            puntos_xp = 150,
            salud_mascota = 20,
            progreso_total = 7,
            rareza = rareza_logro.RARO
        ),

        definicion_logro(
            codigo = "RACHA_30_DIAS",
            titulo = "Mes Impecable",
            descripcion = "Registra transacciones 30 días seguidos",
            icono = "ic_logro_racha_30",
            categoria = categoria_logro.DISCIPLINA,
            puntos_xp = 400,
            salud_mascota = 35,
            progreso_total = 30,
            rareza = rareza_logro.EPICO
        ),

        definicion_logro(
            codigo = "100_TRANSACCIONES",
            titulo = "Contador Pro",
            descripcion = "Registra 100 transacciones",
            icono = "ic_logro_contador",
            categoria = categoria_logro.DISCIPLINA,
            puntos_xp = 200,
            salud_mascota = 15,
            progreso_total = 100,
            rareza = rareza_logro.RARO
        ),

        // === MASCOTA ===
        definicion_logro(
            codigo = "MASCOTA_NIVEL_5",
            titulo = "Entrenador Junior",
            descripcion = "Tu mascota alcanzó nivel 5",
            icono = "ic_logro_entrenador_jr",
            categoria = categoria_logro.MASCOTA,
            puntos_xp = 120,
            salud_mascota = 20,
            rareza = rareza_logro.RARO
        ),

        definicion_logro(
            codigo = "MASCOTA_NIVEL_10",
            titulo = "Entrenador Experto",
            descripcion = "Tu mascota alcanzó nivel 10",
            icono = "ic_logro_entrenador_exp",
            categoria = categoria_logro.MASCOTA,
            puntos_xp = 300,
            salud_mascota = 30,
            rareza = rareza_logro.EPICO
        ),

        definicion_logro(
            codigo = "MASCOTA_SALUD_100",
            titulo = "¡Salud Perfecta!",
            descripcion = "Mantén a tu mascota con 100 de salud",
            icono = "ic_logro_salud_perfecta",
            categoria = categoria_logro.MASCOTA,
            puntos_xp = 250,
            salud_mascota = 0, // No suma más porque ya está al máximo
            rareza = rareza_logro.EPICO
        ),

        // === ESPECIAL ===
        definicion_logro(
            codigo = "META_CUMPLIDA",
            titulo = "Soñador Cumplido",
            descripcion = "Completa tu primera meta de ahorro",
            icono = "ic_logro_meta",
            categoria = categoria_logro.ESPECIAL,
            puntos_xp = 180,
            salud_mascota = 25,
            rareza = rareza_logro.RARO
        ),

        definicion_logro(
            codigo = "PRESUPUESTO_RESPETADO",
            titulo = "Disciplinado",
            descripcion = "Respeta tu presupuesto mensual completo",
            icono = "ic_logro_disciplinado",
            categoria = categoria_logro.ESPECIAL,
            puntos_xp = 200,
            salud_mascota = 20,
            rareza = rareza_logro.EPICO
        )
    )
}