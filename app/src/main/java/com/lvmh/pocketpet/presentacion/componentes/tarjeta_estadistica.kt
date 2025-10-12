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
import kotlin.math.ceil

class barra_progreso @JvmOverloads constructor(
    contexto: Context,
    atributos: AttributeSet? = null,
    estiloDefecto: Int = 0
) : View(contexto, atributos, estiloDefecto) {

    private val pincelFondo = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pincelProgreso = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pincelTexto = Paint(Paint.ANTI_ALIAS_FLAG)

    private var progreso = 0
    private var progresoMaximo = 100
    private var mostrarPorcentaje = true
    private var mostrarEtiqueta = true
    private var etiqueta = "Progreso"
    private var duracionAnimacion = 1000L
    private var tiempoInicio = 0L
    private var progresoObjetivo = 0

    private val radioEsquina = 12f
    private val margenInterno = 16f
    private val alturaEtiqueta = 30f

    enum class EstiloProgreso {
        LINEAL,     // Barra simple
        DEGRADADO,  // Barra con degradado
        SEGMENTADA  // Barra segmentada
    }

    private var estiloProgreso = EstiloProgreso.LINEAL

    init {
        // Configurar pinceles
        pincelFondo.apply {
            color = ContextCompat.getColor(contexto, R.color.fondo_terciario)
            style = Paint.Style.FILL
        }

        pincelProgreso.apply {
            color = ContextCompat.getColor(contexto, R.color.color_primario)
            style = Paint.Style.FILL
        }

        pincelTexto.apply {
            color = ContextCompat.getColor(contexto, R.color.texto_primario)
            textSize = 36f
            textAlign = Paint.Align.CENTER
        }

        // Leer atributos personalizados
        atributos?.let { leerAtributos(it) }
    }

    private fun leerAtributos(atributos: AttributeSet) {
        val tipoArray = context.obtainStyledAttributes(atributos, R.styleable.barra_progreso)

        tipoArray.apply {
            progreso = getInt(R.styleable.barra_progreso_progreso, 0)
            progresoMaximo = getInt(R.styleable.barra_progreso_progresoMaximo, 100)
            mostrarPorcentaje = getBoolean(R.styleable.barra_progreso_mostrarPorcentaje, true)
            mostrarEtiqueta = getBoolean(R.styleable.barra_progreso_mostrarEtiqueta, true)
            etiqueta = getString(R.styleable.barra_progreso_etiqueta) ?: "Progreso"
            val valorEstilo = getInt(R.styleable.barra_progreso_estiloProgreso, 0)
            estiloProgreso = EstiloProgreso.values().getOrNull(valorEstilo) ?: EstiloProgreso.LINEAL

            recycle()
        }
    }

    fun establecerProgreso(nuevoProgreso: Int) {
        progreso = nuevoProgreso.coerceIn(0, progresoMaximo)
        invalidate()
    }

    fun animarProgreso(objetivo: Int) {
        progresoObjetivo = objetivo.coerceIn(0, progresoMaximo)
        tiempoInicio = System.currentTimeMillis()
        animarFrame()
    }

    fun establecerProgresoMaximo(maximo: Int) {
        progresoMaximo = maximo.coerceAtLeast(1)
        progreso = progreso.coerceIn(0, progresoMaximo)
        invalidate()
    }

    fun establecerEtiqueta(texto: String) {
        etiqueta = texto
        invalidate()
    }

    fun establecerColorProgreso(color: Int) {
        pincelProgreso.color = color
        invalidate()
    }

    fun establecerColorFondo(color: Int) {
        pincelFondo.color = color
        invalidate()
    }

    fun establecerEstiloProgreso(estilo: EstiloProgreso) {
        estiloProgreso = estilo
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val inicioY = if (mostrarEtiqueta) alturaEtiqueta + margenInterno else margenInterno
        val alturaBarra = 20f

        // Dibujar etiqueta
        if (mostrarEtiqueta) {
            pincelTexto.textSize = 24f
            canvas.drawText(etiqueta, width / 2f, alturaEtiqueta, pincelTexto)
        }

        // Dibujar fondo
        val rectFondo = RectF(
            margenInterno,
            inicioY,
            width - margenInterno,
            inicioY + alturaBarra
        )
        canvas.drawRoundRect(rectFondo, radioEsquina, radioEsquina, pincelFondo)

        // Dibujar progreso
        val anchoProgreso = ((progreso.toFloat() / progresoMaximo) * (width - 2 * margenInterno))
        val rectProgreso = RectF(
            margenInterno,
            inicioY,
            margenInterno + anchoProgreso,
            inicioY + alturaBarra
        )

        when (estiloProgreso) {
            EstiloProgreso.LINEAL -> dibujarProgresoLineal(canvas, rectProgreso)
            EstiloProgreso.DEGRADADO -> dibujarProgresoDegradado(canvas, rectProgreso)
            EstiloProgreso.SEGMENTADA -> dibujarProgresoSegmentado(canvas, rectProgreso)
        }

        // Dibujar porcentaje
        if (mostrarPorcentaje) {
            pincelTexto.apply {
                textSize = 28f
                color = ContextCompat.getColor(context, R.color.texto_sobre_color)
            }
            val porcentaje = ((progreso.toFloat() / progresoMaximo) * 100).toInt()
            canvas.drawText("$porcentaje%", width / 2f, inicioY + alturaBarra + 40, pincelTexto)
        }
    }

    private fun dibujarProgresoLineal(canvas: Canvas, rect: RectF) {
        canvas.drawRoundRect(rect, radioEsquina, radioEsquina, pincelProgreso)
    }

    private fun dibujarProgresoDegradado(canvas: Canvas, rect: RectF) {
        // Crear efecto degradado con múltiples rectángulos
        val segmentos = 10
        val anchoSegmento = rect.width() / segmentos

        for (i in 0 until segmentos) {
            val tono = 255 - (i * (255 / segmentos))
            pincelProgreso.alpha = tono

            val rectSegmento = RectF(
                rect.left + i * anchoSegmento,
                rect.top,
                rect.left + (i + 1) * anchoSegmento,
                rect.bottom
            )
            canvas.drawRect(rectSegmento, pincelProgreso)
        }

        pincelProgreso.alpha = 255
    }

    private fun dibujarProgresoSegmentado(canvas: Canvas, rect: RectF) {
        val segmentos = 10
        val separacion = 2f
        val anchoSegmento = (rect.width() - (segmentos - 1) * separacion) / segmentos

        for (i in 0 until segmentos) {
            val rectSegmento = RectF(
                rect.left + i * (anchoSegmento + separacion),
                rect.top,
                rect.left + i * (anchoSegmento + separacion) + anchoSegmento,
                rect.bottom
            )
            canvas.drawRoundRect(rectSegmento, 4f, 4f, pincelProgreso)
        }
    }

    private fun animarFrame() {
        val transcurrido = System.currentTimeMillis() - tiempoInicio
        val fraccion = (transcurrido.toFloat() / duracionAnimacion).coerceIn(0f, 1f)

        progreso = (progreso + (progresoObjetivo - progreso) * fraccion).toInt()

        if (fraccion < 1f) {
            postDelayed({ animarFrame() }, 16) // ~60 FPS
        } else {
            progreso = progresoObjetivo
        }

        invalidate()
    }
}