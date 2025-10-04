package com.lvmh.pocketpet.reporte_y_analisis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lvmh.pocketpet.R

class dashboard_estadistica : AppCompatActivity() {

    private lateinit var texto_balance_total: TextView
    private lateinit var texto_ingresos_totales: TextView
    private lateinit var texto_gastos_totales: TextView
    private lateinit var texto_cantidad_transacciones: TextView

    private lateinit var boton_ingresos_vs_gastos: Button
    private lateinit var boton_analisis_categorias: Button
    private lateinit var boton_configurar_presupuesto: Button
    private lateinit var boton_tendencias: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_principal)

        inicializar_vistas()
        cargar_estadisticas()
        configurar_botones()
    }

    private fun inicializar_vistas() {
        texto_balance_total = findViewById(R.id.texto_balance_total)
        texto_ingresos_totales = findViewById(R.id.texto_ingresos_totales)
        texto_gastos_totales = findViewById(R.id.texto_gastos_totales)
        texto_cantidad_transacciones = findViewById(R.id.texto_cantidad_transacciones)

        boton_ingresos_vs_gastos = findViewById(R.id.boton_ingresos_vs_gastos)
        boton_analisis_categorias = findViewById(R.id.boton_analisis_categorias)
        boton_configurar_presupuesto = findViewById(R.id.boton_configurar_presupuesto)
        boton_tendencias = findViewById(R.id.boton_tendencias)
    }

    private fun cargar_estadisticas() {
        val ingresos = 5500.0
        val gastos = 3200.0
        val balance = ingresos - gastos
        val cantidad_transacciones = 48

        texto_balance_total.text = "S/ ${formatear_dinero(balance)}"
        texto_ingresos_totales.text = "S/ ${formatear_dinero(ingresos)}"
        texto_gastos_totales.text = "S/ ${formatear_dinero(gastos)}"
        texto_cantidad_transacciones.text = "$cantidad_transacciones transacciones este mes"

        cambiar_color_balance(balance)
    }

    private fun configurar_botones() {
        boton_ingresos_vs_gastos.setOnClickListener {
            startActivity(Intent(this, ingresos_vs_gastos::class.java))
        }

        boton_analisis_categorias.setOnClickListener {
            startActivity(Intent(this, analisis_categoria::class.java))
        }

        boton_configurar_presupuesto.setOnClickListener {
            startActivity(Intent(this, configurar_presupuesto::class.java))
        }

        boton_tendencias.setOnClickListener {
            startActivity(Intent(this, tendencia_patrones::class.java))
        }
    }

    private fun formatear_dinero(monto: Double): String {
        return String.format("%.2f", monto)
    }

    private fun cambiar_color_balance(balance: Double) {
        if (balance >= 0) {
            texto_balance_total.setTextColor(getColor(R.color.verde_ingresos))
        } else {
            texto_balance_total.setTextColor(getColor(R.color.rojo_gastos))
        }
    }
}