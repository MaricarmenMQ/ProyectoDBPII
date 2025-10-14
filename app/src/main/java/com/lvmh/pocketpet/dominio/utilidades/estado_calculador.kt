package com.lvmh.pocketpet.dominio.utilidades

import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import com.lvmh.pocketpet.data.local.entidades.presupuesto_entidad
import com.lvmh.pocketpet.data.local.entidades.meta_entidad
import java.time.Instant
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import kotlin.math.roundToInt

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

object CalculadoraEstadisticas {

    fun calcularIngresoTotal(transacciones: List<transaccion_entidad>): Double {
        return transacciones
            .filter { it.tipo == "ingreso" }
            .sumOf { it.monto }
    }

    fun calcularGastoTotal(transacciones: List<transaccion_entidad>): Double {
        return transacciones
            .filter { it.tipo == "gasto" }
            .sumOf { it.monto }
    }

    fun calcularBalanceNeto(transacciones: List<transaccion_entidad>): Double {
        val ingreso = calcularIngresoTotal(transacciones)
        val gasto = calcularGastoTotal(transacciones)
        return ingreso - gasto
    }

    fun calcularPromedioGasto(transacciones: List<transaccion_entidad>): Double {
        val gastos = transacciones.filter { it.tipo == "gasto" }
        return if (gastos.isNotEmpty()) {
            gastos.sumOf { it.monto } / gastos.size
        } else {
            0.0
        }
    }

    fun calcularPromedioIngreso(transacciones: List<transaccion_entidad>): Double {
        val ingresos = transacciones.filter { it.tipo == "ingreso" }
        return if (ingresos.isNotEmpty()) {
            ingresos.sumOf { it.monto } / ingresos.size
        } else {
            0.0
        }
    }

    fun agruparTransaccionesPorCategoria(transacciones: List<transaccion_entidad>): Map<Long, Double> {
        return transacciones
            .groupBy { it.id_categoria.toLong() }
            .mapValues { (_, elementos) -> elementos.sumOf { it.monto } }
    }

    fun agruparTransaccionesPorMes(transacciones: List<transaccion_entidad>): Map<YearMonth, Double> {
        return transacciones
            .groupBy { YearMonth.from(it.fecha.toLocalDateTime()) }
            .mapValues { (_, elementos) -> elementos.sumOf { it.monto } }
    }

    fun calcularTendenciaGastos(transacciones: List<transaccion_entidad>): Double {
        val ahora = LocalDateTime.now()

        val gastoMesActual = transacciones.filter {
            val fecha = it.fecha.toLocalDateTime()
            fecha.year == ahora.year && fecha.monthValue == ahora.monthValue && it.tipo == "gasto"
        }.sumOf { it.monto }

        val mesAnterior = ahora.minusMonths(1)
        val gastoMesAnterior = transacciones.filter {
            val fecha = it.fecha.toLocalDateTime()
            fecha.year == mesAnterior.year && fecha.monthValue == mesAnterior.monthValue && it.tipo == "gasto"
        }.sumOf { it.monto }

        return if (gastoMesAnterior > 0) {
            ((gastoMesActual - gastoMesAnterior) / gastoMesAnterior * 100)
        } else {
            0.0
        }
    }

    fun encontrarCategoriaMayorGasto(transacciones: List<transaccion_entidad>): Pair<Long, Double>? {
        return agruparTransaccionesPorCategoria(transacciones)
            .maxByOrNull { it.value }
            ?.let { Pair(it.key, it.value) }
    }

    fun calcularPorcentajePorCategoria(transacciones: List<transaccion_entidad>): Map<Long, Double> {
        val total = calcularGastoTotal(transacciones)
        return agruparTransaccionesPorCategoria(transacciones)
            .mapValues { (_, monto) ->
                if (total > 0) (monto / total * 100) else 0.0
            }
    }

    fun calcularTotalPresupuestado(presupuestos: List<presupuesto_entidad>): Double {
        return presupuestos
            .filter { it.esta_activo }
            .sumOf { it.monto }
    }

    fun calcularTotalGastadoPresupuestos(presupuestos: List<presupuesto_entidad>): Double {
        return presupuestos
            .filter { it.esta_activo }
            .sumOf { it.gastado }
    }

    fun calcularPorcentajePromedioPresupuesto(presupuestos: List<presupuesto_entidad>): Double {
        if (presupuestos.isEmpty()) return 0.0
        return presupuestos
            .filter { it.esta_activo }
            .map { it.porcentaje_usado }
            .average()
    }

    fun contarPresupuestosExcedidos(presupuestos: List<presupuesto_entidad>): Int {
        return presupuestos.count { it.esta_excedido }
    }

    fun contarPresupuestosConAlerta(presupuestos: List<presupuesto_entidad>): Int {
        return presupuestos.count { it.debe_alertar }
    }

    fun calcularPresupuestoRestante(presupuestos: List<presupuesto_entidad>): Double {
        return presupuestos
            .filter { it.esta_activo }
            .sumOf { it.monto_restante }
    }

    fun calcularPromedioProgresoMetas(metas: List<meta_entidad>): Double {
        if (metas.isEmpty()) return 0.0
        return metas
            .map { it.porcentajeCompletado }
            .average()
    }

    fun contarMetasCompletadas(metas: List<meta_entidad>): Int {
        return metas.count { it.estaCompletada }
    }

    fun contarMetasActivas(metas: List<meta_entidad>): Int {
        return metas.count { it.estado.name == "EN_PROGRESO" }
    }

    fun calcularTasaCompletacionMetas(metas: List<meta_entidad>): Double {
        if (metas.isEmpty()) return 0.0
        val completadas = contarMetasCompletadas(metas)
        return (completadas.toDouble() / metas.size * 100)
    }

    fun calcularTotalObjetivoMetas(metas: List<meta_entidad>): Double {
        return metas.sumOf { it.montoObjetivo }
    }

    fun calcularTotalAhorradoMetas(metas: List<meta_entidad>): Double {
        return metas.sumOf { it.montoActual }
    }

    fun encontrarMetasCasiCompletas(metas: List<meta_entidad>): List<meta_entidad> {
        return metas.filter {
            it.porcentajeCompletado in 75.0..99.9 && !it.estaCompletada
        }
    }

    fun encontrarMetasVencidas(metas: List<meta_entidad>): List<meta_entidad> {
        return metas.filter { it.estaVencida }
    }

    fun encontrarMetasEnRiesgo(metas: List<meta_entidad>): List<meta_entidad> {
        return metas.filter { meta ->
            meta.diasRestantes?.let { dias ->
                dias > 0 && meta.porcentajeCompletado < (100 * dias / 365)
            } ?: false
        }
    }

    fun compararGastoMensual(
        mesActual: List<transaccion_entidad>,
        mesAnterior: List<transaccion_entidad>
    ): Double {
        val gastoActual = calcularGastoTotal(mesActual)
        val gastoAnterior = calcularGastoTotal(mesAnterior)

        return if (gastoAnterior > 0) {
            ((gastoActual - gastoAnterior) / gastoAnterior * 100)
        } else {
            0.0
        }
    }

    fun proyectarGastoAnual(transacciones: List<transaccion_entidad>): Double {
        val ahora = LocalDateTime.now()
        val mesesPasados = ahora.monthValue.toDouble()
        val gastoActualMes = calcularGastoTotal(transacciones)

        return (gastoActualMes / mesesPasados) * 12
    }

    data class ResumenEstadisticas(
        val ingresoTotal: Double,
        val gastoTotal: Double,
        val balanceNeto: Double,
        val promedioGasto: Double,
        val totalPresupuestado: Double,
        val usoPresupuesto: Double,
        val metasCompletadas: Int,
        val totalMetas: Int,
        val tasaCompletacion: Double
    )

    fun generarResumenEstadisticas(
        transacciones: List<transaccion_entidad>,
        presupuestos: List<presupuesto_entidad>,
        metas: List<meta_entidad>
    ): ResumenEstadisticas {
        return ResumenEstadisticas(
            ingresoTotal = calcularIngresoTotal(transacciones),
            gastoTotal = calcularGastoTotal(transacciones),
            balanceNeto = calcularBalanceNeto(transacciones),
            promedioGasto = calcularPromedioGasto(transacciones),
            totalPresupuestado = calcularTotalPresupuestado(presupuestos),
            usoPresupuesto = calcularPorcentajePromedioPresupuesto(presupuestos),
            metasCompletadas = contarMetasCompletadas(metas),
            totalMetas = metas.size,
            tasaCompletacion = calcularTasaCompletacionMetas(metas)
        )
    }
}
