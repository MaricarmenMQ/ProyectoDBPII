package com.lvmh.pocketpet.data.local.oad

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import kotlinx.coroutines.flow.Flow

@Dao
interface transaccionOad {

    @Insert
    suspend fun insertar(transaccion: transaccion_entidad): Long

    @Update
    suspend fun actualizar(transaccion: transaccion_entidad)

    @Delete
    suspend fun eliminar(transaccion: transaccion_entidad)

    @Query("DELETE FROM transacciones WHERE id = :transaccion_id")
    suspend fun eliminar_por_id(transaccion_id: Long)

    @Query("SELECT * FROM transacciones WHERE id = :transaccion_id")
    suspend fun obtener_por_id(transaccion_id: Long): transaccion_entidad?

    @Query("SELECT * FROM transacciones ORDER BY fecha DESC LIMIT 5")
    fun obtenerTransaccionesRecientes(): Flow<List<transaccion_entidad>>

    @Query("SELECT * FROM transacciones WHERE fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    fun obtenerTransaccionesPorRangoDeFechas(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<List<transaccion_entidad>>

    @Query("SELECT * FROM transacciones WHERE tipo = :tipo ORDER BY fecha DESC")
    fun obtener_por_tipo(tipo: String): Flow<List<transaccion_entidad>>

    @Query("SELECT * FROM transacciones WHERE id_categoria = :categoria_id ORDER BY fecha DESC")
    fun obtener_por_categoria(categoria_id: String): Flow<List<transaccion_entidad>>

    @Query("SELECT * FROM transacciones WHERE id_cuenta = :cuenta_id ORDER BY fecha DESC")
    fun obtener_por_cuenta(cuenta_id: String): Flow<List<transaccion_entidad>>

    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'ingreso' AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun obtenerTotalDeIngresos(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<Double?>

    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'gasto' AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun obtenerTotalDeGastos(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<Double?>

    @Query("SELECT * FROM transacciones WHERE descripcion LIKE '%' || :consulta || '%' OR notas LIKE '%' || :consulta || '%' ORDER BY fecha DESC")
    fun buscar_transacciones(consulta: String): Flow<List<transaccion_entidad>>

    @Query("SELECT COUNT(*) FROM transacciones WHERE tipo = :tipo AND fecha BETWEEN :fechaInicio AND :fechaFin")
    suspend fun contar_transacciones_por_tipo(tipo: String, fechaInicio: Long, fechaFin: Long): Int

    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun obtener_todas(): Flow<List<transaccion_entidad>>

    @Query("DELETE FROM transacciones")
    suspend fun eliminar_todas()
}