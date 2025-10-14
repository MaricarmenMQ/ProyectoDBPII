package com.lvmh.pocketpet.presentacion.pantallas.core

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.databinding.ActividadAnadirTransaccionBinding
import com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.TransactionViewModel
import com.lvmh.pocketpet.presentacion.pantallas.vistamodelo.EstadoUiTransaccion
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class actividad_anadirtransaccion : AppCompatActivity() {

    private lateinit var vincular: ActividadAnadirTransaccionBinding
    private var tipoSeleccionado = "gasto"
    private var categoriaSeleccionada: Long = 0
    private var cuentaSeleccionada: Long = 1 // Por defecto cuenta 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vincular = ActividadAnadirTransaccionBinding.inflate(layoutInflater)
        setContentView(vincular.root)

        configurarToolbar()
        configurarTabs()
        configurarBotones()
        observarEstado()
    }

    private fun configurarToolbar() {
        setSupportActionBar(vincular.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.agregar_transaccion)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun configurarTabs() {
        vincular.tabLayoutTipo.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tipoSeleccionado = when (tab?.position) {
                    0 -> "gasto"
                    1 -> "ingreso"
                    else -> "gasto"
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun configurarBotones() {
        vincular.btnGuardarTransaccion.setOnClickListener {
            guardarTransaccion()
        }
    }

    private fun guardarTransaccion() {
        val monto = vincular.etMonto.text.toString().toDoubleOrNull()
        val descripcion = vincular.etDescripcion.text.toString()

        if (monto == null || monto <= 0) {
            vincular.etMonto.error = "Ingresa un monto válido"
            return
        }

        if (descripcion.isBlank()) {
            vincular.etDescripcion.error = "Ingresa una descripción"
            return
        }

        verModelo.crear_transaccion(
            tipo = tipoSeleccionado,
            monto = monto,
            categoria_id = categoriaSeleccionada,
            cuenta_id = cuentaSeleccionada,
            descripcion = descripcion
        )
    }

    private fun observarEstado() {
        lifecycleScope.launch {
            verModelo.estado_ui.collect { estado ->
                when (estado) {
                    is EstadoUiTransaccion.Cargando -> {
                        vincular.btnGuardarTransaccion.isEnabled = false
                    }
                    is EstadoUiTransaccion.Exito -> {
                        Toast.makeText(this@actividad_anadirtransaccion, estado.mensaje, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is EstadoUiTransaccion.Error -> {
                        Toast.makeText(this@actividad_anadirtransaccion, estado.mensaje, Toast.LENGTH_SHORT).show()
                        vincular.btnGuardarTransaccion.isEnabled = true
                    }
                    else -> {
                        vincular.btnGuardarTransaccion.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}