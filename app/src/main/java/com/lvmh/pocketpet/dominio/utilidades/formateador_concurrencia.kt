package com.lvmh.pocketpet.dominio.utilidades

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object FormateadorConcurrencia {

    private val simbolosPorMoneda = mapOf(
        "PEN" to "S/ ",
        "MXN" to "$ ",
        "USD" to "$ ",
        "EUR" to "€ ",
        "COP" to "$ ",
        "CLP" to "$ ",
        "ARS" to "$ ",
        "BRL" to "R$ ",
        "GBP" to "£ "
    )

    private val simbolos = DecimalFormatSymbols(Locale.forLanguageTag("es")).apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }

    private val formateador = DecimalFormat("#,##0.00", simbolos)
    private val formateadorSinDecimales = DecimalFormat("#,##0", simbolos)

    /**
     * Formatea un monto con la moneda especificada
     */
    fun formatear(monto: Double, codigoMoneda: String = "PEN"): String {
        val simbolo = simbolosPorMoneda[codigoMoneda] ?: "S/ "
        return "$simbolo${formateador.format(monto)}"
    }

    /**
     * Formatea un monto sin decimales
     */
    fun formatearSinDecimales(monto: Double, codigoMoneda: String = "PEN"): String {
        val simbolo = simbolosPorMoneda[codigoMoneda] ?: "S/ "
        return "$simbolo${formateadorSinDecimales.format(monto)}"
    }

    /**
     * Formatea solo el número sin símbolo de moneda
     */
    fun formatearNumero(monto: Double): String {
        return formateador.format(monto)
    }

    /**
     * Formatea un monto de forma compacta (1K, 1M, etc.)
     */
    fun formatearCompacto(monto: Double, codigoMoneda: String = "PEN"): String {
        val simbolo = simbolosPorMoneda[codigoMoneda] ?: "S/ "

        return when {
            monto >= 1_000_000 -> "$simbolo${String.format("%.1f", monto / 1_000_000)}M"
            monto >= 1_000 -> "$simbolo${String.format("%.1f", monto / 1_000)}K"
            else -> "$simbolo${formateador.format(monto)}"
        }
    }

    /**
     * Formatea con signo + o - según sea positivo o negativo
     */
    fun formatearConSigno(monto: Double, codigoMoneda: String = "PEN"): String {
        val valorFormateado = formatear(kotlin.math.abs(monto), codigoMoneda)
        return if (monto >= 0) "+$valorFormateado" else "-$valorFormateado"
    }

    /**
     * Convierte un string a Double, manejando formatos con comas
     */
    fun parsearMonto(texto: String): Double? {
        return try {
            val textoLimpio = texto
                .replace(",", "")
                .replace("S/", "")
                .replace("$", "")
                .replace("€", "")
                .replace("£", "")
                .replace("R$", "")
                .trim()

            textoLimpio.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Obtiene el símbolo de una moneda
     */
    fun obtenerSimbolo(codigoMoneda: String): String {
        return simbolosPorMoneda[codigoMoneda] ?: "S/ "
    }

    /**
     * Formatea un porcentaje
     */
    fun formatearPorcentaje(valor: Double): String {
        return "${String.format("%.1f", valor)}%"
    }

    /**
     * Formatea la diferencia entre dos montos
     */
    fun formatearDiferencia(montoActual: Double, montoAnterior: Double, codigoMoneda: String = "PEN"): String {
        val diferencia = montoActual - montoAnterior
        val porcentaje = if (montoAnterior != 0.0) {
            (diferencia / kotlin.math.abs(montoAnterior)) * 100
        } else {
            0.0
        }

        val signo = if (diferencia >= 0) "+" else "-"
        val valorAbsoluto = kotlin.math.abs(diferencia)

        return "$signo${formatear(valorAbsoluto, codigoMoneda)} (${formatearPorcentaje(kotlin.math.abs(porcentaje))})"
    }
}