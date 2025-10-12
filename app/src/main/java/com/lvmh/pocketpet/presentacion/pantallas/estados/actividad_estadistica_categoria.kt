package com.lvmh.pocketpet.presentacion.pantallas.estados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import com.lvmh.pocketpet.R
import com.lvmh.pocketpet.data.local.entidades

@AndroidEntryPoint
class CategoryStatsActivity : AppCompatActivity() {

    private lateinit var categoryStatsRecyclerView: RecyclerView
    private lateinit var categoryStatsAdapter: CategoryStatsAdapter

    private val userId = "user_123" // TODO: Obtener del usuario autenticado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_estadistica_categoria)

        setupUI()
    }

    private fun setupUI() {
        // Configurar toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Estadísticas por Categoría"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // RecyclerView para categorías
        categoryStatsRecyclerView = findViewById(R.id.categoryStatsRecyclerView)
        categoryStatsRecyclerView.layoutManager = LinearLayoutManager(this)

        categoryStatsAdapter = CategoryStatsAdapter()
        categoryStatsRecyclerView.adapter = categoryStatsAdapter

        // TODO: Cargar datos de categorías y estadísticas
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

class CategoryStatsAdapter : RecyclerView.Adapter<CategoryStatsViewHolder>() {

    private var categories: List<categoria_entidad> = emptyList()

    fun submitList(newCategories: List<categoria_entidad>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): CategoryStatsViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_stats, parent, false)
        return CategoryStatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryStatsViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}

/**
 * CategoryStatsViewHolder - ViewHolder para estadísticas de categoría
 */
class CategoryStatsViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: CategoryEntity) {
        // Nombre de categoría
        itemView.findViewById<android.widget.TextView>(R.id.categoryNameText)?.text = category.name

        // TODO: Cargar y mostrar:
        // - Total gastado en categoría
        // - Presupuesto asignado
        // - Porcentaje del total
        // - ProgressBar visual
    }
}