package com.lvmh.pocketpet.presentacion.componentes

package com.financepet.presentacion.componentes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.financepet.R
import kotlin.math.ceil

/**
 * BarraDeProgresoPersonalizada - Componente personalizado para mostrar barras de progreso avanzadas
 * Soporta animación, colores dinámicos y etiquetas
 */
class BarraDeProgresoPersonalizada @JvmOverloads constructor(
    contexto: Context,
    atributos: AttributeSet? = null,
    estiloDefecto: Int = 0
) : View(contexto, atributos, estiloDefecto) {

    private val pinturaFondo = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pinturaProgreso = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pinturaTexto = Paint(Paint.ANTI_ALIAS_FLAG)

    private var progreso = 0
    private var progresoMaximo = 100
    private var mostrarPorcentaje = true
    private var mostrarEtiqueta = true
    private var etiqueta = "Progreso"
    private var duracionAnimacion = 1000L
    private var tiempoInicio = 0L
    private var progresoObjetivo = 0

    private val radioEsquina = 12f
    private val relleno = 16f
    private val alturaEtiqueta = 30f

    enum class EstiloProgreso {
        LINEAL,     // Barra simple
        DEGRADADO,  // Barra con degradado
        SEGMENTADA  // Barra segmentada
    }

    private var estiloProgreso = EstiloProgreso.LINEAL

    init {
        // Configurar pinceles
        pinturaFondo.apply {
            color = ContextCompat.getColor(contexto, android.R.color.darker_gray)
            style = Paint.Style.FILL
        }

        pinturaProgreso.apply {
            color = ContextCompat.getColor(contexto, R.color.colorPrimary)
            style = Paint.Style.FILL
        }

        pinturaTexto.apply {
            color = Color.BLACK
            textSize = 36f
            textAlign = Paint.Align.CENTER
        }

        // Leer atributos personalizados
        atributos?.let { leerAtributos(it) }
    }

    private fun leerAtributos(atributos: AttributeSet) {
        val tipoArray = context.obtainStyledAttributes(atributos, R.styleable.CustomProgressBar)

        tipoArray.apply {
            progreso = getInt(R.styleable.CustomProgressBar_progress, 0)
            progresoMaximo = getInt(R.styleable.CustomProgressBar_maxProgress, 100)
            mostrarPorcentaje = getBoolean(R.styleable.CustomProgressBar_showPercentage, true)
            mostrarEtiqueta = getBoolean(R.styleable.CustomProgressBar_showLabel, true)
            etiqueta = getString(R.styleable.CustomProgressBar_label) ?: "Progreso"
            val valorEstilo = getInt(R.styleable.CustomProgressBar_progressStyle, 0)
            estiloProgreso = EstiloProgreso.values().getOrNull(valorEstilo) ?: EstiloProgreso.LINEAL

            recycle()
        }
    }

    /**
     * Establece el progreso actual
     */
    fun establecerProgreso(nuevoProgreso: Int) {
        progreso = nuevoProgreso.coerceIn(0, progresoMaximo)
        invalidate()
    }

    /**
     * Anima el progreso hacia un valor objetivo
     */
    fun animarProgreso(objetivo: Int) {
        progresoObjetivo = objetivo.coerceIn(0, progresoMaximo)
        tiempoInicio = System.currentTimeMillis()
        animarFotograma()
    }

    /**
     * Establece el progreso máximo
     */
    fun establecerProgresoMaximo(max: Int) {
        progresoMaximo = max.coerceAtLeast(1)
        progreso = progreso.coerceIn(0, progresoMaximo)
        invalidate()
    }

    /**
     * Establece la etiqueta de la barra
     */
    fun establecerEtiqueta(texto: String) {
        etiqueta = texto
        invalidate()
    }

    /**
     * Establece el color del progreso
     */
    fun establecerColorProgreso(color: Int) {
        pinturaProgreso.color = color
        invalidate()
    }

    /**
     * Establece el color del fondo
     */
    fun establecerColorFondo(color: Int) {
        pinturaFondo.color = color
        invalidate()
    }

    /**
     * Establece el estilo de progreso
     */
    fun establecerEstiloProgreso(estilo: EstiloProgreso) {
        estiloProgreso = estilo
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val inicioY = if (mostrarEtiqueta) alturaEtiqueta + relleno else relleno
        val alturaBarra = 20f

        // Dibujar etiqueta
        if (mostrarEtiqueta) {
            pinturaTexto.textSize = 24f
            canvas.drawText(etiqueta, width / 2f, alturaEtiqueta, pinturaTexto)
        }

        // Dibujar fondo
        val rectFondo = RectF(
            relleno,
            inicioY,
            width - relleno,
            inicioY + alturaBarra
        )
        canvas.drawRoundRect(rectFondo, radioEsquina, radioEsquina, pinturaFondo)

        // Dibujar progreso
        val anchoProgreso = ((progreso.toFloat() / progresoMaximo) * (width - 2 * relleno))
        val rectProgreso = RectF(
            relleno,
            inicioY,
            relleno + anchoProgreso,
            inicioY + alturaBarra
        )

        when (estiloProgreso) {
            EstiloProgreso.LINEAL -> dibujarProgresoLineal(canvas, rectProgreso)
            EstiloProgreso.DEGRADADO -> dibujarProgresoDegradado(canvas, rectProgreso)
            EstiloProgreso.SEGMENTADA -> dibujarProgresoSegmentado(canvas, rectProgreso)
        }

        // Dibujar porcentaje
        if (mostrarPorcentaje) {
            pinturaTexto.apply {
                textSize = 28f
                color = Color.WHITE
            }
            val porcentaje = ((progreso.toFloat() / progresoMaximo) * 100).toInt()
            canvas.drawText("$porcentaje%", width / 2f, inicioY + alturaBarra + 40, pinturaTexto)
        }
    }

    private fun dibujarProgresoLineal(canvas: Canvas, rect: RectF) {
        canvas.drawRoundRect(rect, radioEsquina, radioEsquina, pinturaProgreso)
    }

    private fun dibujarProgresoDegradado(canvas: Canvas, rect: RectF) {
        // Crear efecto degradado con múltiples rectángulos
        val segmentos = 10
        val anchoSegmento = rect.width() / segmentos

        for (i in 0 until segmentos) {
            val sombra = 255 - (i * (255 / segmentos))
            pinturaProgreso.alpha = sombra

            val rectSegmento = RectF(
                rect.left + i * anchoSegmento,
                rect.top,
                rect.left + (i + 1) * anchoSegmento,
                rect.bottom
            )
            canvas.drawRect(rectSegmento, pinturaProgreso)
        }

        pinturaProgreso.alpha = 255
    }

    private fun dibujarProgresoSegmentado(canvas: Canvas, rect: RectF) {
        val segmentos = 10
        val espacio = 2f
        val anchoSegmento = (rect.width() - (segmentos - 1) * espacio) / segmentos

        for (i in 0 until segmentos) {
            val rectSegmento = RectF(
                rect.left + i * (anchoSegmento + espacio),
                rect.top,
                rect.left + i * (anchoSegmento + espacio) + anchoSegmento,
                rect.bottom
            )
            canvas.drawRoundRect(rectSegmento, 4f, 4f, pinturaProgreso)
        }
    }

    private fun animarFotograma() {
        val transcurrido = System.currentTimeMillis() - tiempoInicio
        val fraccion = (transcurrido.toFloat() / duracionAnimacion).coerceIn(0f, 1f)

        progreso = (progreso + (progresoObjetivo - progreso) * fraccion).toInt()

        if (fraccion < 1f) {
            postDelayed({ animarFotograma() }, 16) // ~60 FPS
        } else {
            progreso = progresoObjetivo
        }

        invalidate()
    }
}

/**
 * StyleableRes para BarraDeProgresoPersonalizada (agregar a attrs.xml)
 */
/*
<declare-styleable name="CustomProgressBar">
    <attr name="progress" format="integer" />
    <attr name="maxProgress" format="integer" />
    <attr name="showPercentage" format="boolean" />
    <attr name="showLabel" format="boolean" />
    <attr name="label" format="string" />
    <attr name="progressStyle" format="enum">
        <enum name="linear" value="0" />
        <enum name="gradient" value="1" />
        <enum name="segmented" value="2" />
    </attr>
</declare-styleable>
*/

