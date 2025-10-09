package com.lvmh.pocketpet.data.local.oad

import androidx.room.*
import com.lvmh.pocketpet.data.local.entidades.presupuesto_entidad
import com.lvmh.pocketpet.data.local.entidades.PeriodoPresupuesto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface presupuestoOad {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(presupuesto: presupuesto_entidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(presupuestos: List<presupuesto_entidad>)

    @Update
    suspend fun actualizar(presupuesto: presupuesto_entidad)

    @Delete
    suspend fun eliminar(presupuesto: presupuesto_entidad)

    @Query("DELETE FROM presupuestos WHERE id = :presupuestoId")
    suspend fun eliminarPorId(presupuestoId: String)

    @Query("DELETE FROM presupuestos WHERE usuarioId= :usuarioId")
    suspend fun eliminarPorUsuario(usuarioId: String)

    @Query("SELECT * FROM presupuestos WHERE id = :presupuestoId")
    suspend fun obtenerPorId(presupuestoId: String): presupuesto_entidad?

    @Query("SELECT * FROM presupuestos WHERE usuarioId = :usuarioId ORDER BY actualizadoEn DESC")
    fun obtenerTodosPorUsuario(usuarioId: String): Flow<List<presupuesto_entidad>>

    @Query("SELECT * FROM presupuestos WHERE usuarioId = :usuarioId AND estaActivo = 1 ORDER BY monto DESC")
    fun obtenerActivos(usuarioId: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuarioId = :usuarioId 
        AND (:categoriaId IS NULL OR categoriaId = :categoriaId)
        AND (:periodo IS NULL OR periodo= :periodo)
        AND (:activo IS NULL OR estaActivo = :activo)
        ORDER BY actualizadoEn DESC
    """)
    fun filtrarPresupuestos(
        usuarioId: String,
        categoriaId: String? = null,
        periodo: PeriodoPresupuesto? = null,
        activo: Boolean? = null
    ): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuarioId = :usuarioId 
        AND cuentaId = :cuentaId 
        AND estaActivo = 1
        ORDER BY monto DESC
    """)
    fun obtenerPorCuenta(usuarioId: String, cuentaId: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuarioId = :usuarioId 
        AND categoriaId = :categoriaId 
        AND estaActivo = 1
        ORDER BY actualizadoEn DESC
    """)
    fun obtenerPorCategoria(usuarioId: String, categoriaId: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos
        WHERE usuarioId = :usuarioId 
        AND (nombre LIKE '%' || :busqueda || '%' OR notas LIKE '%' || :busqueda || '%')
        ORDER BY actualizadoEn DESC
    """)
    fun buscarPresupuestos(usuarioId: String, busqueda: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuarioId = :usuarioId 
        AND estaActivo = 1 
        AND monto > 0 
        AND ((gastado / CAST(monto AS FLOAT)) * 100) >= porcentajeAlerta
        ORDER BY (gastado / CAST(monto AS FLOAT)) DESC
        """)
    fun obtenerConAlertas(usuarioId: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuarioId = :usuarioId 
        AND estaActivo = 1 
        AND gastado > monto
        ORDER BY (gastado - monto) DESC
        """)
    fun obtenerExcedidos(usuarioId: String): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT 
            COUNT(*) as total,
            SUM(monto) as totalAmount,
            SUM(gastado) as totalSpent
        FROM presupuestos
        WHERE usuarioId = :usuarioId AND estaActivo = 1
    """)
    suspend fun obtenerEstadisticas(usuarioId: String): EstadisticasPresupuesto?

    @Query("""
        SELECT AVG(gastado / CAST(monto AS FLOAT) * 100) as averagePercentage
        FROM presupuestos
        WHERE usuarioId = :usuarioId AND monto > 0 AND estaActivo = 1
    """)
    suspend fun obtenerPromedioUso(usuarioId: String): Double?

    @Query("SELECT COUNT(*) FROM presupuestos WHERE usuarioId = :usuarioId AND gastado > monto")
    suspend fun contarExcedidos(usuarioId: String): Int

    @Query("""
        UPDATE presupuestos 
        SET gastado = :nuevoGasto, 
            actualizadoEn = :fechaActualizacion 
        WHERE id = :presupuestoId
    """)
    suspend fun actualizarGasto(presupuestoId: String, nuevoGasto: Double, fechaActualizacion: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE presupuestos 
        SET monto = :nuevoMonto, 
            actualizadoEn = :fechaActualizacion 
        WHERE id = :presupuestoId
    """)
    suspend fun actualizarMonto(presupuestoId: String, nuevoMonto: Double, fechaActualizacion: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE presupuestos 
        SET estaActivo = :activo, 
            actualizadoEn = :fechaActualizacion 
        WHERE id = :presupuestoId
    """)
    suspend fun actualizarEstado(presupuestoId: String, activo: Boolean, fechaActualizacion: LocalDateTime = LocalDateTime.now())

    @Query("""
        UPDATE presupuestos 
        SET porcentajeAlerta = :porcentajeAlerta, 
            actualizadoEn = :fechaActualizacion 
        WHERE id = :presupuestoId
    """)
    suspend fun actualizarPorcentajeAlerta(presupuestoId: String, porcentajeAlerta: Int, fechaActualizacion: LocalDateTime = LocalDateTime.now())

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuarioId = :usuarioId 
        AND periodo = :periodo 
        AND estaActivo = 1
        ORDER BY fechaInicio DESC
    """)
    fun obtenerPorPeriodo(usuarioId: String, periodo: PeriodoPresupuesto): Flow<List<presupuesto_entidad>>

    @Query("""
        SELECT * FROM presupuestos 
        WHERE usuarioId = :usuarioId 
        AND fechaInicio >= :fechaInicio 
        AND fechaFin <= :fechaFin
        ORDER BY fechaInicio DESC
    """)
    fun obtenerPorRangoDeFechas(usuarioId: String, fechaInicio: LocalDateTime, fechaFin: LocalDateTime): Flow<List<presupuesto_entidad>>

    @Query("SELECT COUNT(*) FROM presupuestos WHERE usuarioId = :usuarioId AND categoriaId = :categoriaId AND periodo = :periodo")
    suspend fun existePresupuestoParaCategoriaYPeriodo(usuarioId: String, categoriaId: String, periodo: PeriodoPresupuesto): Int

    @Query("SELECT COUNT(*) FROM presupuestos WHERE usuarioId = :usuarioId")
    suspend fun contarPorUsuario(usuarioId: String): Int
}

data class EstadisticasPresupuesto(
    val total: Int = 0,
    val totalAmount: Double = 0.0,
    val totalSpent: Double = 0.0
) {
    val montoRestante: Double
        get() = (totalAmount - totalSpent).coerceAtLeast(0.0)

    val porcentajeUsado: Double
        get() = if (totalAmount > 0) (totalSpent / totalAmount * 100).coerceIn(0.0, 100.0) else 0.0
}
