package com.lvmh.pocketpet.dominio.modelos

import java.time.LocalDateTime

data class presupuesto(
    val id: String = "",
    val idUsuario: String = "",
    val idCategoria: String = "",
    val idCuenta: String? = null,
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

    val estaExcedido: Boolean
        get() = gastado > monto

    val debeAlertar: Boolean
        get() = porcentajeUsado >= porcentajeAlerta
}

enum class PeriodoPresupuesto {
    SEMANAL,
    MENSUAL,
    TRIMESTRAL,
    ANUAL,
    PERSONALIZADO
}
