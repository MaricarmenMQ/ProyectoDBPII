package com.lvmh.pocketpet.presentacion.pantallas.core

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputEditText
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.databinding.ActividadBienvenidaBinding
import com.lvmh.pocketpet.presentacion.adaptadores.adaptadorBienvenida
import com.lvmh.pocketpet.utilidades.administradorpreferenciasUsuario
import kotlinx.coroutines.launch

class actividad_bienvenida : AppCompatActivity() {

    private lateinit var vincular: ActividadBienvenidaBinding
    private lateinit var adminpreferencia: administradorpreferenciasUsuario
    private lateinit var adaptador: adaptadorBienvenida
    private val indicadores = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vincular = ActividadBienvenidaBinding.inflate(layoutInflater)
        setContentView(vincular.root)

        adminpreferencia = administradorpreferenciasUsuario(this)

        configurarPaginador()
        configurarBoton()
    }

    private fun configurarPaginador() {
        // Configurar adaptador
        adaptador = adaptadorBienvenida(this)
        vincular.paginadorVistas.adapter = adaptador

        // Configurar indicadores (los puntitos)
        configurarIndicadores(3)
        establecerIndicadorActual(0)

        // Listener para detectar cambio de página
        vincular.paginadorVistas.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(posicion: Int) {
                super.onPageSelected(posicion)
                establecerIndicadorActual(posicion)

                // Cambiar texto del botón en última página
                if (posicion == 2) {
                    vincular.btnSiguiente.text = "Comenzar"
                } else {
                    vincular.btnSiguiente.text = "Siguiente"
                }
            }
        })
    }

    private fun configurarBoton() {
        vincular.btnSiguiente.setOnClickListener {
            val paginaActual = vincular.paginadorVistas.currentItem

            if (paginaActual < 2) {
                // Ir a la siguiente diapositiva
                vincular.paginadorVistas.currentItem = paginaActual + 1
            } else {
                // Última diapositiva - guardar configuración
                completarBienvenida()
            }
        }
    }

    private fun configurarIndicadores(cantidad: Int) {
        val parametros = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        parametros.setMargins(8, 0, 8, 0)

        for (i in 0 until cantidad) {
            val indicador = ImageView(this)
            indicador.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.indicador_inactivo)
            )
            indicador.layoutParams = parametros
            vincular.layoutIndicadores.addView(indicador)
            indicadores.add(indicador)
        }
    }

    private fun establecerIndicadorActual(posicion: Int) {
        indicadores.forEachIndexed { indice, indicador ->
            if (indice == posicion) {
                indicador.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.indicador_activo)
                )
            } else {
                indicador.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.indicador_inactivo)
                )
            }
        }
    }

    private fun completarBienvenida() {
        // Obtener el fragmento actual (el tercero con el input)
        val fragmentoTercero = supportFragmentManager.findFragmentByTag("f2") as? fragmentoBienvenida3
        val inputNombre = fragmentoTercero?.view?.findViewById<TextInputEditText>(R.id.etNombreUsuario)

        val nombre = inputNombre?.text.toString().trim()

        if (nombre.isEmpty()) {
            inputNombre?.error = "Ingresa tu nombre"
            return
        }

        // Guardar configuración
        lifecycleScope.launch {
            adminpreferencia.guardarConfiguracionUsuario(
                nombre = nombre,
                moneda = "@simbolosPormoneda",
                diaInicio = 1,
                notificaciones = true
            )

            // Ir al panel principal
            startActivity(Intent(this@actividad_bienvenida, actividad_panelprincipal::class.java))
            finish()
        }
    }
}