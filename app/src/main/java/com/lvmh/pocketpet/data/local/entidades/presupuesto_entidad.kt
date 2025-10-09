package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.lvmh.pocketpet.dominio.modelos.categoria
import java.time.LocalDateTime

@Entity(
    tableName = "presupuestos",
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
        Index("usuarioId")
    ]
)
data class presupuesto_entidad(
    val id: String = "",
    val usuarioId: String = "",
    val categoriaId: String = "",
    val cuentaId: String? = null,
    val nombre: String = "",
    val monto: Double = 0.0,
    val gastado: Double = 0.0,
    val periodo: PeriodoPresupuesto = PeriodoPresupuesto.MENSUAL,
    val fechaInicio: LocalDateTime = LocalDateTime.now(),
    val fechaFin: LocalDateTime? = null,
    val estaActivo: Boolean = true,
    val porcentajeAlerta: Int = 80,
    val notas: String = "",
    val color: String = "#4CAF50",
    val creadoEn: LocalDateTime = LocalDateTime.now(),
    val actualizadoEn: LocalDateTime = LocalDateTime.now()
) {
    val montoRestante: Double
        get() = (monto - gastado).coerceAtLeast(0.0)

    val porcentajeUsado: Double
        get() = if (monto > 0) (gastado / monto * 100).coerceIn(0.0, 100.0) else 0.0

    val excedido: Boolean
        get() = gastado > monto

    val debeAlerta: Boolean
        get() = porcentajeUsado >= porcentajeAlerta
}

enum class PeriodoPresupuesto {
    SEMANAL,
    MENSUAL,
    TRIMESTRAL,
    ANUAL,
    PERSONALIZADO
}
