package com.lvmh.pocketpet

<<<<<<< HEAD
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lvmh.pocketpet.ui.theme.PocketPetTheme
=======
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.lvmh.pocketpet.presentacion.pantallas.core.actividad_bienvenida
import com.lvmh.pocketpet.presentacion.pantallas.core.actividad_panelprincipal
import com.lvmh.pocketpet.dominio.utilidades.administradorpreferenciasUsuario
import kotlinx.coroutines.launch
>>>>>>> 659aa94513e4e27a1d883ea9b2add7cdac420627

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        enableEdgeToEdge()
        setContent {
            PocketPetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PocketPetTheme {
        Greeting("Android")
    }
=======

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
>>>>>>> 659aa94513e4e27a1d883ea9b2add7cdac420627
}