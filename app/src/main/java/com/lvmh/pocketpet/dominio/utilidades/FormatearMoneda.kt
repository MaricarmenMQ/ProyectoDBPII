package com.lvmh.pocketpet.dominio.utilidades
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object FormatearMoneda {

    private val simbolosPorMoneda = mapOf(
        "PEN" to "S/ ",
        "MXN" to "$ ",
        "USD" to "$ ",
        "EUR" to "€ ",
        "COP" to "$ ",
        "CLP" to "$ ",
        "ARS" to "$ ",
    )
    private val simbolos = DecimalFormatSymbols(Locale.forLanguageTag("es")).apply {
        groupingSeparator = ','
        decimalSeparator = '.'
    }

    private val formateador = DecimalFormat("#,##0.00", simbolos)

    fun formatear(monto: Double, codigoMoneda: String = "PEN"): String {
        val simbolo = simbolosPorMoneda[codigoMoneda] ?: ""
        return "$simbolo${formateador.format(monto)}"
    }
}
