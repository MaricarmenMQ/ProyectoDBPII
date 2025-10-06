package com.lvmh.pocketpet.presentacion.pantallas.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lvmh.pocketpet.databinding.ActividadPrincipalBienvenidaBinding
import com.lvmh.pocketpet.utilidades.FormatearMoneda
import com.lvmh.pocketpet.utilidades.administradorpreferenciasUsuario
import kotlinx.coroutines.launch

class actividad_panelprincipal : AppCompatActivity() {

    private lateinit var vincular: ActividadPrincipalBienvenidaBinding
    private lateinit var adminpreferencia: administradorpreferenciasUsuario

    override fun onCreate(estadoGuardado: Bundle?) {
        super.onCreate(estadoGuardado)

        vincular = ActividadPrincipalBienvenidaBinding.inflate(layoutInflater)
        setContentView(vincular.root)

        adminpreferencia = administradorpreferenciasUsuario(this)

        configurarVistas()
        cargarDatosUsuario()
    }

    private fun configurarVistas() {
        // Click en botón agregar transacción
        vincular.btnAgregarTransaccion.setOnClickListener {
            // TODO: Navegar a ActividadAgregarTransaccion
            // val intent = Intent(this, ActividadAgregarTransaccion::class.java)
            // startActivity(intent)
        }

        // Click en card de la mascota
        vincular.cardEstadoMascota.setOnClickListener {
            // TODO: Navegar a ActividadEstadoMascota
            // val intent = Intent(this, ActividadEstadoMascota::class.java)
            // startActivity(intent)
        }
    }

    private fun cargarDatosUsuario() {
        // Cargar nombre de usuario
        lifecycleScope.launch {
            adminpreferencia.obtenerNombreUsuario().collect { nombre ->
                vincular.tvNombreUsuario.text = "Hola, $nombre"
            }
        }

        // Datos de ejemplo (después conectarás con base de datos)
        val balanceTotal = 1500.50
        val ingresosMensuales = 3000.00
        val gastosMensuales = 1500.50

        vincular.tvBalanceTotal.text = FormatearMoneda.formatear(balanceTotal)
        vincular.tvIngresosMensuales.text = FormatearMoneda.formatear(ingresosMensuales)
        vincular.tvGastosMensuales.text = FormatearMoneda.formatear(gastosMensuales)
    }
}