package com.lvmh.pocketpet.data.local.oad

import androidx.room.*
import com.lvmh.pocketpet.data.local.entidades.PrioridadMeta
import com.lvmh.pocketpet.data.local.entidades.EstadoMeta
import com.lvmh.pocketpet.data.local.entidades.CategoriaMeta
import com.lvmh.pocketpet.data.local.entidades.meta_entidad
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface metaOad {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(meta: meta_entidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(metas: List<meta_entidad>)

    @Update
    suspend fun actualizar(meta: meta_entidad)

    @Delete
    suspend fun eliminar(meta: meta_entidad)

    @Query("DELETE FROM metas WHERE id = :metaId")
    suspend fun eliminarPorId(metaId: String)

    @Query("DELETE FROM metas WHERE usuarioId = :usuarioId")
    suspend fun eliminarPorUsuario(usuarioId: String)

    @Query("SELECT * FROM metas WHERE id = :metaId")
    suspend fun obtenerPorId(metaId: String): meta_entidad?

    @Query("SELECT * FROM metas WHERE usuarioId = :usuarioId ORDER BY actualizadoEn DESC")
    fun obtenerTodasPorUsuario(usuarioId: String): Flow<List<meta_entidad>>

    @Query("SELECT * FROM metas WHERE usuarioId = :usuarioId AND estado = 'EN_PROGRESO' ORDER BY fechaLimite ASC")
    fun obtenerMetasActivas(usuarioId: String): Flow<List<meta_entidad>>

    @Query("SELECT * FROM metas WHERE usuarioId = :usuarioId AND estado = 'COMPLETADA' ORDER BY completadoEn DESC")
    fun obtenerMetasCompletadas(usuarioId: String): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND (:estado IS NULL OR estado = :estado)
        AND (:prioridad IS NULL OR prioridad = :prioridad)
        AND (:categoria IS NULL OR categoria = :categoria)
        ORDER BY 
            CASE WHEN :ordenarPorFechaLimite = 1 THEN fechaLimite ELSE actualizadoEn END ASC
    """)
    fun filtrarMetas(
        usuarioId: String,
        estado: EstadoMeta? = null,
        prioridad: PrioridadMeta? = null,
        categoria: CategoriaMeta? = null,
        ordenarPorFechaLimite: Int = 0
    ): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND categoriaId = :categoriaId 
        ORDER BY prioridad DESC, fechaLimite ASC
    """)
    fun obtenerMetasPorCategoria(usuarioId: String, categoriaId: String): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND cuentaId = :cuentaId 
        AND estado != 'COMPLETADA'
        ORDER BY fechaLimite ASC
    """)
    fun obtenerMetasPorCuenta(usuarioId: String, cuentaId: String): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND (nombre LIKE '%' || :consulta || '%' 
        OR descripcion LIKE '%' || :consulta || '%'
        OR notas LIKE '%' || :consulta || '%')
        ORDER BY actualizadoEn DESC
    """)
    fun buscarMetas(usuarioId: String, consulta: String): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND fechaLimite IS NOT NULL 
        AND fechaLimite < datetime('now')
        AND estado != 'COMPLETADA'
        ORDER BY fechaLimite ASC
    """)
    fun obtenerMetasVencidas(usuarioId: String): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND fechaLimite IS NOT NULL 
        AND fechaLimite BETWEEN datetime('now') AND datetime('now', '+7 days')
        ORDER BY fechaLimite ASC
    """)
    fun obtenerMetasProximasAVencer(usuarioId: String): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND fechaLimite IS NOT NULL 
        ORDER BY fechaLimite ASC
    """)
    fun obtenerMetasOrdenadasPorFecha(usuarioId: String): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND estado = 'EN_PROGRESO'
        AND (montoActual / CAST(montoObjetivo AS FLOAT)) * 100 >= :porcentajeProgreso
        ORDER BY (montoActual / CAST(montoObjetivo AS FLOAT)) DESC
    """)
    fun obtenerMetasConProgresoMinimo(usuarioId: String, porcentajeProgreso: Double = 50.0): Flow<List<meta_entidad>>

    @Query("""
        SELECT * FROM metas 
        WHERE usuarioId = :usuarioId 
        AND montoActual >= montoObjetivo
        AND estado != 'COMPLETADA'
        ORDER BY completadoEn DESC
    """)
    fun obtenerMetasListasParaCompletar(usuarioId: String): Flow<List<meta_entidad>>

    @Query("""
        SELECT 
            COUNT(*) as totalMetas,
            SUM(CASE WHEN estado = 'COMPLETADA' THEN 1 ELSE 0 END) as metasCompletadas,
            SUM(CASE WHEN estado = 'EN_PROGRESO' THEN 1 ELSE 0 END) as metasActivas,
            SUM(montoObjetivo) as montoObjetivoTotal,
            SUM(montoActual) as montoActualTotal
        FROM metas 
        WHERE usuarioId = :usuarioId
    """)
    suspend fun obtenerEstadisticasMetas(usuarioId: String): DatosEstadisticasMeta?

    @Query("""
        SELECT AVG((montoActual / CAST(montoObjetivo AS FLOAT)) * 100) as progresoPromedio
        FROM metas 
        WHERE usuarioId = :usuarioId 
        AND montoObjetivo > 0 
        AND estado = 'EN_PROGRESO'
    """)
    suspend fun obtenerProgresoPromedioMetasActivas(usuarioId: String): Double?

    @Query("""
        SELECT COUNT(*) FROM metas 
        WHERE usuarioId = :usuarioId AND estado = 'COMPLETADA'
    """)
    suspend fun contarMetasCompletadas(usuarioId: String): Int

    @Query("""
        SELECT COUNT(*) FROM metas 
        WHERE usuarioId = :usuarioId AND estado = 'EN_PROGRESO'
    """)
    suspend fun contarMetasActivas(usuarioId: String): Int

    @Query("""
        UPDATE metas 
        SET montoActual = :monto, 
            actualizadoEn = :actualizadoEn 
        WHERE id = :metaId
    """)
    suspend fun actualizarProgreso(metaId: String, monto: Double, actualizadoEn: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE metas 
        SET estado = :estado, 
            completadoEn = CASE WHEN :estado = 'COMPLETADA' THEN :completadoEn ELSE NULL END,
            actualizadoEn = :actualizadoEn 
        WHERE id = :metaId
    """)
    suspend fun actualizarEstado(
        metaId: String,
        estado: EstadoMeta,
        completadoEn: LocalDateTime? = null,
        actualizadoEn: LocalDateTime = LocalDateTime.now()
    )

    @Query("""
        UPDATE metas 
        SET montoObjetivo = :nuevoObjetivo, 
            actualizadoEn = :actualizadoEn 
        WHERE id = :metaId
    """)
    suspend fun actualizarMontoObjetivo(metaId: String, nuevoObjetivo: Double, actualizadoEn: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE metas 
        SET prioridad = :prioridad, 
            actualizadoEn = :actualizadoEn 
        WHERE id = :metaId
    """)
    suspend fun actualizarPrioridad(metaId: String, prioridad: PrioridadMeta, actualizadoEn: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE metas 
        SET fechaLimite = :nuevaFechaLimite, 
            actualizadoEn = :actualizadoEn 
        WHERE id = :metaId
    """)
    suspend fun actualizarFechaLimite(metaId: String, nuevaFechaLimite: LocalDateTime, actualizadoEn: LocalDateTime = LocalDateTime.now())

    @Query("SELECT COUNT(*) FROM metas WHERE usuarioId = :usuarioId")
    suspend fun contarMetas(usuarioId: String): Int

    @Query("SELECT COUNT(*) FROM metas WHERE usuarioId = :usuarioId AND categoria = :categoria")
    suspend fun contarMetasPorCategoria(usuarioId: String, categoria: CategoriaMeta): Int

    @Query("SELECT EXISTS(SELECT 1 FROM metas WHERE usuarioId = :usuarioId AND id = :metaId)")
    suspend fun existeMeta(usuarioId: String, metaId: String): Boolean
}

data class DatosEstadisticasMeta(
    val totalMetas: Int = 0,
    val metasCompletadas: Int = 0,
    val metasActivas: Int = 0,
    val montoObjetivoTotal: Double = 0.0,
    val montoActualTotal: Double = 0.0
) {
    val tasaCompletado: Double
        get() = if (totalMetas > 0) (metasCompletadas.toDouble() / totalMetas * 100) else 0.0

    val restantePorAhorrar: Double
        get() = (montoObjetivoTotal - montoActualTotal).coerceAtLeast(0.0)

    val progresoPromedio: Double
        get() = if (montoObjetivoTotal > 0) (montoActualTotal / montoObjetivoTotal * 100).coerceIn(0.0, 100.0) else 0.0
}