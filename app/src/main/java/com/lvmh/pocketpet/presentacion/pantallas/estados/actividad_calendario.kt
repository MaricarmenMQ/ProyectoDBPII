package com.lvmh.pocketpet.presentacion.pantallas.estados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import dagger.hilt.android.AndroidEntryPoint
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import com.lvmh.pocketpet.dominio.utilidades.toLocalDateTime

@AndroidEntryPoint
class actividad_calendario : AppCompatActivity() {

    private lateinit var recicladorTransacciones: RecyclerView
    private lateinit var adaptadorTransacciones: AdaptadorCalendarioTransacciones

    private val idUsuario = "usuario_123" // TODO: Obtener del usuario autenticado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_calendario)

        configurarUI()
    }

    private fun configurarUI() {
        // Configurar la barra de herramientas
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = getString(R.string.titulo_calendario)
            setDisplayHomeAsUpEnabled(true)
        }

        // Configurar RecyclerView de transacciones
        recicladorTransacciones = findViewById(R.id.recicladorTransaccionesCalendario)
        recicladorTransacciones.layoutManager = LinearLayoutManager(this)

        adaptadorTransacciones = AdaptadorCalendarioTransacciones()
        recicladorTransacciones.adapter = adaptadorTransacciones

        // TODO: Cargar transacciones del mes actual
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

/**
 * AdaptadorCalendarioTransacciones - Adaptador para transacciones en calendario
 */
class AdaptadorCalendarioTransacciones : RecyclerView.Adapter<VistaCalendarioTransaccion>() {

    private var transacciones: List<transaccion_entidad> = emptyList()

    fun actualizarLista(nuevasTransacciones: List<transaccion_entidad>) {
        transacciones = nuevasTransacciones
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        padre: ViewGroup,
        tipoVista: Int
    ): VistaCalendarioTransaccion {
        val vista = LayoutInflater.from(padre.context)
            .inflate(R.layout.item_transaccion_calendario, padre, false)
        return VistaCalendarioTransaccion(vista)
    }

    override fun onBindViewHolder(
        contenedor: VistaCalendarioTransaccion,
        posicion: Int
    ) {
        contenedor.enlazar(transacciones[posicion])
    }

    override fun getItemCount(): Int = transacciones.size
}

/**
 * VistaCalendarioTransaccion - ViewHolder para una transacción en el calendario
 */
class VistaCalendarioTransaccion(vistaItem: android.view.View) :
    RecyclerView.ViewHolder(vistaItem) {

    private val textoFecha: TextView? = vistaItem.findViewById(R.id.textoFechaTransaccion)
    private val textoDescripcion: TextView? = vistaItem.findViewById(R.id.textoDescripcionTransaccion)
    private val textoMonto: TextView? = vistaItem.findViewById(R.id.textoMontoTransaccion)

    fun enlazar(transaccion: transaccion_entidad) {
        // Mostrar fecha
        textoFecha?.text = transaccion.fecha.toLocalDateTime().toString()

        // Mostrar descripción
        textoDescripcion?.text = transaccion.descripcion

        // Mostrar monto con signo
        textoMonto?.text = if (transaccion.tipo == "ingreso") {
            "+S/ ${transaccion.monto}"
        } else {
            "-S/ ${transaccion.monto}"
        }

        // Color según tipo de transacción
        val color = if (transaccion.tipo == "ingreso") {
            itemView.context.getColor(android.R.color.holo_green_dark)
        } else {
            itemView.context.getColor(android.R.color.holo_red_dark)
        }

        textoMonto?.setTextColor(color)
    }
}