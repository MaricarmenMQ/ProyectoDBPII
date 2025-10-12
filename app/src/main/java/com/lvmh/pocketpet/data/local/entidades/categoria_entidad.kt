package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "categorias",
    indices = [
        Index("usuario_id"),
        Index("tipo")
    ]
)
data class categoria_entidad(
    @PrimaryKey
    val id: String = "",
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

// Categorías predeterminadas del sistema
object categorias_predeterminadas {
    val CATEGORIAS_GASTO = listOf(
        categoria_entidad(
            id = "cat_gasto_alimentacion",
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
            id = "cat_gasto_transporte",
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
            id = "cat_gasto_entretenimiento",
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
            id = "cat_gasto_salud",
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
            id = "cat_gasto_servicios",
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
            id = "cat_gasto_educacion",
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
            id = "cat_gasto_vivienda",
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
            id = "cat_gasto_otros",
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
            id = "cat_ingreso_salario",
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
            id = "cat_ingreso_freelance",
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
            id = "cat_ingreso_inversion",
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
            id = "cat_ingreso_otros",
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
