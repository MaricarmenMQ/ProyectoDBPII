package com.lvmh.pocketpet.presentacion.pantallas.estados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Button
import dagger.hilt.android.AndroidEntryPoint
import com.lvmh.pocketpet.R

@AndroidEntryPoint
class actividad_reportes : AppCompatActivity() {

    private val idUsuario = "usuario_123" // TODO: Obtener del usuario autenticado

    private lateinit var spinnerTipoReporte: Spinner
    private lateinit var spinnerPeriodo: Spinner
    private lateinit var botonExportarReporte: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_reportes)

        inicializarVistas()
        configurarUI()
    }

    private fun inicializarVistas() {
        spinnerTipoReporte = findViewById(R.id.spinnerTipoReporte)
        spinnerPeriodo = findViewById(R.id.spinnerPeriodo)
        botonExportarReporte = findViewById(R.id.botonExportarReporte)
    }

    private fun configurarUI() {
        // Configurar la barra de herramientas
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = getString(R.string.titulo_reportes)
            setDisplayHomeAsUpEnabled(true)
        }

        // Configurar el spinner de tipo de reporte
        configurarSpinnerTipoReporte()

        // Configurar el spinner de período
        configurarSpinnerPeriodo()

        // Botón para exportar o descargar
        botonExportarReporte.setOnClickListener {
            exportarReporte()
        }
    }

    private fun configurarSpinnerTipoReporte() {
        val tiposReporte = arrayOf(
            getString(R.string.tipo_ingresos_gastos),
            getString(R.string.tipo_por_categoria),
            getString(R.string.tipo_por_cuenta),
            getString(R.string.tipo_presupuesto_real)
        )
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            tiposReporte
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoReporte.adapter = adaptador
    }

    private fun configurarSpinnerPeriodo() {
        val periodos = arrayOf(
            getString(R.string.este_mes),
            getString(R.string.mes_anterior),
            getString(R.string.este_trimestre),
            getString(R.string.este_ano),
            getString(R.string.personalizado)
        )
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            periodos
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPeriodo.adapter = adaptador
    }

    private fun exportarReporte() {
        // TODO: Obtener selecciones del spinner
        val tipoReporteSeleccionado = spinnerTipoReporte.selectedItem.toString()
        val periodoSeleccionado = spinnerPeriodo.selectedItem.toString()

        // TODO: Generar reporte en PDF o Excel
        // TODO: Guardar en almacenamiento del dispositivo
        // TODO: Mostrar notificación de éxito al usuario
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}