package com.lvmh.pocketpet.presentacion.pantallas.vistamodelo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvmh.pocketpet.data.local.entidades.EstadoMeta
import com.lvmh.pocketpet.data.local.entidades.meta_entidad
import com.lvmh.pocketpet.data.local.entidades.CategoriaMeta
import com.lvmh.pocketpet.data.local.entidades.PrioridadMeta
import com.lvmh.pocketpet.data.local.repositorio.metaRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDateTime

@HiltViewModel
class MetaVerModelo @Inject constructor(
        private val metaRepositorio: metaRepositorio
    ) : ViewModel() {

        private val _estadoUiMeta = MutableStateFlow<EstadoUiMeta>(EstadoUiMeta.Inactivo)
        val estadoUiMeta: StateFlow<EstadoUiMeta> = _estadoUiMeta.asStateFlow()

        private val _listaMetas = MutableStateFlow<List<meta_entidad>>(emptyList())
        val listaMetas: StateFlow<List<meta_entidad>> = _listaMetas.asStateFlow()

        private val _metaSeleccionada = MutableStateFlow<meta_entidad?>(null)
        val metaSeleccionada: StateFlow<meta_entidad?> = _metaSeleccionada.asStateFlow()

        private val _metasUrgentes = MutableStateFlow<List<meta_entidad>>(emptyList())
        val metasUrgentes: StateFlow<List<meta_entidad>> = _metasUrgentes.asStateFlow()

        private val _estadisticasMetas = MutableStateFlow<EstadisticasMeta?>(null)
        val estadisticasMetas: StateFlow<EstadisticasMeta?> = _estadisticasMetas.asStateFlow()

        fun crearMeta(
            usuarioId: String,
            nombre: String,
            descripcion: String = "",
            montoObjetivo: Double,
            categoria: CategoriaMeta = CategoriaMeta.AHORRO,
            prioridad: PrioridadMeta = PrioridadMeta.MEDIA,
            fechaLimite: LocalDateTime? = null,
            categoriaId: String = "",
            cuentaId: String? = null
        ) {
            if (nombre.isBlank()) {
                _estadoUiMeta.value = EstadoUiMeta.Error("El nombre de la meta es requerido")
                return
            }

            if (montoObjetivo <= 0) {
                _estadoUiMeta.value = EstadoUiMeta.Error("El monto objetivo debe ser mayor a 0")
                return
            }

            if (fechaLimite != null && fechaLimite.isBefore(LocalDateTime.now())) {
                _estadoUiMeta.value = EstadoUiMeta.Error("La fecha límite no puede ser en el pasado")
                return
            }

            viewModelScope.launch {
                try {
                    _estadoUiMeta.value = EstadoUiMeta.Cargando

                    val nuevaMeta = meta_entidad(
                        id = "meta_${System.currentTimeMillis()}",
                        usuarioId = usuarioId,
                        categoriaId = categoriaId,
                        cuentaId = cuentaId,
                        nombre = nombre,
                        descripcion = descripcion,
                        montoObjetivo = montoObjetivo,
                        montoActual = 0.0,
                        estado = EstadoMeta.EN_PROGRESO,
                        prioridad = prioridad,
                        fechaLimite = fechaLimite,
                        categoria = categoria,
                        esAutomatico = false
                    )

                    metaRepositorio.crearMeta(nuevaMeta)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Meta creada exitosamente")

                    cargarMetasActivas(usuarioId)

                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al crear meta")
                }
            }
        }

        fun cargarTodasLasMetas(usuarioId: String) {
            viewModelScope.launch {
                try {
                    _estadoUiMeta.value = EstadoUiMeta.Cargando

                    metaRepositorio.obtenerTodasLasMetas(usuarioId).collect { metas ->
                        _listaMetas.value = metas
                        _estadoUiMeta.value = EstadoUiMeta.Inactivo
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al cargar metas")
                }
            }
        }

        fun cargarMetasActivas(usuarioId: String) {
            viewModelScope.launch {
                try {
                    _estadoUiMeta.value = EstadoUiMeta.Cargando

                    metaRepositorio.obtenerMetasActivas(usuarioId).collect { metas ->
                        _listaMetas.value = metas
                        _estadoUiMeta.value = EstadoUiMeta.Inactivo
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al cargar metas")
                }
            }
        }

        fun cargarMetasCompletadas(usuarioId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.obtenerMetasCompletadas(usuarioId).collect { metas ->
                        _listaMetas.value = metas
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al cargar metas completadas")
                }
            }
        }

        fun cargarMetasVencidas(usuarioId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.obtenerMetasVencidas(usuarioId).collect { metas ->
                        _listaMetas.value = metas
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al cargar metas vencidas")
                }
            }
        }

        fun cargarMetasProximasAVencer(usuarioId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.obtenerMetasProximasAVencer(usuarioId).collect { metas ->
                        _metasUrgentes.value = metas
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al cargar metas próximas")
                }
            }
        }

        fun cargarMetasUrgentes(usuarioId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.obtenerMetasUrgentes(usuarioId).collect { metas ->
                        _metasUrgentes.value = metas
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al cargar metas urgentes")
                }
            }
        }

        fun cargarMetasEnRiesgo(usuarioId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.obtenerMetasEnRiesgo(usuarioId).collect { metas ->
                        _metasUrgentes.value = metas
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al cargar metas en riesgo")
                }
            }
        }

        fun buscarMetas(usuarioId: String, consulta: String) {
            viewModelScope.launch {
                try {
                    if (consulta.isBlank()) {
                        cargarMetasActivas(usuarioId)
                        return@launch
                    }

                    metaRepositorio.buscarMetas(usuarioId, consulta).collect { metas ->
                        _listaMetas.value = metas
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error en búsqueda")
                }
            }
        }

        fun filtrarMetas(
            usuarioId: String,
            estado: EstadoMeta? = null,
            prioridad: PrioridadMeta? = null,
            categoria: CategoriaMeta? = null,
            ordenarPorFechaLimite: Boolean = false
        ) {
            viewModelScope.launch {
                try {
                    metaRepositorio.filtrarMetas(usuarioId, estado, prioridad, categoria, ordenarPorFechaLimite)
                        .collect { metas ->
                            _listaMetas.value = metas
                        }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error en filtrado")
                }
            }
        }

        fun obtenerMetaPorId(metaId: String) {
            viewModelScope.launch {
                try {
                    val meta = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = meta
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al obtener meta")
                }
            }
        }

        fun actualizarProgresoMeta(metaId: String, nuevoMonto: Double) {
            if (nuevoMonto < 0) {
                _estadoUiMeta.value = EstadoUiMeta.Error("El monto no puede ser negativo")
                return
            }

            viewModelScope.launch {
                try {
                    _estadoUiMeta.value = EstadoUiMeta.Cargando
                    metaRepositorio.actualizarProgresoMeta(metaId, nuevoMonto)

                    val metaActualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = metaActualizada

                    _estadoUiMeta.value = if (metaActualizada?.estaCompletada == true) {
                        EstadoUiMeta.Exito("¡Meta completada! 🎉")
                    } else {
                        EstadoUiMeta.Exito("Progreso actualizado")
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al actualizar progreso")
                }
            }
        }

        fun incrementarProgresoMeta(metaId: String, monto: Double) {
            viewModelScope.launch {
                try {
                    metaRepositorio.incrementarProgresoMeta(metaId, monto)

                    val metaActualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = metaActualizada

                    _estadoUiMeta.value = if (metaActualizada?.estaCompletada == true) {
                        EstadoUiMeta.Exito("¡Meta completada! 🎉")
                    } else {
                        EstadoUiMeta.Exito("Incremento registrado")
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al incrementar")
                }
            }
        }

        fun completarMeta(metaId: String) {
            viewModelScope.launch {
                try {
                    _estadoUiMeta.value = EstadoUiMeta.Cargando
                    metaRepositorio.completarMeta(metaId)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("¡Meta completada! 🎉")

                    val actualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = actualizada
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al completar meta")
                }
            }
        }

        fun pausarMeta(metaId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.pausarMeta(metaId)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Meta pausada")

                    val actualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = actualizada
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al pausar meta")
                }
            }
        }

        fun reanudarMeta(metaId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.reanudarMeta(metaId)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Meta reanudada")

                    val actualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = actualizada
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al reanudar meta")
                }
            }
        }

        fun abandonarMeta(metaId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.abandonarMeta(metaId)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Meta abandonada")

                    val actualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = actualizada
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al abandonar meta")
                }
            }
        }

        fun actualizarMontoObjetivo(metaId: String, nuevoObjetivo: Double) {
            if (nuevoObjetivo <= 0) {
                _estadoUiMeta.value = EstadoUiMeta.Error("El monto debe ser mayor a 0")
                return
            }

            viewModelScope.launch {
                try {
                    metaRepositorio.actualizarMontoObjetivo(metaId, nuevoObjetivo)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Meta actualizada")

                    val actualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = actualizada
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al actualizar meta")
                }
            }
        }

        fun actualizarPrioridad(metaId: String, prioridad: PrioridadMeta) {
            viewModelScope.launch {
                try {
                    metaRepositorio.actualizarPrioridad(metaId, prioridad)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Prioridad actualizada")

                    val actualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = actualizada
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al actualizar prioridad")
                }
            }
        }

        fun actualizarFechaLimite(metaId: String, nuevaFechaLimite: LocalDateTime) {
            if (nuevaFechaLimite.isBefore(LocalDateTime.now())) {
                _estadoUiMeta.value = EstadoUiMeta.Error("La fecha no puede ser en el pasado")
                return
            }

            viewModelScope.launch {
                try {
                    metaRepositorio.actualizarFechaLimite(metaId, nuevaFechaLimite)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Fecha actualizada")

                    val actualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = actualizada
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al actualizar fecha")
                }
            }
        }

        fun reiniciarMeta(metaId: String) {
            viewModelScope.launch {
                try {
                    _estadoUiMeta.value = EstadoUiMeta.Cargando
                    metaRepositorio.reiniciarMeta(metaId)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Meta reiniciada")

                    val actualizada = metaRepositorio.obtenerMetaPorId(metaId)
                    _metaSeleccionada.value = actualizada
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al reiniciar meta")
                }
            }
        }

        fun eliminarMeta(metaId: String) {
            viewModelScope.launch {
                try {
                    _estadoUiMeta.value = EstadoUiMeta.Cargando
                    metaRepositorio.eliminarMetaPorId(metaId)
                    _estadoUiMeta.value = EstadoUiMeta.Exito("Meta eliminada")
                    _metaSeleccionada.value = null
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al eliminar meta")
                }
            }
        }

        fun cargarEstadisticasMetas(usuarioId: String) {
            viewModelScope.launch {
                try {
                    metaRepositorio.obtenerEstadisticasMetas(usuarioId).collect { estadisticas ->
                        _estadisticasMetas.value = EstadisticasMeta(
                            totalMetas = estadisticas?.totalMetas ?: 0,
                            metasCompletadas = estadisticas?.metasCompletadas ?: 0,
                            metasActivas = estadisticas?.metasActivas ?: 0,
                            montoObjetivoTotal = estadisticas?.montoObjetivoTotal ?: 0.0,
                            montoActualTotal = estadisticas?.montoActualTotal ?: 0.0,
                            tasaCompletado = estadisticas?.tasaCompletado ?: 0.0,
                            restantePorAhorrar = estadisticas?.restantePorAhorrar ?: 0.0,
                            progresoPromedio = estadisticas?.progresoPromedio ?: 0.0
                        )
                    }
                } catch (e: Exception) {
                    _estadoUiMeta.value = EstadoUiMeta.Error(e.message ?: "Error al cargar estadísticas")
                }
            }
        }

        fun limpiarEstado() {
            _estadoUiMeta.value = EstadoUiMeta.Inactivo
            _metaSeleccionada.value = null
        }
    }

    sealed class EstadoUiMeta {
        object Inactivo : EstadoUiMeta()
        object Cargando : EstadoUiMeta()
        data class Exito(val mensaje: String) : EstadoUiMeta()
        data class Error(val mensaje: String) : EstadoUiMeta()
    }

    data class EstadisticasMeta(
        val totalMetas: Int = 0,
        val metasCompletadas: Int = 0,
        val metasActivas: Int = 0,
        val montoObjetivoTotal: Double = 0.0,
        val montoActualTotal: Double = 0.0,
        val tasaCompletado: Double = 0.0,
        val restantePorAhorrar: Double = 0.0,
        val progresoPromedio: Double = 0.0
    ) {
        val estaEnCamino: Boolean
            get() = progresoPromedio > 0.0 && tasaCompletado < 100.0

        val todasCompletadas: Boolean
            get() = tasaCompletado == 100.0 && totalMetas > 0

        val sinMetas: Boolean
            get() = totalMetas == 0
    }