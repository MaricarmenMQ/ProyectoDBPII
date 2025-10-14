package com.lvmh.pocketpet.data.local.oad

import androidx.room.*
import com.lvmh.pocketpet.data.local.entidades.categoria_entidad
import kotlinx.coroutines.flow.Flow
@Dao
interface categoriaOad {@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertar(categoria: categoria_entidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar_todas(categorias: List<categoria_entidad>)

    @Update
    suspend fun actualizar(categoria: categoria_entidad)

    @Delete
    suspend fun eliminar(categoria: categoria_entidad)

    @Query("DELETE FROM categorias WHERE id = :categoria_id")
    suspend fun eliminar_por_id(categoria_id: String)

    @Query("DELETE FROM categorias WHERE usuario_id = :usuario_id AND es_predeterminada = 0")
    suspend fun eliminar_personalizadas_usuario(usuario_id: String)

    @Query("SELECT * FROM categorias WHERE id = :categoria_id")
    suspend fun obtener_por_id(categoria_id: String): categoria_entidad?

    @Query("SELECT * FROM categorias WHERE usuario_id = :usuario_id OR es_predeterminada = 1 ORDER BY orden ASC, nombre ASC")
    fun obtener_todas_por_usuario(usuario_id: String): Flow<List<categoria_entidad>>

    @Query("SELECT * FROM categorias WHERE (usuario_id = :usuario_id OR es_predeterminada = 1) AND esta_activa = 1 ORDER BY orden ASC")
    fun obtener_activas_por_usuario(usuario_id: String): Flow<List<categoria_entidad>>

    @Query("SELECT * FROM categorias WHERE (usuario_id = :usuario_id OR es_predeterminada = 1) AND tipo = :tipo AND esta_activa = 1 ORDER BY orden ASC")
    fun obtener_por_tipo(usuario_id: String, tipo: String): Flow<List<categoria_entidad>>

    @Query("SELECT * FROM categorias WHERE (usuario_id = :usuario_id OR es_predeterminada = 1) AND tipo = 'gasto' AND esta_activa = 1 ORDER BY orden ASC")
    fun obtener_categorias_gasto(usuario_id: String): Flow<List<categoria_entidad>>

    @Query("SELECT * FROM categorias WHERE (usuario_id = :usuario_id OR es_predeterminada = 1) AND tipo = 'ingreso' AND esta_activa = 1 ORDER BY orden ASC")
    fun obtener_categorias_ingreso(usuario_id: String): Flow<List<categoria_entidad>>

    @Query("SELECT * FROM categorias WHERE usuario_id = :usuario_id AND es_predeterminada = 0 ORDER BY nombre ASC")
    fun obtener_categorias_personalizadas(usuario_id: String): Flow<List<categoria_entidad>>

    @Query("SELECT * FROM categorias WHERE es_predeterminada = 1 ORDER BY tipo, orden ASC")
    fun obtener_categorias_predeterminadas(): Flow<List<categoria_entidad>>

    @Query("""
        SELECT * FROM categorias 
        WHERE (usuario_id = :usuario_id OR es_predeterminada = 1) 
        AND (nombre LIKE '%' || :consulta || '%' OR descripcion LIKE '%' || :consulta || '%')
        AND esta_activa = 1
        ORDER BY nombre ASC
    """)
    fun buscar_categorias(usuario_id: String, consulta: String): Flow<List<categoria_entidad>>

    @Query("""
        UPDATE categorias 
        SET esta_activa = :esta_activa,
            actualizado_en = :actualizado_en
        WHERE id = :categoria_id
    """)
    suspend fun actualizar_estado(categoria_id: String, esta_activa: Boolean, actualizado_en: Long = System.currentTimeMillis())

    @Query("""
        UPDATE categorias 
        SET color = :color,
            actualizado_en = :actualizado_en
        WHERE id = :categoria_id
    """)
    suspend fun actualizar_color(categoria_id: String, color: String, actualizado_en: Long = System.currentTimeMillis())

    @Query("""
        UPDATE categorias 
        SET icono = :icono,
            actualizado_en = :actualizado_en
        WHERE id = :categoria_id
    """)
    suspend fun actualizar_icono(categoria_id: String, icono: String, actualizado_en: Long = System.currentTimeMillis())

    @Query("""
        UPDATE categorias 
        SET orden = :orden,
            actualizado_en = :actualizado_en
        WHERE id = :categoria_id
    """)
    suspend fun actualizar_orden(categoria_id: String, orden: Int, actualizado_en: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM categorias WHERE usuario_id = :usuario_id")
    suspend fun contar_categorias_usuario(usuario_id: String): Int

    @Query("SELECT COUNT(*) FROM categorias WHERE usuario_id = :usuario_id AND tipo = :tipo")
    suspend fun contar_por_tipo(usuario_id: String, tipo: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM categorias WHERE usuario_id = :usuario_id AND nombre = :nombre AND tipo = :tipo)")
    suspend fun existe_categoria_con_nombre(usuario_id: String, nombre: String, tipo: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM categorias WHERE id = :categoria_id)")
    suspend fun existe_categoria(categoria_id: String): Boolean

    @Query("SELECT MAX(orden) FROM categorias WHERE usuario_id = :usuario_id AND tipo = :tipo")
    suspend fun obtener_maximo_orden(usuario_id: String, tipo: String): Int?

    @Query("DELETE FROM categorias WHERE usuario_id = :usuario_id")
    suspend fun eliminar_todas_usuario(usuario_id: String)

}