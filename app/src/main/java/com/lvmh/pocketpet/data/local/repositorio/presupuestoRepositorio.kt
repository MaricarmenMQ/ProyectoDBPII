package com.lvmh.pocketpet.data.local.repositorio

import com.lvmh.pocketpet.data.local.entidades.periodo_presupuesto
import com.lvmh.pocketpet.data.local.entidades.presupuesto_entidad
import com.lvmh.pocketpet.data.local.oad.presupuestoOad
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class presupuestoRepositorio @Inject constructor(
    private val presupuestoOad: presupuestoOad
) {
    suspend fun crear_presupuesto(presupuesto: presupuesto_entidad) {
        presupuestoOad.insertar(presupuesto)
    }

    suspend fun obtener_presupuesto_por_id(presupuesto_id: String): presupuesto_entidad? {
        return presupuestoOad.obtener_por_id(presupuesto_id)
    }

    fun obtener_todos_presupuestos(usuario_id: String): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.obtener_todos_por_usuario(usuario_id)
    }

    fun obtener_presupuestos_activos(usuario_id: String): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.obtener_presupuestos_activos(usuario_id)
    }

    suspend fun actualizar_presupuesto(presupuesto: presupuesto_entidad) {
        presupuestoOad.actualizar(presupuesto.copy(actualizado_en = LocalDateTime.now()))
    }

    suspend fun eliminar_presupuesto(presupuesto: presupuesto_entidad) {
        presupuestoOad.eliminar(presupuesto)
    }

    suspend fun eliminar_presupuesto_por_id(presupuesto_id: String) {
        presupuestoOad.eliminar_por_id(presupuesto_id)
    }

    fun filtrar_presupuestos(
        usuario_id: String,
        categoria_id: String? = null,
        periodo: periodo_presupuesto? = null,
        esta_activo: Boolean? = null
    ): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.filtrar_presupuestos(usuario_id, categoria_id, periodo, esta_activo)
    }

    fun obtener_presupuestos_por_cuenta(usuario_id: String, cuenta_id: String): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.obtener_presupuestos_por_cuenta(usuario_id, cuenta_id)
    }

    fun obtener_presupuestos_por_categoria(usuario_id: String, categoria_id: String): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.obtener_presupuestos_por_categoria(usuario_id, categoria_id)
    }

    fun buscar_presupuestos(usuario_id: String, consulta: String): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.buscar_presupuestos(usuario_id, consulta)
    }

    fun obtener_presupuestos_con_alertas(usuario_id: String): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.obtener_presupuestos_con_alertas(usuario_id)
    }

    fun obtener_presupuestos_excedidos(usuario_id: String): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.obtener_presupuestos_excedidos(usuario_id)
    }

    fun obtener_presupuestos_por_periodo(usuario_id: String, periodo: periodo_presupuesto): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.obtener_presupuestos_por_periodo(usuario_id, periodo)
    }

    suspend fun agregar_transaccion_a_presupuesto(presupuesto_id: String, monto: Double) {
        val presupuesto = presupuestoOad.obtener_por_id(presupuesto_id)
        presupuesto?.let {
            val nuevo_gastado = it.gastado + monto
            presupuestoOad.actualizar_gastado(presupuesto_id, nuevo_gastado)
        }
    }

    suspend fun remover_transaccion_de_presupuesto(presupuesto_id: String, monto: Double) {
        val presupuesto = presupuestoOad.obtener_por_id(presupuesto_id)
        presupuesto?.let {
            val nuevo_gastado = (it.gastado - monto).coerceAtLeast(0.0)
            presupuestoOad.actualizar_gastado(presupuesto_id, nuevo_gastado)
        }
    }

    suspend fun reiniciar_presupuesto(presupuesto_id: String) {
        val presupuesto = presupuestoOad.obtener_por_id(presupuesto_id)
        presupuesto?.let {
            val presupuesto_reiniciado = it.copy(
                gastado = 0.0,
                fecha_inicio = LocalDateTime.now(),
                fecha_fin = LocalDateTime.now().plusMonths(1),
                actualizado_en = LocalDateTime.now()
            )
            presupuestoOad.actualizar(presupuesto_reiniciado)
        }
    }

    suspend fun actualizar_porcentaje_alerta(presupuesto_id: String, porcentaje: Int) {
        val porcentaje_ajustado = porcentaje.coerceIn(0, 100)
        presupuestoOad.actualizar_porcentaje_alerta(presupuesto_id, porcentaje_ajustado)
    }

    suspend fun alternar_presupuesto_activo(presupuesto_id: String, esta_activo: Boolean) {
        presupuestoOad.actualizar_activo(presupuesto_id, esta_activo)
    }

    suspend fun actualizar_monto_presupuesto(presupuesto_id: String, nuevo_monto: Double) {
        if (nuevo_monto > 0) {
            presupuestoOad.actualizar_monto(presupuesto_id, nuevo_monto)
        }
    }

    suspend fun obtener_estadisticas_presupuesto(usuario_id: String) = flow {
        val estadisticas = presupuestoOad.obtener_estadisticas_presupuesto(usuario_id)
        emit(estadisticas)
    }

    suspend fun obtener_porcentaje_promedio_usado(usuario_id: String): Double? {
        return presupuestoOad.obtener_porcentaje_promedio_usado(usuario_id)
    }

    suspend fun obtener_cantidad_presupuestos_excedidos(usuario_id: String): Int {
        return presupuestoOad.obtener_cantidad_presupuestos_excedidos(usuario_id)
    }

    fun obtener_presupuestos_en_rango_fechas(
        usuario_id: String,
        fecha_inicio: LocalDateTime,
        fecha_fin: LocalDateTime
    ): Flow<List<presupuesto_entidad>> {
        return presupuestoOad.obtener_presupuestos_en_rango_fechas(usuario_id, fecha_inicio, fecha_fin)
    }

    suspend fun existe_presupuesto_para_categoria_y_periodo(
        usuario_id: String,
        categoria_id: String,
        periodo: periodo_presupuesto
    ): Boolean {
        return presupuestoOad.existe_presupuesto_para_categoria_y_periodo(usuario_id, categoria_id, periodo) > 0
    }

    suspend fun contar_presupuestos(usuario_id: String): Int {
        return presupuestoOad.contar_presupuestos(usuario_id)
    }

    fun obtener_presupuestos_advertencia(usuario_id: String, umbral_advertencia: Double = 90.0): Flow<List<presupuesto_entidad>> {
        return flow {
            presupuestoOad.obtener_presupuestos_activos(usuario_id).collect { presupuestos ->
                val presupuestos_advertencia = presupuestos.filter {
                    it.porcentaje_usado >= umbral_advertencia
                }
                emit(presupuestos_advertencia)
            }
        }
    }
}