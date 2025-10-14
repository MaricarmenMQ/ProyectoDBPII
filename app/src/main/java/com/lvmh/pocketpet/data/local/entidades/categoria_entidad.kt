package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class categoria_entidad(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val usuario_id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val icono: String = "📁",
    val color: String = "#2196F3",
    val tipo: String = "", // "ingreso" o "gasto"
    val es_predeterminada: Boolean = false,
    val orden: Int = 0,
    val esta_activa: Boolean = true,
    val creado_en: Long = System.currentTimeMillis(),
    val actualizado_en: Long = System.currentTimeMillis()
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

object categorias_predeterminadas {
    val CATEGORIAS_GASTO = listOf(
        categoria_entidad(
            id = 1,
            usuario_id = "sistema",
            nombre = "Alimentación",
            descripcion = "Comida, supermercado, restaurantes",
            icono = "🍔",
            color = "#FF5722",
            tipo = "gasto",
            es_predeterminada = true,
            orden = 1
        ),
        categoria_entidad(
            id = 2,
            usuario_id = "sistema",
            nombre = "Transporte",
            descripcion = "Taxi, combustible, transporte público",
            icono = "🚗",
            color = "#2196F3",
            tipo = "gasto",
            es_predeterminada = true,
            orden = 2
        ),
        categoria_entidad(
            id = 3,
            usuario_id = "sistema",
            nombre = "Entretenimiento",
            descripcion = "Cine, streaming, juegos, salidas",
            icono = "🎮",
            color = "#9C27B0",
            tipo = "gasto",
            es_predeterminada = true,
            orden = 3
        ),
        categoria_entidad(
            id = 4,
            usuario_id = "sistema",
            nombre = "Salud",
            descripcion = "Medicinas, doctor, farmacia",
            icono = "💊",
            color = "#4CAF50",
            tipo = "gasto",
            es_predeterminada = true,
            orden = 4
        ),
        categoria_entidad(
            id = 5,
            usuario_id = "sistema",
            nombre = "Servicios",
            descripcion = "Luz, agua, internet, teléfono",
            icono = "💡",
            color = "#FFC107",
            tipo = "gasto",
            es_predeterminada = true,
            orden = 5
        ),
        categoria_entidad(
            id = 6,
            usuario_id = "sistema",
            nombre = "Educación",
            descripcion = "Cursos, libros, materiales",
            icono = "📚",
            color = "#00BCD4",
            tipo = "gasto",
            es_predeterminada = true,
            orden = 6
        ),
        categoria_entidad(
            id = 7,
            usuario_id = "sistema",
            nombre = "Vivienda",
            descripcion = "Alquiler, mantenimiento, muebles",
            icono = "🏠",
            color = "#795548",
            tipo = "gasto",
            es_predeterminada = true,
            orden = 7
        ),
        categoria_entidad(
            id = 8,
            usuario_id = "sistema",
            nombre = "Otros Gastos",
            descripcion = "Gastos varios",
            icono = "💸",
            color = "#9E9E9E",
            tipo = "gasto",
            es_predeterminada = true,
            orden = 99
        )
    )

    val CATEGORIAS_INGRESO = listOf(
        categoria_entidad(
            id = 9,
            usuario_id = "sistema",
            nombre = "Salario",
            descripcion = "Sueldo mensual, pago de trabajo",
            icono = "💰",
            color = "#4CAF50",
            tipo = "ingreso",
            es_predeterminada = true,
            orden = 1
        ),
        categoria_entidad(
            id = 10,
            usuario_id = "sistema",
            nombre = "Freelance",
            descripcion = "Trabajos independientes",
            icono = "💼",
            color = "#2196F3",
            tipo = "ingreso",
            es_predeterminada = true,
            orden = 2
        ),
        categoria_entidad(
            id = 11,
            usuario_id = "sistema",
            nombre = "Inversiones",
            descripcion = "Dividendos, rendimientos",
            icono = "📈",
            color = "#FF9800",
            tipo = "ingreso",
            es_predeterminada = true,
            orden = 3
        ),
        categoria_entidad(
            id = 12,
            usuario_id = "sistema",
            nombre = "Otros Ingresos",
            descripcion = "Ingresos varios",
            icono = "💵",
            color = "#8BC34A",
            tipo = "ingreso",
            es_predeterminada = true,
            orden = 99
        )
    )

    val TODAS = CATEGORIAS_GASTO + CATEGORIAS_INGRESO
}
