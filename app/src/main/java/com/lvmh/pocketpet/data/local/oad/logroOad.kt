package com.lvmh.pocketpet.data.local.oad

import androidx.room.*
import com.lvmh.pocketpet.data.local.entidades.logro_entidad
import kotlinx.coroutines.flow.Flow

@Dao
interface logroOad {

    @Query("SELECT * FROM logros ORDER BY desbloqueado DESC, categoria, rareza DESC")
    fun obtener_todos_logros(): Flow<List<logro_entidad>>
    @Query("SELECT * FROM logros WHERE desbloqueado = 1 ORDER BY fecha_desbloqueo DESC")
    fun obtener_logros_desbloqueados(): Flow<List<logro_entidad>>
    @Query("SELECT * FROM logros WHERE desbloqueado = 0 ORDER BY categoria, rareza DESC")
    fun obtener_logros_bloqueados(): Flow<List<logro_entidad>>
    @Query("SELECT * FROM logros WHERE categoria = :categoria ORDER BY desbloqueado DESC, rareza DESC")
    fun obtener_logros_por_categoria(categoria: String): Flow<List<logro_entidad>>
    @Query("SELECT * FROM logros WHERE codigo_logro = :codigo LIMIT 1")
    suspend fun obtener_logro_por_codigo(codigo: String): logro_entidad?
    @Query("SELECT desbloqueado FROM logros WHERE codigo_logro = :codigo")
    suspend fun esta_desbloqueado(codigo: String): Boolean?
    @Query("SELECT COUNT(*) FROM logros WHERE desbloqueado = 1")
    suspend fun contar_logros_desbloqueados(): Int

    @Query("SELECT COUNT(*) FROM logros")
    suspend fun contar_total_logros(): Int
    @Query("SELECT progreso_actual FROM logros WHERE codigo_logro = :codigo")
    suspend fun obtener_progreso(codigo: String): Int?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar_logro(logro: logro_entidad)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar_logros(logros: List<logro_entidad>)

    @Update
    suspend fun actualizar_logro(logro: logro_entidad)

    @Query("""
        UPDATE logros 
        SET desbloqueado = 1, 
            fecha_desbloqueo = :timestamp 
        WHERE codigo_logro = :codigo
    """)
    suspend fun desbloquear_logro(codigo: String, timestamp: Long = System.currentTimeMillis())

    @Query("""
        UPDATE logros 
        SET progreso_actual = :progreso,
            desbloqueado = CASE 
                WHEN :progreso >= progreso_total THEN 1 
                ELSE desbloqueado 
            END,
            fecha_desbloqueo = CASE 
                WHEN :progreso >= progreso_total THEN :timestamp 
                ELSE fecha_desbloqueo 
            END
        WHERE codigo_logro = :codigo
    """)
    suspend fun actualizar_progreso(
        codigo: String,
        progreso: Int,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE logros 
        SET progreso_actual = progreso_actual + 1,
            desbloqueado = CASE 
                WHEN progreso_actual + 1 >= progreso_total THEN 1 
                ELSE desbloqueado 
            END,
            fecha_desbloqueo = CASE 
                WHEN progreso_actual + 1 >= progreso_total THEN :timestamp 
                ELSE fecha_desbloqueo 
            END
        WHERE codigo_logro = :codigo
    """)
    suspend fun incrementar_progreso(codigo: String, timestamp: Long = System.currentTimeMillis())

    @Query("""
        SELECT CAST(
            (SELECT COUNT(*) FROM logros WHERE desbloqueado = 1) * 100.0 
            / COUNT(*) 
        AS INTEGER)
        FROM logros
    """)
    suspend fun obtener_porcentaje_completado(): Int
    @Query("SELECT * FROM logros WHERE desbloqueado = 1 ORDER BY fecha_desbloqueo DESC LIMIT 5")
    fun obtener_ultimos_logros(): Flow<List<logro_entidad>>
    @Query("SELECT SUM(puntos_experiencia) FROM logros WHERE desbloqueado = 1")
    suspend fun obtener_total_xp(): Int?
    @Query("SELECT SUM(salud_mascota) FROM logros WHERE desbloqueado = 1")
    suspend fun obtener_total_salud_ganada(): Int?
    @Query("SELECT * FROM logros WHERE rareza = :rareza ORDER BY desbloqueado DESC")
    fun obtener_logros_por_rareza(rareza: String): Flow<List<logro_entidad>>
    @Query("SELECT COUNT(*) FROM logros WHERE categoria = :categoria AND desbloqueado = 1")
    suspend fun contar_logros_categoria(categoria: String): Int

    @Query("UPDATE logros SET desbloqueado = 0, progreso_actual = 0, fecha_desbloqueo = NULL")
    suspend fun reiniciar_todos_logros()
    @Query("DELETE FROM logros")
    suspend fun eliminar_todos_logros()
    @Query("SELECT COUNT(*) FROM logros")
    suspend fun contar_logros(): Int
}