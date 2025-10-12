package com.lvmh.pocketpet.presentacion.pantallas.estados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import dagger.hilt.android.AndroidEntryPoint
import com.lvmh.pocketpet.R

@AndroidEntryPoint
class actividad_comparar : AppCompatActivity() {

    private val idUsuario = "usuario_123" // TODO: Obtener del usuario autenticado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_comparar)

        configurarUI()
    }

    private fun configurarUI() {
        // Configurar barra de herramientas
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Comparativas"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar opciones de comparación
        configurarOpcionesComparacion()
    }

    private fun configurarOpcionesComparacion() {
        val grupoComparacion = findViewById<RadioGroup>(R.id.grupoComparacion)
        grupoComparacion?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioMesVsMes -> cargarComparacion("mesVsMes")
                R.id.radioAnioVsAnio -> cargarComparacion("anioVsAnio")
                R.id.radioPresupuestoVsReal -> cargarComparacion("presupuestoVsReal")
                R.id.radioComparativaCategorias -> cargarComparacion("comparacionCategorias")
            }
        }
    }

    private fun cargarComparacion(tipoComparacion: String) {
        when (tipoComparacion) {
            "mesVsMes" -> {
                // Comparar mes actual vs mes anterior
                // Mostrar diferencias en gastos/ingresos
            }
            "anioVsAnio" -> {
                // Comparar año actual vs año anterior
                // Mostrar tendencias anuales
            }
            "presupuestoVsReal" -> {
                // Comparar presupuesto vs gasto real
                // Mostrar desviaciones
            }
            "comparacionCategorias" -> {
                // Comparar categorías entre períodos
                // Mostrar cuál gastó más/menos
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
