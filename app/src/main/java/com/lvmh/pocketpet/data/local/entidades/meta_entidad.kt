package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "metas",
    foreignKeys = [
        ForeignKey(
            entity = categoria_entidad::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = cuenta_entidad::class,
            parentColumns = ["id"],
            childColumns = ["cuentaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("categoriaId"),
        Index("cuentaId"),
        Index("usuarioId"),
        Index("estado")
    ]
)
data class meta_entidad(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: String = "",
    val categoriaId: String = "",
    val cuentaId: String? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val montoObjetivo: Double = 0.0,
    val montoActual: Double = 0.0,
    val estado: EstadoMeta = EstadoMeta.EN_PROGRESO,
    val prioridad: PrioridadMeta = PrioridadMeta.MEDIA,
    val fechaLimite: LocalDateTime? = null,
    val categoria: CategoriaMeta = CategoriaMeta.AHORRO,
    val esAutomatico: Boolean = false,
    val notas: String = "",
    val icono: String = "🎯",
    val color: String = "#2196F3",
    val creadoEn: LocalDateTime = LocalDateTime.now(),
    val actualizadoEn: LocalDateTime = LocalDateTime.now(),
    val completadoEn: LocalDateTime? = null
) {
    val porcentajeCompletado: Double
        get() = if (montoObjetivo > 0) (montoActual / montoObjetivo * 100).coerceIn(0.0, 100.0) else 0.0

    val montoRestante: Double
        get() = (montoObjetivo - montoActual).coerceAtLeast(0.0)

    val estaCompletada: Boolean
        get() = montoActual >= montoObjetivo || estado == EstadoMeta.COMPLETADA

    val diasRestantes: Long?
        get() = fechaLimite?.let { (it.toLocalDate().toEpochDay() - LocalDateTime.now().toLocalDate().toEpochDay()) }

    val estaVencida: Boolean
        get() = fechaLimite?.let { LocalDateTime.now().isAfter(it) } ?: false
}

enum class EstadoMeta {
    EN_PROGRESO,
    PAUSADA,
    COMPLETADA,
    ABANDONADA,
    FALLIDA
}

enum class PrioridadMeta {
    BAJA,
    MEDIA,
    ALTA,
    CRITICA
}

enum class CategoriaMeta {
    AHORRO,
    INVERSION,
    PAGO_DEUDA,
    FONDO_EMERGENCIA,
    EDUCACION,
    VACACIONES,
    HOGAR,
    VEHICULO,
    JUBILACION,
    OTRO
}