package com.lvmh.pocketpet.presentacion.pantallas.estados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.dominio.modelos.meta
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.ViewHolder

@AndroidEntryPoint
class actividad_meta: AppCompatActivity() {

    private lateinit var recicladorMetas: RecyclerView
    private lateinit var adaptadorMetas: AdaptadorMetas
    private lateinit var vistaVaciaMetas: LinearLayout
    private val idUsuario = "usuario_123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_meta)

        inicializarVistas()
        configurarUI()
    }

    private fun inicializarVistas() {
        recicladorMetas = findViewById(R.id.recicladorMetas)
        vistaVaciaMetas = findViewById(R.id.vistaVaciaMetas)
    }

    private fun configurarUI() {
        setSupportActionBar(findViewById(R.id.barraHerramientas))
        supportActionBar?.apply {
            title = getString(R.string.titulo_metas)
            setDisplayHomeAsUpEnabled(true)
        }

        recicladorMetas.layoutManager = LinearLayoutManager(this)
        adaptadorMetas = AdaptadorMetas()
        recicladorMetas.adapter = adaptadorMetas

        cargarMetas()
    }

    private fun cargarMetas() {
        lifecycleScope.launch {
            // TODO: Cargar metas del usuario desde el repositorio
            val metasVacias = emptyList<meta>()
            if (metasVacias.isEmpty()) {
                vistaVaciaMetas.visibility = android.view.View.VISIBLE
                recicladorMetas.visibility = android.view.View.GONE
            } else {
                vistaVaciaMetas.visibility = android.view.View.GONE
                recicladorMetas.visibility = android.view.View.VISIBLE
                adaptadorMetas.actualizarLista(metasVacias)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

class AdaptadorMetas : RecyclerView.Adapter<VistaMeta>() {

    private var metas: List<meta> = emptyList()

    fun actualizarLista(nuevasMetas: List<meta>) {
        metas = nuevasMetas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(padre: ViewGroup, tipoVista: Int): VistaMeta {
        val vista = LayoutInflater.from(padre.context)
            .inflate(R.layout.item_meta, padre, false)
        return VistaMeta(vista)
    }

    override fun onBindViewHolder(contenedor: VistaMeta, posicion: Int) {
        contenedor.enlazar(metas[posicion])
    }

    override fun getItemCount(): Int = metas.size
}

class VistaMeta(vistaItem: android.view.View) : ViewHolder(vistaItem) {

    private val nombreMeta: TextView? = vistaItem.findViewById(R.id.nombreMeta)
    private val textoDescripcionMeta: TextView? = vistaItem.findViewById(R.id.indicadorPrioridadMeta)
    private val montoMeta: TextView? = vistaItem.findViewById(R.id.montoMeta)
    private val progresoMeta: TextView? = vistaItem.findViewById(R.id.progresoMeta)
    private val barraProgresoMetas: ProgressBar? = vistaItem.findViewById(R.id.barraProgresoMetas)
    private val estadoMeta: TextView? = vistaItem.findViewById(R.id.estadoMeta)

    fun enlazar(meta: meta) {
        nombreMeta?.text = meta.nombre
        textoDescripcionMeta?.text = meta.descripcion

        montoMeta?.text = itemView.context.getString(
            R.string.moneda_soles,
            String.format("%.2f", meta.montoObjetivo)
        )

        val porcentaje = meta.porcentajeCompletado.toInt()
        progresoMeta?.text = itemView.context.getString(
            R.string.porcentaje,
            porcentaje.toString()
        )

        barraProgresoMetas?.apply {
            progress = porcentaje
            max = 100
        }

        estadoMeta?.text = meta.estado.name
        estadoMeta?.setTextColor(
            when (meta.estado.name) {
                "COMPLETADA" -> itemView.context.getColor(android.R.color.holo_green_dark)
                "FALLIDA" -> itemView.context.getColor(android.R.color.holo_red_dark)
                "PAUSADA" -> itemView.context.getColor(android.R.color.darker_gray)
                else -> itemView.context.getColor(android.R.color.holo_blue_bright)
            }
        )
    }
}