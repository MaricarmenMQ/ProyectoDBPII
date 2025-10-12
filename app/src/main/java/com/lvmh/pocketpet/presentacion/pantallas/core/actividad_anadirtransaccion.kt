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

    private lateinit var binding: ActividadAnadirTransaccionBinding
    private val viewModel: TransactionViewModel by viewModels()

    private var tipoSeleccionado = "gasto"
    private var categoriaSeleccionada: Long = 0
    private var cuentaSeleccionada: Long = 1 // Por defecto cuenta 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActividadAnadirTransaccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarToolbar()
        configurarTabs()
        configurarBotones()
        observarEstado()
    }

    private fun configurarToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.agregar_transaccion)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun configurarTabs() {
        binding.tabLayoutTipo.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
        binding.btnGuardarTransaccion.setOnClickListener {
            guardarTransaccion()
        }
    }

    private fun guardarTransaccion() {
        val monto = binding.etMonto.text.toString().toDoubleOrNull()
        val descripcion = binding.etDescripcion.text.toString()

        if (monto == null || monto <= 0) {
            binding.etMonto.error = "Ingresa un monto válido"
            return
        }

        if (descripcion.isBlank()) {
            binding.etDescripcion.error = "Ingresa una descripción"
            return
        }

        viewModel.crear_transaccion(
            tipo = tipoSeleccionado,
            monto = monto,
            categoria_id = categoriaSeleccionada,
            cuenta_id = cuentaSeleccionada,
            descripcion = descripcion
        )
    }

    private fun observarEstado() {
        lifecycleScope.launch {
            viewModel.estado_ui.collect { estado ->
                when (estado) {
                    is EstadoUiTransaccion.Cargando -> {
                        binding.btnGuardarTransaccion.isEnabled = false
                    }
                    is EstadoUiTransaccion.Exito -> {
                        Toast.makeText(this@actividad_anadirtransaccion, estado.mensaje, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is EstadoUiTransaccion.Error -> {
                        Toast.makeText(this@actividad_anadirtransaccion, estado.mensaje, Toast.LENGTH_SHORT).show()
                        binding.btnGuardarTransaccion.isEnabled = true
                    }
                    else -> {
                        binding.btnGuardarTransaccion.isEnabled = true
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