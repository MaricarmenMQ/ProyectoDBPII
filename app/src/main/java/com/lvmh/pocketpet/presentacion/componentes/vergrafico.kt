package com.lvmh.pocketpet.presentacion.componentes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.lvmh.pocketpet.R
import kotlin.math.max
import kotlin.math.min

class vergrafico @JvmOverloads constructor(
    contexto: Context,
    atributos: AttributeSet? = null,
    estiloDefecto: Int = 0
) : View(contexto, atributos, estiloDefecto) {

    private val pinturaBarra = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pinturaTexto = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pinturaCuadricula = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pinturaEje = Paint(Paint.ANTI_ALIAS_FLAG)

    private var datos: List<DatoGrafico> = emptyList()
    private var tipoGrafico: TipoGrafico = TipoGrafico.BARRA

    private val margen = 60f
    private val margenEtiqueta = 40f
    private var valorMaximo = 100f

    enum class TipoGrafico {
        BARRA, LINEA, CIRCULAR
    }

    data class DatoGrafico(
        val etiqueta: String,
        val valor: Float,
        val color: Int = Color.BLUE
    )

    init {
        pinturaTexto.apply {
            color = ContextCompat.getColor(contexto, R.color.texto_primario)
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }

        pinturaCuadricula.apply {
            color = ContextCompat.getColor(contexto, R.color.grafico_grid)
            strokeWidth = 1f
        }

        pinturaEje.apply {
            color = ContextCompat.getColor(contexto, R.color.texto_primario)
            strokeWidth = 2f
        }
    }

    fun establecerDatos(datosGrafico: List<DatoGrafico>, tipo: TipoGrafico = TipoGrafico.BARRA) {
        datos = datosGrafico
        tipoGrafico = tipo
        valorMaximo = (datosGrafico.maxOfOrNull { it.valor } ?: 100f) * 1.2f
        invalidate()
    }

    fun agregarDato(dato: DatoGrafico) {
        this.datos = this.datos + dato
        valorMaximo = (this.datos.maxOfOrNull { it.valor } ?: 100f) * 1.2f
        invalidate()
    }

    fun limpiar() {
        datos = emptyList()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (datos.isEmpty()) {
            dibujarEstadoVacio(canvas)
            return
        }

        when (tipoGrafico) {
            TipoGrafico.BARRA -> dibujarGraficoBarras(canvas)
            TipoGrafico.LINEA -> dibujarGraficoLineas(canvas)
            TipoGrafico.CIRCULAR -> dibujarGraficoCircular(canvas)
        }
    }

    private fun dibujarGraficoBarras(canvas: Canvas) {
        val ancho = width - 2 * margen
        val alto = height - 2 * margen - margenEtiqueta

        val anchoBarra = ancho / (datos.size * 1.5f)
        val espacio = anchoBarra * 0.5f

        pinturaEje.color = ContextCompat.getColor(context, R.color.texto_primario)
        canvas.drawLine(margen, alto + margen, ancho + margen, alto + margen, pinturaEje)
        canvas.drawLine(margen, margen, margen, alto + margen, pinturaEje)

        datos.forEachIndexed { indice, dato ->
            val alturaBarra = (dato.valor / valorMaximo) * alto
            val x = margen + indice * (anchoBarra + espacio) + espacio / 2
            val y = alto + margen - alturaBarra

            pinturaBarra.color = dato.color
            canvas.drawRect(x, y, x + anchoBarra, alto + margen, pinturaBarra)

            pinturaTexto.textSize = 30f
            pinturaTexto.color = ContextCompat.getColor(context, R.color.texto_primario)
            canvas.drawText(
                dato.etiqueta,
                x + anchoBarra / 2,
                alto + margen + margenEtiqueta - 10,
                pinturaTexto
            )

            pinturaTexto.textSize = 28f
            canvas.drawText(
                "${dato.valor.toInt()}",
                x + anchoBarra / 2,
                y - 10,
                pinturaTexto
            )
        }
    }

    private fun dibujarGraficoLineas(canvas: Canvas) {
        if (datos.size < 2) return

        val ancho = width - 2 * margen
        val alto = height - 2 * margen - margenEtiqueta

        val pasoX = ancho / (datos.size - 1)

        pinturaBarra.apply {
            color = ContextCompat.getColor(context, R.color.color_primario)
            strokeWidth = 4f
        }

        for (i in 0 until datos.size - 1) {
            val x1 = margen + i * pasoX
            val y1 = margen + alto - (datos[i].valor / valorMaximo) * alto

            val x2 = margen + (i + 1) * pasoX
            val y2 = margen + alto - (datos[i + 1].valor / valorMaximo) * alto

            canvas.drawLine(x1, y1, x2, y2, pinturaBarra)
        }

        datos.forEachIndexed { indice, dato ->
            val x = margen + indice * pasoX
            val y = margen + alto - (dato.valor / valorMaximo) * alto

            pinturaBarra.color = dato.color
            canvas.drawCircle(x, y, 8f, pinturaBarra)
        }
    }

    private fun dibujarGraficoCircular(canvas: Canvas) {
        val centroX = width / 2f
        val centroY = height / 2f
        val radio = min(width, height) / 2f - 50

        var anguloInicio = -90f
        val total = datos.sumOf { it.valor.toDouble() }

        datos.forEach { dato ->
            val barrido = (dato.valor / total * 360).toFloat()

            pinturaBarra.color = dato.color
            val rectF = RectF(
                centroX - radio,
                centroY - radio,
                centroX + radio,
                centroY + radio
            )

            canvas.drawArc(rectF, anguloInicio, barrido, true, pinturaBarra)

            anguloInicio += barrido
        }
    }

    private fun dibujarEstadoVacio(canvas: Canvas) {
        pinturaTexto.apply {
            color = ContextCompat.getColor(context, R.color.texto_secundario)
            textSize = 50f
        }
        canvas.drawText("Sin datos", width / 2f, height / 2f, pinturaTexto)
    }
}