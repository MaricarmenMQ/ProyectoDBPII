package com.lvmh.pocketpet.presentacion.pantallas.estados
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import com.lvmh.pocketpet.R

@AndroidEntryPoint
class actividad_tendencias : AppCompatActivity() {

    private val idUsuario = "usuario_123" // TODO: Obtener del usuario autenticado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_tendencias)

        configurarInterfaz()
        cargarDatosTendencias()
    }

    private fun configurarInterfaz() {
        // Configurar la barra superior (toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Tendencias"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar el selector de período
        configurarSelectorPeriodo()
    }

    private fun configurarSelectorPeriodo() {
        // RadioGroup para seleccionar el período (Mes, Trimestre, Año)
        val grupoRadioPeriodo = findViewById<android.widget.RadioGroup>(R.id.grupo_periodo)
        grupoRadioPeriodo?.setOnCheckedChangeListener { _, idSeleccionado ->
            when (idSeleccionado) {
                R.id.radio_mes -> cargarDatosTendencias("mes")
                R.id.radio_trimestre -> cargarDatosTendencias("trimestre")
                R.id.radio_ano -> cargarDatosTendencias("anio")
            }
        }
    }

    private fun cargarDatosTendencias(periodo: String = "mes") {
        // TODO: Cargar los datos de tendencias según el período seleccionado
        // Mostrar gráfico de línea con gastos e ingresos
        // Mostrar promedio, máximo y mínimo
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
