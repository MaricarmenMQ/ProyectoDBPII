package com.lvmh.pocketpet.data.local.oad

import androidx.room.*
import com.lvmh.pocketpet.data.local.entidades.presupuesto_entidad
import com.lvmh.pocketpet.data.local.entidades.periodo_presupuesto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface presupuestoOad {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(presupuesto: presupuesto_entidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar_todos(presupuestos: List<presupuesto_entidad>)

    @Update
    suspend fun actualizar(presupuesto: presupuesto_entidad)

    @Delete
    suspend fun eliminar(presupuesto: presupuesto_entidad)

    @Query("DELETE FROM presupuestos WHERE id = :presupuesto_id")
    suspend fun eliminar_por_id(presupuesto_id: String)

    @Query("DELETE FROM presupuestos WHERE usuario_id = :usuario_id")
    suspend fun eliminar_por_usuario(usuario_id: String)


    @Query("SELECT * FROM presupuestos WHERE id = :presupuesto_id")
    suspend fun obtener_por_id(presupuesto_id: String): presupuesto_entidad?

    @Query("SELECT * FROM presupuestos WHERE usuario_id = :usuario_id ORDER BY actualizado_en DESC")
    fun obtener_todos_por_usuario(usuario_id: String): Flow<List<presupuesto_entidad>>

    @Query("SELECT * FROM presupuestos WHERE usuario_id = :usuario_id AND esta_activo = 1 ORDER BY monto DESC")
    fun obtener_presupuestos_activos(usuario_id: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuario_id = :usuario_id 
        AND (:categoria_id IS NULL OR categoria_id = :categoria_id)
        AND (:periodo IS NULL OR periodo = :periodo)
        AND (:esta_activo IS NULL OR esta_activo = :esta_activo)
        ORDER BY actualizado_en DESC
    """)
    fun filtrar_presupuestos(
        usuario_id: String,
        categoria_id: String? = null,
        periodo: periodo_presupuesto? = null,
        esta_activo: Boolean? = null
    ): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuario_id = :usuario_id 
        AND cuenta_id = :cuenta_id 
        AND esta_activo = 1
        ORDER BY monto DESC
    """)
    fun obtener_presupuestos_por_cuenta(usuario_id: String, cuenta_id: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuario_id = :usuario_id 
        AND categoria_id = :categoria_id 
        AND esta_activo = 1
        ORDER BY actualizado_en DESC
    """)
    fun obtener_presupuestos_por_categoria(usuario_id: String, categoria_id: String): Flow<List<presupuesto_entidad>>


    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuario_id = :usuario_id 
        AND (nombre LIKE '%' || :consulta || '%' OR notas LIKE '%' || :consulta || '%')
        ORDER BY actualizado_en DESC
    """)
    fun buscar_presupuestos(usuario_id: String, consulta: String): Flow<List<presupuesto_entidad>>

    // ============= ALERTAS Y NOTIFICACIONES =============

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuario_id = :usuario_id 
        AND esta_activo = 1 
        AND (gastado / CAST(monto AS FLOAT)) * 100 >= porcentaje_alerta
        ORDER BY (gastado / CAST(monto AS FLOAT)) DESC
    """)
    fun obtener_presupuestos_con_alertas(usuario_id: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuario_id = :usuario_id 
        AND esta_activo = 1 
        AND gastado > monto
        ORDER BY (gastado - monto) DESC
    """)
    fun obtener_presupuestos_excedidos(usuario_id: String): Flow<List<presupuesto_entidad>>


    @Query("""
        SELECT 
            COUNT(*) as total,
            SUM(monto) as monto_total,
            SUM(gastado) as gastado_total
        FROM presupuestos 
        WHERE usuario_id = :usuario_id AND esta_activo = 1
    """)
    suspend fun obtener_estadisticas_presupuesto(usuario_id: String): datos_estadisticas_presupuesto?

    @Query("""
        SELECT AVG(gastado / CAST(monto AS FLOAT) * 100) as porcentaje_promedio
        FROM presupuestos 
        WHERE usuario_id = :usuario_id AND monto > 0 AND esta_activo = 1
    """)
    suspend fun obtener_porcentaje_promedio_usado(usuario_id: String): Double?

    @Query("""
        SELECT COUNT(*) FROM presupuestos 
        WHERE usuario_id = :usuario_id AND gastado > monto
    """)
    suspend fun obtener_cantidad_presupuestos_excedidos(usuario_id: String): Int

    @Query("""
        UPDATE presupuestos 
        SET gastado = :nuevo_gastado, 
            actualizado_en = :actualizado_en 
        WHERE id = :presupuesto_id
    """)
    suspend fun actualizar_gastado(presupuesto_id: String, nuevo_gastado: Double, actualizado_en: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE presupuestos 
        SET monto = :nuevo_monto, 
            actualizado_en = :actualizado_en 
        WHERE id = :presupuesto_id
    """)
    suspend fun actualizar_monto(presupuesto_id: String, nuevo_monto: Double, actualizado_en: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE presupuestos 
        SET esta_activo = :esta_activo, 
            actualizado_en = :actualizado_en 
        WHERE id = :presupuesto_id
    """)
    suspend fun actualizar_activo(presupuesto_id: String, esta_activo: Boolean, actualizado_en: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE presupuestos 
        SET porcentaje_alerta = :porcentaje, 
            actualizado_en = :actualizado_en 
        WHERE id = :presupuesto_id
    """)
    suspend fun actualizar_porcentaje_alerta(presupuesto_id: String, porcentaje: Int, actualizado_en: LocalDateTime = LocalDateTime.now())


    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuario_id = :usuario_id 
        AND periodo = :periodo 
        AND esta_activo = 1
        ORDER BY fecha_inicio DESC
    """)
    fun obtener_presupuestos_por_periodo(usuario_id: String, periodo: periodo_presupuesto): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuario_id = :usuario_id 
        AND fecha_inicio >= :fecha_inicio 
        AND fecha_fin <= :fecha_fin
        ORDER BY fecha_inicio DESC
    """)
    fun obtener_presupuestos_en_rango_fechas(usuario_id: String, fecha_inicio: LocalDateTime, fecha_fin: LocalDateTime): Flow<List<presupuesto_entidad>>


    @Query("SELECT COUNT(*) FROM presupuestos WHERE usuario_id = :usuario_id AND categoria_id = :categoria_id AND periodo = :periodo")
    suspend fun existe_presupuesto_para_categoria_y_periodo(usuario_id: String, categoria_id: String, periodo: periodo_presupuesto): Int

    @Query("SELECT COUNT(*) FROM presupuestos WHERE usuario_id = :usuario_id")
    suspend fun contar_presupuestos(usuario_id: String): Int

}
data class datos_estadisticas_presupuesto(
    val total: Int = 0,
    val monto_total: Double = 0.0,
    val gastado_total: Double = 0.0
) {
    val monto_restante: Double
        get() = (monto_total - gastado_total).coerceAtLeast(0.0)

    val porcentaje_usado: Double
        get() = if (monto_total > 0) (gastado_total / monto_total * 100).coerceIn(0.0, 100.0) else 0.0
}
