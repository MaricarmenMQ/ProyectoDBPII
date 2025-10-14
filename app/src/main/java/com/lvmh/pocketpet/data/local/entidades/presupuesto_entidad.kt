package com.lvmh.pocketpet.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lvmh.pocketpet.dominio.modelos.categoria
import java.time.LocalDateTime
@Entity(
    tableName = "presupuestos",
    foreignKeys = [
        ForeignKey(
            entity = categoria_entidad::class,
            parentColumns = ["id"],
            childColumns = ["categoria_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = cuenta_entidad::class,
            parentColumns = ["id"],
            childColumns = ["cuenta_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("categoria_id"),
        Index("cuenta_id"),
        Index("usuario_id")
    ]
)
data class presupuesto_entidad(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    val usuario_id: String = "",
    val categoria_id: String = "",
    val cuenta_id: String? = null,
    val nombre: String = "",
    val monto: Double = 0.0,
    val gastado: Double = 0.0,
    val periodo: periodo_presupuesto = periodo_presupuesto.MENSUAL,
    val fecha_inicio: LocalDateTime = LocalDateTime.now(),
    val fecha_fin: LocalDateTime? = null,
    val esta_activo: Boolean = true,
    val porcentaje_alerta: Int = 80,
    val notas: String = "",
    val color: String = "#4CAF50",
    val creado_en: LocalDateTime = LocalDateTime.now(),
    val actualizado_en: LocalDateTime = LocalDateTime.now()
) {
    val monto_restante: Double
        get() = (monto - gastado).coerceAtLeast(0.0)

    val porcentaje_usado: Double
        get() = if (monto > 0) (gastado / monto * 100).coerceIn(0.0, 100.0) else 0.0

    val esta_excedido: Boolean
        get() = gastado > monto

    val debe_alertar: Boolean
        get() = porcentaje_usado >= porcentaje_alerta
}

enum class periodo_presupuesto {
    SEMANAL,
    MENSUAL,
    TRIMESTRAL,
    ANUAL,
    PERSONALIZADO
}
