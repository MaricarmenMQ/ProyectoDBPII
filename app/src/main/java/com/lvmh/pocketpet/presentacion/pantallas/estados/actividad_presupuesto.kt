package com.lvmh.pocketpet.presentacion.pantallas.estados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.dominio.modelos.presupuesto
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton

@AndroidEntryPoint
class actividad_presupuesto : AppCompatActivity() {

    private lateinit var recicladorPresupuestos: RecyclerView
    private lateinit var adaptadorPresupuestos: AdaptadorPresupuestos
    private lateinit var vistaVaciaPresupuestos: LinearLayout
    private lateinit var barraProgresoCargando: ProgressBar
    private lateinit var botonFlotanteCrear: FloatingActionButton
    private val idUsuario = "usuario_123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_presupuesto)

        inicializarVistas()
        configurarUI()
        configurarListeners()
    }

    private fun inicializarVistas() {
        recicladorPresupuestos = findViewById(R.id.listaPresupuestos)
        vistaVaciaPresupuestos = findViewById(R.id.vistaVacia)
        barraProgresoCargando = findViewById(R.id.barraProgresoCargando)
        botonFlotanteCrear = findViewById(R.id.botonFlotanteCrearPresupuesto)
    }

    private fun configurarUI() {
        setSupportActionBar(findViewById(R.id.barraHerramientas))
        supportActionBar?.apply {
            title = getString(R.string.titulo_presupuestos)
            setDisplayHomeAsUpEnabled(true)
        }

        recicladorPresupuestos.layoutManager = LinearLayoutManager(this)
        adaptadorPresupuestos = AdaptadorPresupuestos()
        recicladorPresupuestos.adapter = adaptadorPresupuestos

        cargarPresupuestos()
    }

    private fun configurarListeners() {
        botonFlotanteCrear.setOnClickListener {
            // TODO: Navegar a la pantalla de crear presupuesto
            // val intent = Intent(this, ActividadCrearPresupuesto::class.java)
            // startActivity(intent)
        }
    }

    private fun cargarPresupuestos() {
        lifecycleScope.launch {
            mostrarCargando(true)

            try {
                // TODO: Cargar presupuestos del usuario desde el repositorio
                // val presupuestos = repositorioPresupuestos.obtenerPresupuestosDeUsuario(idUsuario)
                val presupuestos = emptyList<presupuesto>()

                mostrarCargando(false)

                if (presupuestos.isEmpty()) {
                    vistaVaciaPresupuestos.visibility = android.view.View.VISIBLE
                    recicladorPresupuestos.visibility = android.view.View.GONE
                } else {
                    vistaVaciaPresupuestos.visibility = android.view.View.GONE
                    recicladorPresupuestos.visibility = android.view.View.VISIBLE
                    adaptadorPresupuestos.actualizarLista(presupuestos)
                }
            } catch (e: Exception) {
                mostrarCargando(false)
                // TODO: Manejar error (mostrar mensaje al usuario)
            }
        }
    }

    private fun mostrarCargando(mostrar: Boolean) {
        barraProgresoCargando.visibility = if (mostrar) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

class AdaptadorPresupuestos : RecyclerView.Adapter<VistaPresupuesto>() {

    private var presupuestos: List<presupuesto> = emptyList()

    fun actualizarLista(nuevosPresupuestos: List<presupuesto>) {
        presupuestos = nuevosPresupuestos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(padre: ViewGroup, tipoVista: Int): VistaPresupuesto {
        val vista = LayoutInflater.from(padre.context)
            .inflate(R.layout.item_presupuesto, padre, false)
        return VistaPresupuesto(vista)
    }

    override fun onBindViewHolder(contenedor: VistaPresupuesto, posicion: Int) {
        contenedor.enlazar(presupuestos[posicion])
    }

    override fun getItemCount(): Int = presupuestos.size
}

class VistaPresupuesto(vistaItem: android.view.View) : ViewHolder(vistaItem) {

    private val textoNombre: TextView? = vistaItem.findViewById(R.id.nombrePresupuesto)
    private val textoMonto: TextView? = vistaItem.findViewById(R.id.montoPresupuesto)
    private val textoGastado: TextView? = vistaItem.findViewById(R.id.textoTotalGastado)
    private val textoRestante: TextView? = vistaItem.findViewById(R.id.montoPresupuesto)
    private val barraProgreso: ProgressBar? = vistaItem.findViewById(R.id.barraProgresoPresupuesto)
    private val textoEstado: TextView? = vistaItem.findViewById(R.id.indicadorPresupuesto)

    fun enlazar(presupuesto: presupuesto) {
        textoNombre?.text = presupuesto.nombre

        textoMonto?.text = itemView.context.getString(
            R.string.moneda_soles,
            String.format("%.2f", presupuesto.monto)
        )

        textoGastado?.text = itemView.context.getString(
            R.string.moneda_soles,
            String.format("%.2f", presupuesto.gastado)
        )

        textoRestante?.text = itemView.context.getString(
            R.string.moneda_soles,
            String.format("%.2f", presupuesto.montoRestante)
        )

        val porcentaje = presupuesto.porcentajeUsado.toInt()
        barraProgreso?.apply {
            progress = porcentaje
            max = 100
        }

        // Determinar estado
        val estadoId = when {
            presupuesto.estaExcedido -> R.string.excedido
            presupuesto.debeAlertar -> R.string.cerca_limite
            else -> R.string.en_control
        }

        textoEstado?.text = itemView.context.getString(estadoId)
        textoEstado?.setTextColor(
            when {
                presupuesto.estaExcedido -> itemView.context.getColor(android.R.color.holo_red_dark)
                presupuesto.debeAlertar -> itemView.context.getColor(android.R.color.holo_orange_dark)
                else -> itemView.context.getColor(android.R.color.holo_green_dark)
            }
        )
    }
}