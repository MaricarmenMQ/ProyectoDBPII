package com.lvmh.pocketpet.data.local.oad

import git pull origin main
androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import kotlinx.coroutines.flow.Flow

@Dao
interface transaccionOad {

    // obtiene las 5 transacciones más recientes, ordenadas por fecha descendente
    @Query("SELECT * FROM transaccion ORDER BY fecha DESC LIMIT 5")
    fun obtenerTransaccionesRecientes(): Flow<List<transaccion_entidad>>

    // obtiene transacciones dentro de un rango de fechas
    @Query("SELECT * FROM transaccion WHERE fecha BETWEEN :fechaInicio AND :fechaFin")
    fun obtenerTransaccionesPorRangoDeFechas(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<List<transaccion_entidad>>

    @Insert
    suspend fun insertar(transaccion: transaccion_entidad): Long
    @Query("SELECT SUM(monto) FROM transaccion WHERE tipo = 'INGRESO' AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun obtenerTotalDeIngresos(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<Double?>

    @Query("SELECT SUM(monto) FROM transaccion WHERE tipo = 'GASTO' AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun obtenerTotalDeGastos(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<Double?>
}