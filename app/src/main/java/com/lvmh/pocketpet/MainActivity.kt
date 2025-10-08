package com.lvmh.pocketpet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.lvmh.pocketpet.presentacion.pantallas.core.actividad_bienvenida
import com.lvmh.pocketpet.presentacion.pantallas.core.actividad_panelprincipal
import com.lvmh.pocketpet.dominio.utilidades.administradorpreferenciasUsuario
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adminPrefs = administradorpreferenciasUsuario(this)

        // Verificar si completó el onboarding
        lifecycleScope.launch {
            adminPrefs.bienvenidaCompletada.collect { completado ->
                if (completado) {
                    // Ya completó bienvenida → ir al panel principal
                    startActivity(Intent(this@MainActivity, actividad_panelprincipal::class.java))
                } else {
                    // Primera vez → mostrar bienvenida
                    startActivity(Intent(this@MainActivity, actividad_bienvenida::class.java))
                }
                finish()
            }
        }
    }
}