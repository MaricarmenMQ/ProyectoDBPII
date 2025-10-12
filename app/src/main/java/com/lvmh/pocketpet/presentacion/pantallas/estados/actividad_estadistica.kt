package com.lvmh.pocketpet.presentacion.pantallas.estados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.PresupuestoVerModelo
import com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.MetaVerModelo
import androidx.activity.viewModels


@AndroidEntryPoint
class actividad_estadistica : AppCompatActivity() {

    private val presupuestoVerModelo: PresupuestoVerModelo by viewModels()
    private val metaVerModelo: MetaVerModelo by viewModels()

    private val idUsuario = "usuario_123" // Reemplaza con el ID de usuario real

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_estadistica)

        configurarInterfaz()
        cargarEstadisticas()
    }

    private fun configurarInterfaz() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Estadisticas"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun cargarEstadisticas() {
        lifecycleScope.launch {
            presupuestoVerModelo.cargar_estadisticas_presupuesto(idUsuario)
            presupuestoVerModelo.estadisticasPresupuesto.collect { estadisticas ->
                actualizarUIPresupuesto(estadisticas)
            }
        }

        lifecycleScope.launch {
            MetaVerModelo.cargarEstadisticasMetas(idUsuario)
            MetaVerModelo.estadisticasMeta.collect { estadisticas ->
                actualizarUIMeta(estadisticas)
            }
        }
    }

    private fun actualizarUIPresupuesto(estadisticas: com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.EstadisticasPresupuesto?) {
        estadisticas?.let {
            findViewById<android.widget.TextView>(R.id.textoTotalPresupuestos)?.text =
                "${it.totalPresupuestos} presupuestos"

            findViewById<android.widget.TextView>(R.id.textoMontoTotal)?.text =
                "$${String.format("%.2f", it.montoTotal)}"

            findViewById<android.widget.TextView>(R.id.textoTotalGastado)?.text =
                "$${String.format("%.2f", it.totalGastado)}"

            findViewById<android.widget.ProgressBar>(R.id.barraProgresoPresupuesto)?.progress =
                it.porcentajeUsado.toInt()

            findViewById<android.widget.TextView>(R.id.textoPorcentajePresupuesto)?.text =
                "${String.format("%.1f", it.porcentajeUsado)}% gastado"
        }
    }

    private fun actualizarUIMeta(estadisticas: com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.EstadisticasMeta?) {
        estadisticas?.let {
            // Actualizar vistas con estadisticas de metas
            findViewById<android.widget.TextView>(R.id.textoTotalMetas)?.text =
                "${it.totalMetas} metas"

            findViewById<android.widget.TextView>(R.id.textoMetasCompletadas)?.text =
                "${it.metasCompletadas} completadas"

            findViewById<android.widget.TextView>(R.id.textoTasaCumplimiento)?.text =
                "${String.format("%.1f", it.tasaCumplimiento)}%"

            findViewById<android.widget.ProgressBar>(R.id.barraProgresoMetas)?.progress =
                it.tasaCumplimiento.toInt()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

