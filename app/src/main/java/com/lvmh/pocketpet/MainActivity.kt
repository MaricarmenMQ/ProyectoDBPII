package com.lvmh.pocketpet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lvmh.pocketpet.presentacion.pantallas.estados.actividad_meta

/**
 * MainActivity temporal - Solo para pruebas de la pantalla de metas
 * TODO: Implementar navegación completa con onboarding y panel principal
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Navegar directamente a la pantalla de metas
        val intent = Intent(this, actividad_meta::class.java)
        startActivity(intent)
        finish()
    }
}