package com.lvmh.pocketpet.dominio.utilidades

import android.graphics.Color
import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import com.lvmh.pocketpet.data.local.entidades.presupuesto_entidad
import com.lvmh.pocketpet.presentacion.componentes.vergrafico.DatoGrafico
import kotlin.math.roundToInt
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneId


object asistente_grafico {
    private val paletaColores = listOf(
        Color.parseColor("#FF8C42"),
        Color.parseColor("#4285F4"),
        Color.parseColor("#34A853"),
        Color.parseColor("#EA4335"),
        Color.parseColor("#FBBC04"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#00BCD4"),
        Color.parseColor("#FF5252"),
        Color.parseColor("#FFC107"),
        Color.parseColor("#8BC34A")
    )

    fun prepararDatosGraficoCategoria(transacciones: List<transaccion_entidad>): List<DatoGrafico> {
        val agrupado = transacciones
            .filter { it.tipo == "gasto" }
            .groupBy { it.id_categoria }
            .mapValues { entry -> entry.value.sumOf { it.monto } }

        return agrupado.entries.mapIndexed { indice, entry ->
            val categoriaId = entry.key
            val monto = entry.value
            DatoGrafico(
                etiqueta = "C$categoriaId",
                valor = monto.toFloat(),
                color = obtenerColorPorIndice(indice)
            )
        }
    }

    fun prepararDatosComparacionPresupuesto(presupuestos: List<presupuesto_entidad>): List<DatoGrafico> {
        return presupuestos.mapIndexed { _, presupuesto ->
            DatoGrafico(
                etiqueta = "${presupuesto.nombre.take(4)} (${presupuesto.porcentaje_usado.roundToInt()}%)",
                valor = presupuesto.gastado.toFloat(),
                color = when {
                    presupuesto.esta_excedido -> Color.RED
                    presupuesto.debe_alertar -> Color.YELLOW
                    else -> Color.GREEN
                }
            )
        }
    }

    fun prepararDatosPresupuestoVsReal(
        presupuestos: List<presupuesto_entidad>
    ): Pair<List<DatoGrafico>, List<DatoGrafico>> {
        val datosPresupuestado = presupuestos.map {
            DatoGrafico(
                etiqueta = it.nombre.take(4),
                valor = it.monto.toFloat(),
                color = Color.BLUE
            )
        }

        val datosReales = presupuestos.map {
            DatoGrafico(
                etiqueta = it.nombre.take(4),
                valor = it.gastado.toFloat(),
                color = Color.RED
            )
        }

        return Pair(datosPresupuestado, datosReales)
    }

    fun prepararDatosTendencia(transacciones: List<transaccion_entidad>): List<DatoGrafico> {
        val agrupado = transacciones
            .groupBy { it.fecha.toLocalDateTime().withDayOfMonth(1) }
            .mapValues { (_, elementos) -> elementos.sumOf { it.monto } }
            .toSortedMap()

        return agrupado.entries.mapIndexed { indice, entry ->
            DatoGrafico(
                etiqueta = "M${indice + 1}",
                valor = entry.value.toFloat(),
                color = obtenerColorPorIndice(indice)
            )
        }
    }

    fun prepararDatosIngresosVsGastos(
        transacciones: List<transaccion_entidad>
    ): Pair<DatoGrafico, DatoGrafico> {
        val ingreso = transacciones
            .filter { it.tipo == "ingreso" }
            .sumOf { it.monto }

        val gasto = transacciones
            .filter { it.tipo == "gasto" }
            .sumOf { it.monto }

        return Pair(
            DatoGrafico("Ingresos", ingreso.toFloat(), Color.GREEN),
            DatoGrafico("Gastos", gasto.toFloat(), Color.RED)
        )
    }

    fun prepararDatosComparacionMes(
        mesActual: List<transaccion_entidad>,
        mesAnterior: List<transaccion_entidad>
    ): Pair<DatoGrafico, DatoGrafico> {
        val gastoActual = mesActual
            .filter { it.tipo == "gasto" }
            .sumOf { it.monto }

        val gastoAnterior = mesAnterior
            .filter { it.tipo == "gasto" }
            .sumOf { it.monto }

        return Pair(
            DatoGrafico("Mes Anterior", gastoAnterior.toFloat(), Color.BLUE),
            DatoGrafico("Mes Actual", gastoActual.toFloat(), Color.RED)
        )
    }

    fun prepararDatosGraficoCircularGastos(transacciones: List<transaccion_entidad>): List<DatoGrafico> {
        val agrupado = transacciones
            .filter { it.tipo == "gasto" }
            .groupBy { it.id_categoria }
            .mapValues { (_, elementos) -> elementos.sumOf { it.monto } }

        return agrupado.entries.mapIndexed { indice, entry ->
            val categoriaId = entry.key
            val monto = entry.value
            DatoGrafico(
                etiqueta = "C$categoriaId",
                valor = monto.toFloat(),
                color = obtenerColorPorIndice(indice)
            )
        }
    }

    fun prepararDatosGraficoCircularMetas(
        metasCompletadas: Int,
        metasActivas: Int,
        metasPausadas: Int
    ): List<DatoGrafico> {
        return listOf(
            DatoGrafico("Completadas", metasCompletadas.toFloat(), Color.GREEN),
            DatoGrafico("Activas", metasActivas.toFloat(), Color.BLUE),
            DatoGrafico("Pausadas", metasPausadas.toFloat(), Color.GRAY)
        )
    }

    private fun obtenerColorPorIndice(indice: Int): Int {
        return paletaColores[indice % paletaColores.size]
    }

    fun obtenerColorPorValor(valor: Float, valorMaximo: Float): Int {
        val razon = (valor / valorMaximo).coerceIn(0f, 1f)
        return when {
            razon < 0.33f -> Color.GREEN
            razon < 0.66f -> Color.YELLOW
            else -> Color.RED
        }
    }

    fun obtenerColorPorEstado(estado: String): Int {
        return when (estado.lowercase()) {
            "bueno", "saludable", "en_ruta" -> Color.GREEN
            "advertencia", "alerta", "atencion" -> Color.YELLOW
            "malo", "critico", "excedido" -> Color.RED
            else -> Color.BLUE
        }
    }

    fun formatearValorGrafico(valor: Float): String {
        return when {
            valor >= 1_000_000 -> "$${String.format("%.1f", valor / 1_000_000)}M"
            valor >= 1_000 -> "$${String.format("%.1f", valor / 1_000)}K"
            else -> "$${String.format("%.0f", valor)}"
        }
    }

    fun calcularEscalaEjeY(valorMaximo: Float): Float {
        if (valorMaximo == 0f) return 100f

        val magnitud = Math.pow(10.0, Math.floor(Math.log10(valorMaximo.toDouble()))).toFloat()
        val normalizado = valorMaximo / magnitud

        return magnitud * when {
            normalizado <= 1 -> 1f
            normalizado <= 2 -> 2f
            normalizado <= 5 -> 5f
            else -> 10f
        }
    }

    fun calcularLineasCuadricula(valorMaximo: Float): Int {
        return when {
            valorMaximo <= 100 -> 5
            valorMaximo <= 1000 -> 4
            valorMaximo <= 10000 -> 5
            else -> 6
        }
    }

    fun generarEtiquetasEjeX(tamano: Int, prefijo: String = ""): List<String> {
        return (1..tamano).map { "$prefijo$it" }
    }

    fun calcularAnchoBarra(anchoContenedor: Float, cantidadElementos: Int): Float {
        val espacio = 20f
        return (anchoContenedor - (espacio * (cantidadElementos - 1))) / cantidadElementos
    }

    data class EstadisticasGrafico(
        val total: Float,
        val promedio: Float,
        val maximo: Float,
        val minimo: Float,
        val mediana: Float
    )

    fun calcularEstadisticasGrafico(datos: List<DatoGrafico>): EstadisticasGrafico {
        val valores = datos.map { it.valor }
        val ordenados = valores.sorted()

        return EstadisticasGrafico(
            total = valores.sum(),
            promedio = valores.average().toFloat(),
            maximo = valores.maxOrNull() ?: 0f,
            minimo = valores.minOrNull() ?: 0f,
            mediana = if (ordenados.isNotEmpty()) {
                if (ordenados.size % 2 == 0) {
                    (ordenados[ordenados.size / 2 - 1] + ordenados[ordenados.size / 2]) / 2
                } else {
                    ordenados[ordenados.size / 2]
                }
            } else 0f
        )
    }
}
