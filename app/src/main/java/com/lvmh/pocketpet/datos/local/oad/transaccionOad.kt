package com.lvmh.pocketpet.datos.local.oad

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lvmh.pocketpet.datos.local.entidades.transaccionesentidades
import kotlinx.coroutines.flow.Flow

@Dao
interface transaccionOad {

    // obtiene las 5 transacciones más recientes, ordenadas por fecha descendente
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC LIMIT 5")
    fun obtenerTransaccionesRecientes(): Flow<List<transaccionesentidades>>

    // obtiene transacciones dentro de un rango de fechas
    @Query("SELECT * FROM transacciones WHERE fecha BETWEEN :fechaInicio AND :fechaFin")
    fun obtenerTransaccionesPorRangoDeFechas(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<List<transaccionesentidades>>

    @Insert
    suspend fun insertar(transaccion: transaccionesentidades): Long
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'INGRESO' AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun obtenerTotalDeIngresos(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<Double?>

    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'GASTO' AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun obtenerTotalDeGastos(
        fechaInicio: Long,
        fechaFin: Long
    ): Flow<Double?>
}