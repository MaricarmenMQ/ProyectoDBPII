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
import com.lvmh.pocketpet.dominio.utilidades.administradorpreferenciasUsuario
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

    // Configura el ViewPager y los indicadores
    private fun configurarPaginador() {
        adaptador = adaptadorBienvenida(this)
        vincular.paginadorVistas.adapter = adaptador

        configurarIndicadores(3)
        establecerIndicadorActual(0)

        vincular.paginadorVistas.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(posicion: Int) {
                super.onPageSelected(posicion)
                establecerIndicadorActual(posicion)
                vincular.btnSiguiente.text = if (posicion == 2) "Comenzar" else "Siguiente"
            }
        })
    }

    // Configura el botón "Siguiente" o "Comenzar"
    private fun configurarBoton() {
        vincular.btnSiguiente.setOnClickListener {
            val pagina = vincular.paginadorVistas.currentItem
            if (pagina < 2) {
                vincular.paginadorVistas.currentItem = pagina + 1
            } else {
                completarBienvenida()
            }
        }
    }

    // Crea los puntos indicadores
    private fun configurarIndicadores(cantidad: Int) {
        val parametros = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(8, 0, 8, 0) }

        repeat(cantidad) {
            val punto = ImageView(this)
            punto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicador_inactivo))
            punto.layoutParams = parametros
            vincular.layoutIndicadores.addView(punto)
            indicadores.add(punto)
        }
    }

    // Cambia el estado del punto activo
    private fun establecerIndicadorActual(posicion: Int) {
        indicadores.forEachIndexed { i, punto ->
            val drawable = if (i == posicion)
                R.drawable.indicador_activo else R.drawable.indicador_inactivo
            punto.setImageDrawable(ContextCompat.getDrawable(this, drawable))
        }
    }

    // Guarda los datos y pasa al panel principal
    private fun completarBienvenida() {
        val fragmento = supportFragmentManager.findFragmentByTag("f2") as? fragmentoBienvenida3
        val campoNombre = fragmento?.view?.findViewById<TextInputEditText>(R.id.etNombreUsuario)
        val nombre = campoNombre?.text.toString().trim()

        if (nombre.isEmpty()) {
            campoNombre?.error = "Ingresa tu nombre"
            return
        }

        lifecycleScope.launch {
            adminpreferencia.guardarConfiguracionUsuario(
                nombre = nombre,
                moneda = "@simbolosPormoneda",
                diaInicio = 1,
                notificaciones = true
            )
            startActivity(Intent(this@actividad_bienvenida, actividad_panelprincipal::class.java))
            finish()
        }
    }
}
