package com.lvmh.pocketpet.data.local.repositorio

import com.lvmh.pocketpet.data.local.entidades.categoria_entidad
import com.lvmh.pocketpet.data.local.oad.categoriaOad
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class categoriaRepositorio @Inject constructor(
    private val categoriaOad: categoriaOad
) {

    suspend fun crear_categoria(categoria: categoria_entidad) {
        categoriaOad.insertar(categoria)
    }

    suspend fun crear_categorias_iniciales(usuario_id: String, categorias: List<categoria_entidad>) {
        categoriaOad.insertar_todas(categorias)
    }

    suspend fun obtener_categoria_por_id(categoria_id: String): categoria_entidad? {
        return categoriaOad.obtener_por_id(categoria_id)
    }

    fun obtener_todas_categorias(usuario_id: String): Flow<List<categoria_entidad>> {
        return categoriaOad.obtener_todas_por_usuario(usuario_id)
    }

    fun obtener_categorias_activas(usuario_id: String): Flow<List<categoria_entidad>> {
        return categoriaOad.obtener_activas_por_usuario(usuario_id)
    }

    fun obtener_categorias_gasto(usuario_id: String): Flow<List<categoria_entidad>> {
        return categoriaOad.obtener_categorias_gasto(usuario_id)
    }

    fun obtener_categorias_ingreso(usuario_id: String): Flow<List<categoria_entidad>> {
        return categoriaOad.obtener_categorias_ingreso(usuario_id)
    }

    fun obtener_por_tipo(usuario_id: String, tipo: String): Flow<List<categoria_entidad>> {
        return categoriaOad.obtener_por_tipo(usuario_id, tipo)
    }

    fun obtener_categorias_personalizadas(usuario_id: String): Flow<List<categoria_entidad>> {
        return categoriaOad.obtener_categorias_personalizadas(usuario_id)
    }

    fun obtener_categorias_predeterminadas(): Flow<List<categoria_entidad>> {
        return categoriaOad.obtener_categorias_predeterminadas()
    }

    fun buscar_categorias(usuario_id: String, consulta: String): Flow<List<categoria_entidad>> {
        return categoriaOad.buscar_categorias(usuario_id, consulta)
    }

    suspend fun actualizar_categoria(categoria: categoria_entidad) {
        categoriaOad.actualizar(categoria.copy(actualizado_en = System.currentTimeMillis()))
    }

    suspend fun eliminar_categoria(categoria: categoria_entidad) {
        if (!categoria.es_predeterminada) {
            categoriaOad.eliminar(categoria)
        }
    }

    suspend fun eliminar_categoria_por_id(categoria_id: String) {
        val categoria = categoriaOad.obtener_por_id(categoria_id)
        if (categoria != null && !categoria.es_predeterminada) {
            categoriaOad.eliminar_por_id(categoria_id)
        }
    }

    suspend fun actualizar_estado(categoria_id: String, esta_activa: Boolean) {
        categoriaOad.actualizar_estado(categoria_id, esta_activa)
    }

    suspend fun actualizar_color(categoria_id: String, color: String) {
        categoriaOad.actualizar_color(categoria_id, color)
    }

    suspend fun actualizar_icono(categoria_id: String, icono: String) {
        categoriaOad.actualizar_icono(categoria_id, icono)
    }

    suspend fun actualizar_orden(categoria_id: String, orden: Int) {
        categoriaOad.actualizar_orden(categoria_id, orden)
    }

    suspend fun contar_categorias_usuario(usuario_id: String): Int {
        return categoriaOad.contar_categorias_usuario(usuario_id)
    }

    suspend fun contar_por_tipo(usuario_id: String, tipo: String): Int {
        return categoriaOad.contar_por_tipo(usuario_id, tipo)
    }

    suspend fun existe_categoria_con_nombre(usuario_id: String, nombre: String, tipo: String): Boolean {
        return categoriaOad.existe_categoria_con_nombre(usuario_id, nombre, tipo)
    }

    suspend fun existe_categoria(categoria_id: String): Boolean {
        return categoriaOad.existe_categoria(categoria_id)
    }

    suspend fun obtener_siguiente_orden(usuario_id: String, tipo: String): Int {
        val maximo_orden = categoriaOad.obtener_maximo_orden(usuario_id, tipo)
        return (maximo_orden ?: 0) + 1
    }

    suspend fun eliminar_todas_usuario(usuario_id: String) {
        categoriaOad.eliminar_todas_usuario(usuario_id)
    }

    suspend fun eliminar_personalizadas_usuario(usuario_id: String) {
        categoriaOad.eliminar_personalizadas_usuario(usuario_id)
    }
}