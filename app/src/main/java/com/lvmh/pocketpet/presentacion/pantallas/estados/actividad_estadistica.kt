package com.lvmh.pocketpet.presentacion.pantallas.estados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.PresupuestoVerModelo
import com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.MetaVerModelo
import com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.EstadisticasPresupuesto
import com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.EstadisticasMeta
import androidx.activity.viewModels
import android.widget.TextView
import android.widget.ProgressBar

@AndroidEntryPoint
class actividad_estadistica : AppCompatActivity() {

    private val presupuestoVerModelo: PresupuestoVerModelo by viewModels()
    private val metaVerModelo: MetaVerModelo by viewModels()

    private val idUsuario = "usuario_123" // Reemplaza con el ID de usuario real

    // Vistas
    private var textoTotalPresupuestos: TextView? = null
    private var textoMontoTotal: TextView? = null
    private var textoTotalGastado: TextView? = null
    private var barraProgresoPresupuesto: ProgressBar? = null
    private var textoPorcentajePresupuesto: TextView? = null

    private var textoTotalMetas: TextView? = null
    private var textoMetasCompletadas: TextView? = null
    private var textoTasaCumplimiento: TextView? = null
    private var barraProgresoMetas: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_estadistica)

        inicializarVistas()
        configurarInterfaz()
        cargarEstadisticas()
    }

    private fun inicializarVistas() {
        textoTotalPresupuestos = findViewById(R.id.textoTotalPresupuestos)
        textoMontoTotal = findViewById(R.id.textoMontoTotal)
        textoTotalGastado = findViewById(R.id.textoTotalGastado)
        barraProgresoPresupuesto = findViewById(R.id.barraProgresoPresupuesto)
        textoPorcentajePresupuesto = findViewById(R.id.textoPorcentajePresupuesto)

        textoTotalMetas = findViewById(R.id.textoTotalMetas)
        textoMetasCompletadas = findViewById(R.id.textoMetasCompletadas)
        textoTasaCumplimiento = findViewById(R.id.textoTasaCumplimiento)
        barraProgresoMetas = findViewById(R.id.barraProgresoMetas)
    }

    private fun configurarInterfaz() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = getString(R.string.titulo_estadisticas)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun cargarEstadisticas() {
        cargarEstadisticasPresupuesto()
        cargarEstadisticasMetas()
    }

    private fun cargarEstadisticasPresupuesto() {
        lifecycleScope.launch {
            presupuestoVerModelo.cargar_estadisticas_presupuesto(idUsuario)
            presupuestoVerModelo.estadisticasPresupuesto.collect { estadisticas ->
                actualizarUIPresupuesto(estadisticas)
            }
        }
    }

    private fun cargarEstadisticasMetas() {
        lifecycleScope.launch {
            metaVerModelo.cargarEstadisticasMetas(idUsuario)
            metaVerModelo.estadisticasMetas.collect { estadisticas ->
                actualizarUIMeta(estadisticas)
            }
        }
    }

    private fun actualizarUIPresupuesto(estadisticas: EstadisticasPresupuesto?) {
        estadisticas?.let {
            textoTotalPresupuestos?.text = getString(
                R.string.presupuestos_cantidad,
                it.total_presupuestos
            )

            textoMontoTotal?.text = getString(
                R.string.moneda_soles,
                String.format("%.2f", it.monto_total)
            )

            textoTotalGastado?.text = getString(
                R.string.gastado_cantidad,
                String.format("%.2f", it.gastado_total)
            )

            barraProgresoPresupuesto?.apply {
                progress = it.porcentaje_usado.toInt()
                max = 100
            }

            textoPorcentajePresupuesto?.text = getString(
                R.string.porcentaje_gastado,
                String.format("%.1f", it.porcentaje_usado)
            )
        }
    }

    private fun actualizarUIMeta(estadisticas: EstadisticasMeta?) {
        estadisticas?.let {
            textoTotalMetas?.text = getString(
                R.string.metas_cantidad,
                it.totalMetas
            )

            textoMetasCompletadas?.text = getString(
                R.string.metas_completadas_cantidad,
                it.metasCompletadas
            )

            textoTasaCumplimiento?.text = getString(
                R.string.porcentaje,
                String.format("%.1f", it.tasaCompletado)
            )

            barraProgresoMetas?.apply {
                progress = it.tasaCompletado.toInt()
                max = 100
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}