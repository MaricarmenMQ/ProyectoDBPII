package com.lvmh.pocketpet.presentacion.componentes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.dominio.utilidades.FormatearMoneda

class BalanceCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val textoTitulo: TextView
    private val textoBalance: TextView
    private val textoIngresos: TextView
    private val textoGastos: TextView
    private val textoAhorro: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.componente_balance_card, this, true)

        textoTitulo = findViewById(R.id.textoTituloBalance)
        textoBalance = findViewById(R.id.textoBalanceTotal)
        textoIngresos = findViewById(R.id.textoIngresos)
        textoGastos = findViewById(R.id.textoGastos)
        textoAhorro = findViewById(R.id.textoAhorro)

        radius = 16f
        cardElevation = 8f
        useCompatPadding = true
        setCardBackgroundColor(context.getColor(R.color.color_primario))
    }

    fun configurarBalance(
        balance: Double,
        ingresos: Double,
        gastos: Double,
        titulo: String = "Balance Total"
    ) {
        textoTitulo.text = titulo
        textoBalance.text = FormatearMoneda.formatear(balance)
        textoIngresos.text = FormatearMoneda.formatear(ingresos)
        textoGastos.text = FormatearMoneda.formatear(gastos)

        val ahorro = ingresos - gastos
        textoAhorro.text = FormatearMoneda.formatear(ahorro)

        // Color del ahorro según si es positivo o negativo
        val colorAhorro = if (ahorro >= 0) {
            context.getColor(R.color.color_ingreso)
        } else {
            context.getColor(R.color.color_gasto)
        }
        textoAhorro.setTextColor(colorAhorro)
    }

    fun configurarBalanceSimple(balance: Double, titulo: String = "Balance") {
        textoTitulo.text = titulo
        textoBalance.text = FormatearMoneda.formatear(balance)
        textoIngresos.text = "-"
        textoGastos.text = "-"
        textoAhorro.text = "-"
    }

    fun mostrarCargando() {
        textoBalance.text = "..."
        textoIngresos.text = "..."
        textoGastos.text = "..."
        textoAhorro.text = "..."
    }
}