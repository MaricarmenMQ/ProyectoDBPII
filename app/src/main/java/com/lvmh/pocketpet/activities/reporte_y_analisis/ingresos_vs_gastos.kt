package com.lvmh.pocketpet.reporte_y_analisis

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lvmh.pocketpet.R

class ingresos_vs_gastos : AppCompatActivity() {

    private lateinit var texto_ingresos_este_mes: TextView
    private lateinit var texto_gastos_este_mes: TextView
    private lateinit var texto_diferencia: TextView
    private lateinit var texto_porcentaje_ingresos: TextView
    private lateinit var texto_porcentaje_gastos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_comparativa)

        inicializar_vistas()
        cargar_datos_comparacion()
    }

    private fun inicializar_vistas() {
        texto_ingresos_este_mes = findViewById(R.id.texto_ingresos_este_mes)
        texto_gastos_este_mes = findViewById(R.id.texto_gastos_este_mes)
        texto_diferencia = findViewById(R.id.texto_diferencia)
        texto_porcentaje_ingresos = findViewById(R.id.texto_porcentaje_ingresos)
        texto_porcentaje_gastos = findViewById(R.id.texto_porcentaje_gastos)
    }

    private fun cargar_datos_comparacion() {
        val ingresos_este_mes = 5500.0
        val gastos_este_mes = 3200.0
        val diferencia = ingresos_este_mes - gastos_este_mes
        val total = ingresos_este_mes + gastos_este_mes

        val porcentaje_ingresos = (ingresos_este_mes / total) * 100
        val porcentaje_gastos = (gastos_este_mes / total) * 100

        texto_ingresos_este_mes.text = "S/ ${formatear_dinero(ingresos_este_mes)}"
        texto_gastos_este_mes.text = "S/ ${formatear_dinero(gastos_este_mes)}"
        texto_diferencia.text = "S/ ${formatear_dinero(diferencia)}"
        texto_porcentaje_ingresos.text = "${String.format("%.1f", porcentaje_ingresos)}%"
        texto_porcentaje_gastos.text = "${String.format("%.1f", porcentaje_gastos)}%"

        if (diferencia >= 0) {
            texto_diferencia.setTextColor(getColor(R.color.verde_ingresos))
        } else {
            texto_diferencia.setTextColor(getColor(R.color.rojo_gastos))
        }
    }

    private fun formatear_dinero(monto: Double): String {
        return String.format("%.2f", monto)
    }
}