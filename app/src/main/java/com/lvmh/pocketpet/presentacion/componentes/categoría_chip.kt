package com.lvmh.pocketpet.presentacion.componentes

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.data.local.entidades.categoria_entidad

class CategoryChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val textoIcono: TextView
    private val textoNombre: TextView
    private var categoriaActual: categoria_entidad? = null
    private var estaSeleccionado = false

    init {
        LayoutInflater.from(context).inflate(R.layout.componente_category_chip, this, true)

        textoIcono = findViewById(R.id.textoIconoCategoria)
        textoNombre = findViewById(R.id.textoNombreCategoria)

        radius = 20f
        cardElevation = 2f
        useCompatPadding = true
    }

    fun configurar(categoria: categoria_entidad, seleccionado: Boolean = false) {
        categoriaActual = categoria
        estaSeleccionado = seleccionado

        textoIcono.text = categoria.icono
        textoNombre.text = categoria.nombre

        // Aplicar color de la categoría
        try {
            val color = Color.parseColor(categoria.color)
            if (seleccionado) {
                setCardBackgroundColor(color)
                textoNombre.setTextColor(Color.WHITE)
                textoIcono.setTextColor(Color.WHITE)
            } else {
                setCardBackgroundColor(context.getColor(R.color.fondo_secundario))
                textoNombre.setTextColor(color)
                textoIcono.setTextColor(color)
            }
        } catch (e: Exception) {
            setCardBackgroundColor(context.getColor(R.color.fondo_secundario))
        }

        cardElevation = if (seleccionado) 6f else 2f
    }

    fun seleccionar() {
        categoriaActual?.let { configurar(it, true) }
    }

    fun deseleccionar() {
        categoriaActual?.let { configurar(it, false) }
    }

    fun estaSeleccionado(): Boolean = estaSeleccionado

    fun obtenerCategoria(): categoria_entidad? = categoriaActual
}