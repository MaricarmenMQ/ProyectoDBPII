package com.lvmh.pocketpet.data.local.repositorio

import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import com.lvmh.pocketpet.data.local.oad.transaccionOad
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class transaccionRepositorio @Inject constructor(
    private val transaccionOad: transaccionOad
) {

    suspend fun crear_transaccion(transaccion: transaccion_entidad): Long {
        return transaccionOad.insertar(transaccion)
    }

    fun obtener_transacciones_recientes(): Flow<List<transaccion_entidad>> {
        return transaccionOad.obtenerTransaccionesRecientes()
    }

    fun obtener_transacciones_por_rango_fechas(
        fecha_inicio: Long,
        fecha_fin: Long
    ): Flow<List<transaccion_entidad>> {
        return transaccionOad.obtenerTransaccionesPorRangoDeFechas(fecha_inicio, fecha_fin)
    }

    fun obtener_total_ingresos(
        fecha_inicio: Long,
        fecha_fin: Long
    ): Flow<Double?> {
        return transaccionOad.obtenerTotalDeIngresos(fecha_inicio, fecha_fin)
    }

    fun obtener_total_gastos(
        fecha_inicio: Long,
        fecha_fin: Long
    ): Flow<Double?> {
        return transaccionOad.obtenerTotalDeGastos(fecha_inicio, fecha_fin)
    }
}