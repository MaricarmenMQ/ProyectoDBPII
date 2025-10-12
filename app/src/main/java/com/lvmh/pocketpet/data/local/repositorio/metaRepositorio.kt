package com.lvmh.pocketpet.data.local.repositorio

import com.lvmh.pocketpet.data.local.entidades.CategoriaMeta
import com.lvmh.pocketpet.data.local.entidades.EstadoMeta
import com.lvmh.pocketpet.data.local.entidades.meta_entidad
import com.lvmh.pocketpet.data.local.entidades.PrioridadMeta
import com.lvmh.pocketpet.data.local.oad.metaOad
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class metaRepositorio @Inject constructor(
    private val metaOad: metaOad
) {

    suspend fun crearMeta(meta: meta_entidad) {
        metaOad.insertar(meta)
    }

    suspend fun obtenerMetaPorId(metaId: String): meta_entidad? {
        return metaOad.obtenerPorId(metaId)
    }

    fun obtenerTodasLasMetas(usuarioId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerTodasPorUsuario(usuarioId)
    }

    fun obtenerMetasActivas(usuarioId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasActivas(usuarioId)
    }

    fun obtenerMetasCompletadas(usuarioId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasCompletadas(usuarioId)
    }

    suspend fun actualizarMeta(meta: meta_entidad) {
        metaOad.actualizar(meta.copy(actualizadoEn = LocalDateTime.now()))
    }

    suspend fun eliminarMeta(meta: meta_entidad) {
        metaOad.eliminar(meta)
    }

    suspend fun eliminarMetaPorId(metaId: String) {
        metaOad.eliminarPorId(metaId)
    }

    fun filtrarMetas(
        usuarioId: String,
        estado: EstadoMeta? = null,
        prioridad: PrioridadMeta? = null,
        categoria: CategoriaMeta? = null,
        ordenarPorFechaLimite: Boolean = false
    ): Flow<List<meta_entidad>> {
        return metaOad.filtrarMetas(
            usuarioId,
            estado,
            prioridad,
            categoria,
            if (ordenarPorFechaLimite) 1 else 0
        )
    }

    fun obtenerMetasPorCategoria(usuarioId: String, categoriaId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasPorCategoria(usuarioId, categoriaId)
    }

    fun obtenerMetasPorCuenta(usuarioId: String, cuentaId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasPorCuenta(usuarioId, cuentaId)
    }

    fun buscarMetas(usuarioId: String, consulta: String): Flow<List<meta_entidad>> {
        return metaOad.buscarMetas(usuarioId, consulta)
    }

    fun obtenerMetasVencidas(usuarioId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasVencidas(usuarioId)
    }

    fun obtenerMetasProximasAVencer(usuarioId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasProximasAVencer(usuarioId)
    }

    fun obtenerMetasOrdenadasPorFecha(usuarioId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasOrdenadasPorFecha(usuarioId)
    }

    fun obtenerMetasConProgresoMinimo(
        usuarioId: String,
        porcentajeProgreso: Double = 50.0
    ): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasConProgresoMinimo(usuarioId, porcentajeProgreso)
    }

    fun obtenerMetasListasParaCompletar(usuarioId: String): Flow<List<meta_entidad>> {
        return metaOad.obtenerMetasListasParaCompletar(usuarioId)
    }

    suspend fun actualizarProgresoMeta(metaId: String, nuevoMonto: Double) {
        val meta = metaOad.obtenerPorId(metaId)
        meta?.let {
            metaOad.actualizarProgreso(metaId, nuevoMonto)

            if (nuevoMonto >= it.montoObjetivo && it.estado != EstadoMeta.COMPLETADA) {
                completarMeta(metaId)
            }
        }
    }

    suspend fun incrementarProgresoMeta(metaId: String, monto: Double) {
        val meta = metaOad.obtenerPorId(metaId)
        meta?.let {
            val nuevoMonto = it.montoActual + monto
            actualizarProgresoMeta(metaId, nuevoMonto)
        }
    }

    suspend fun completarMeta(metaId: String) {
        metaOad.actualizarEstado(
            metaId,
            EstadoMeta.COMPLETADA,
            completadoEn = LocalDateTime.now()
        )
    }

    suspend fun pausarMeta(metaId: String) {
        metaOad.actualizarEstado(metaId, EstadoMeta.PAUSADA)
    }

    suspend fun reanudarMeta(metaId: String) {
        metaOad.actualizarEstado(metaId, EstadoMeta.EN_PROGRESO)
    }

    suspend fun abandonarMeta(metaId: String) {
        metaOad.actualizarEstado(metaId, EstadoMeta.ABANDONADA)
    }

    suspend fun actualizarMontoObjetivo(metaId: String, nuevoObjetivo: Double) {
        if (nuevoObjetivo > 0) {
            metaOad.actualizarMontoObjetivo(metaId, nuevoObjetivo)
        }
    }

    suspend fun actualizarPrioridad(metaId: String, prioridad: PrioridadMeta) {
        metaOad.actualizarPrioridad(metaId, prioridad)
    }

    suspend fun actualizarFechaLimite(metaId: String, nuevaFechaLimite: LocalDateTime) {
        metaOad.actualizarFechaLimite(metaId, nuevaFechaLimite)
    }

    suspend fun reiniciarMeta(metaId: String) {
        val meta = metaOad.obtenerPorId(metaId)
        meta?.let {
            val metaReiniciada = it.copy(
                montoActual = 0.0,
                estado = EstadoMeta.EN_PROGRESO,
                actualizadoEn = LocalDateTime.now()
            )
            metaOad.actualizar(metaReiniciada)
        }
    }

    suspend fun obtenerEstadisticasMetas(usuarioId: String) = flow {
        val estadisticas = metaOad.obtenerEstadisticasMetas(usuarioId)
        emit(estadisticas)
    }

    suspend fun obtenerProgresoPromedioMetasActivas(usuarioId: String): Double? {
        return metaOad.obtenerProgresoPromedioMetasActivas(usuarioId)
    }

    suspend fun contarMetasCompletadas(usuarioId: String): Int {
        return metaOad.contarMetasCompletadas(usuarioId)
    }

    suspend fun contarMetasActivas(usuarioId: String): Int {
        return metaOad.contarMetasActivas(usuarioId)
    }

    fun obtenerMetasUrgentes(usuarioId: String): Flow<List<meta_entidad>> = flow {
        metaOad.obtenerMetasProximasAVencer(usuarioId).collect { metas ->
            val metasUrgentes = metas.filter {
                it.diasRestantes?.let { dias -> dias <= 3 } ?: false
            }
            emit(metasUrgentes)
        }
    }

    fun obtenerMetasEnRiesgo(usuarioId: String): Flow<List<meta_entidad>> = flow {
        metaOad.obtenerMetasActivas(usuarioId).collect { metas ->
            val metasEnRiesgo = metas.filter { meta ->
                meta.diasRestantes?.let { dias ->
                    dias > 0 && meta.porcentajeCompletado < (100 * dias / 365)
                } ?: false
            }
            emit(metasEnRiesgo)
        }
    }

    suspend fun existeMeta(usuarioId: String, metaId: String): Boolean {
        return metaOad.existeMeta(usuarioId, metaId)
    }

    suspend fun contarMetas(usuarioId: String): Int {
        return metaOad.contarMetas(usuarioId)
    }

    suspend fun contarMetasPorCategoria(usuarioId: String, categoria: CategoriaMeta): Int {
        return metaOad.contarMetasPorCategoria(usuarioId, categoria)
    }
}
