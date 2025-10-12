package com.lvmh.pocketpet.presentacion.componentes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import com.lvmh.pocketpet.dominio.utilidades.FormatearMoneda
import com.lvmh.pocketpet.dominio.utilidades.DateUtils

class TransactionCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val iconoCategoria: ImageView
    private val textoDescripcion: TextView
    private val textoMonto: TextView
    private val textoFecha: TextView
    private val textoCategoria: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.componente_transaction_card, this, true)

        iconoCategoria = findViewById(R.id.iconoCategoria)
        textoDescripcion = findViewById(R.id.textoDescripcion)
        textoMonto = findViewById(R.id.textoMonto)
        textoFecha = findViewById(R.id.textoFecha)
        textoCategoria = findViewById(R.id.textoCategoria)

        radius = 12f
        cardElevation = 4f
        useCompatPadding = true
    }

    fun configurar(transaccion: transaccion_entidad) {
        // Descripción
        textoDescripcion.text = transaccion.descripcion

        // Monto con signo según tipo
        val montoFormateado = FormatearMoneda.formatear(transaccion.monto)
        textoMonto.text = if (transaccion.tipo.lowercase() == "ingreso") {
            "+$montoFormateado"
        } else {
            "-$montoFormateado"
        }

        // Color según tipo
        val colorMonto = if (transaccion.tipo.lowercase() == "ingreso") {
            context.getColor(R.color.color_ingreso)
        } else {
            context.getColor(R.color.color_gasto)
        }
        textoMonto.setTextColor(colorMonto)

        // Fecha formateada
        textoFecha.text = DateUtils.formatear_relativo(transaccion.fecha)

        // Categoría (por ahora ID, luego conectar con categoría real)
        textoCategoria.text = "Cat: ${transaccion.id_categoria}"

        // Icono según tipo
        val iconoRes = if (transaccion.tipo.lowercase() == "ingreso") {
            R.drawable.ic_ingreso
        } else {
            R.drawable.ic_gasto
        }
        iconoCategoria.setImageResource(iconoRes)
    }

    fun configurarClickListener(onClick: () -> Unit) {
        setOnClickListener { onClick() }
    }
}