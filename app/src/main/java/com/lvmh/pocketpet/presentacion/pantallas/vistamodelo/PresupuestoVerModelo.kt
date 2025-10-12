package com.lvmh.pocketpet.presentacion.pantallas.vistamodelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvmh.pocketpet.data.local.entidades.periodo_presupuesto
import com.lvmh.pocketpet.data.local.entidades.presupuesto_entidad
import com.lvmh.pocketpet.data.local.repositorio.presupuestoRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDateTime

@HiltViewModel
class PresupuestoVerModelo @Inject constructor(
    private val repositorioPresupuesto: presupuestoRepositorio
) : ViewModel() {

    private val _estadoUiPresupuesto = MutableStateFlow<EstadoUiPresupuesto>(EstadoUiPresupuesto.Inactivo)
    val estadoUiPresupuesto: StateFlow<EstadoUiPresupuesto> = _estadoUiPresupuesto.asStateFlow()

    private val _listaPresupuestos = MutableStateFlow<List<presupuesto_entidad>>(emptyList())
    val listaPresupuestos: StateFlow<List<presupuesto_entidad>> = _listaPresupuestos.asStateFlow()

    private val _presupuestoSeleccionado = MutableStateFlow<presupuesto_entidad?>(null)
    val presupuestoSeleccionado: StateFlow<presupuesto_entidad?> = _presupuestoSeleccionado.asStateFlow()

    private val _presupuestosAlerta = MutableStateFlow<List<presupuesto_entidad>>(emptyList())
    val presupuestosAlerta: StateFlow<List<presupuesto_entidad>> = _presupuestosAlerta.asStateFlow()

    private val _estadisticasPresupuesto = MutableStateFlow<EstadisticasPresupuesto?>(null)
    val estadisticasPresupuesto: StateFlow<EstadisticasPresupuesto?> = _estadisticasPresupuesto.asStateFlow()

    fun crear_presupuesto(
        usuario_id: String,
        categoria_id: String,
        nombre: String,
        monto: Double,
        periodo: periodo_presupuesto = periodo_presupuesto.MENSUAL,
        porcentaje_alerta: Int = 80
    ) {
        if (nombre.isBlank()) {
            _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error("El nombre del presupuesto es requerido")
            return
        }

        if (monto <= 0) {
            _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error("El monto debe ser mayor a 0")
            return
        }

        if (porcentaje_alerta !in 1..100) {
            _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error("El porcentaje de alerta debe estar entre 1 y 100")
            return
        }

        viewModelScope.launch {
            try {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Cargando

                val nuevo_presupuesto = presupuesto_entidad(
                    id = "presupuesto_${System.currentTimeMillis()}",
                    usuario_id = usuario_id,
                    categoria_id = categoria_id,
                    nombre = nombre,
                    monto = monto,
                    gastado = 0.0,
                    periodo = periodo,
                    fecha_inicio = LocalDateTime.now(),
                    fecha_fin = LocalDateTime.now().plusMonths(
                        when (periodo) {
                            periodo_presupuesto.SEMANAL -> 1
                            periodo_presupuesto.MENSUAL -> 1
                            periodo_presupuesto.TRIMESTRAL -> 3
                            periodo_presupuesto.ANUAL -> 12
                            periodo_presupuesto.PERSONALIZADO -> 1
                        }
                    ),
                    porcentaje_alerta = porcentaje_alerta,
                    esta_activo = true
                )

                repositorioPresupuesto.crear_presupuesto(nuevo_presupuesto)
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Exito("Presupuesto creado exitosamente")
                cargar_presupuestos_activos(usuario_id)

            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al crear presupuesto")
            }
        }
    }

    fun cargar_todos_presupuestos(usuario_id: String) {
        viewModelScope.launch {
            try {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Cargando
                repositorioPresupuesto.obtener_todos_presupuestos(usuario_id).collect { presupuestos ->
                    _listaPresupuestos.value = presupuestos
                    _estadoUiPresupuesto.value = EstadoUiPresupuesto.Inactivo
                }
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al cargar presupuestos")
            }
        }
    }

    fun cargar_presupuestos_activos(usuario_id: String) {
        viewModelScope.launch {
            try {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Cargando
                repositorioPresupuesto.obtener_presupuestos_activos(usuario_id).collect { presupuestos ->
                    _listaPresupuestos.value = presupuestos
                    _estadoUiPresupuesto.value = EstadoUiPresupuesto.Inactivo
                }
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al cargar presupuestos")
            }
        }
    }

    fun cargar_presupuestos_con_alertas(usuario_id: String) {
        viewModelScope.launch {
            try {
                repositorioPresupuesto.obtener_presupuestos_con_alertas(usuario_id).collect { presupuestos ->
                    _presupuestosAlerta.value = presupuestos
                }
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al cargar alertas")
            }
        }
    }

    fun cargar_presupuestos_excedidos(usuario_id: String) {
        viewModelScope.launch {
            try {
                repositorioPresupuesto.obtener_presupuestos_excedidos(usuario_id).collect { presupuestos ->
                    _presupuestosAlerta.value = presupuestos
                }
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al cargar presupuestos excedidos")
            }
        }
    }

    fun buscar_presupuestos(usuario_id: String, consulta: String) {
        viewModelScope.launch {
            try {
                if (consulta.isBlank()) {
                    cargar_presupuestos_activos(usuario_id)
                    return@launch
                }

                repositorioPresupuesto.buscar_presupuestos(usuario_id, consulta).collect { presupuestos ->
                    _listaPresupuestos.value = presupuestos
                }
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error en búsqueda")
            }
        }
    }

    fun filtrar_presupuestos(
        usuario_id: String,
        categoria_id: String? = null,
        periodo: periodo_presupuesto? = null,
        esta_activo: Boolean? = null
    ) {
        viewModelScope.launch {
            try {
                repositorioPresupuesto.filtrar_presupuestos(usuario_id, categoria_id, periodo, esta_activo)
                    .collect { presupuestos ->
                        _listaPresupuestos.value = presupuestos
                    }
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error en filtrado")
            }
        }
    }

    fun obtener_presupuesto_por_id(presupuesto_id: String) {
        viewModelScope.launch {
            try {
                val presupuesto = repositorioPresupuesto.obtener_presupuesto_por_id(presupuesto_id)
                _presupuestoSeleccionado.value = presupuesto
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al obtener presupuesto")
            }
        }
    }

    fun actualizar_presupuesto(presupuesto: presupuesto_entidad) {
        viewModelScope.launch {
            try {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Cargando
                repositorioPresupuesto.actualizar_presupuesto(presupuesto)
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Exito("Presupuesto actualizado")
                _presupuestoSeleccionado.value = presupuesto
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al actualizar")
            }
        }
    }

    fun reiniciar_presupuesto(presupuesto_id: String) {
        viewModelScope.launch {
            try {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Cargando
                repositorioPresupuesto.reiniciar_presupuesto(presupuesto_id)
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Exito("Presupuesto reiniciado")

                val actualizado = repositorioPresupuesto.obtener_presupuesto_por_id(presupuesto_id)
                _presupuestoSeleccionado.value = actualizado
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al reiniciar")
            }
        }
    }

    fun actualizar_porcentaje_alerta(presupuesto_id: String, porcentaje: Int) {
        if (porcentaje !in 1..100) {
            _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error("Porcentaje debe estar entre 1 y 100")
            return
        }

        viewModelScope.launch {
            try {
                repositorioPresupuesto.actualizar_porcentaje_alerta(presupuesto_id, porcentaje)
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Exito("Alerta actualizada")
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al actualizar alerta")
            }
        }
    }

    fun alternar_presupuesto_activo(presupuesto_id: String, esta_activo: Boolean) {
        viewModelScope.launch {
            try {
                repositorioPresupuesto.alternar_presupuesto_activo(presupuesto_id, esta_activo)
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Exito(
                    if (esta_activo) "Presupuesto activado" else "Presupuesto desactivado"
                )
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al cambiar estado")
            }
        }
    }

    fun eliminar_presupuesto(presupuesto_id: String) {
        viewModelScope.launch {
            try {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Cargando
                repositorioPresupuesto.eliminar_presupuesto_por_id(presupuesto_id)
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Exito("Presupuesto eliminado")
                _presupuestoSeleccionado.value = null
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al eliminar")
            }
        }
    }

    fun cargar_estadisticas_presupuesto(usuario_id: String) {
        viewModelScope.launch {
            try {
                repositorioPresupuesto.obtener_estadisticas_presupuesto(usuario_id).collect { estadisticas ->
                    _estadisticasPresupuesto.value = EstadisticasPresupuesto(
                        total_presupuestos = estadisticas?.total ?: 0,
                        monto_total = estadisticas?.monto_total ?: 0.0,
                        gastado_total = estadisticas?.gastado_total ?: 0.0,
                        monto_restante = estadisticas?.monto_restante ?: 0.0,
                        porcentaje_usado = estadisticas?.porcentaje_usado ?: 0.0
                    )
                }
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al cargar estadísticas")
            }
        }
    }

    fun cargar_presupuestos_advertencia(usuario_id: String, umbral_advertencia: Double = 90.0) {
        viewModelScope.launch {
            try {
                repositorioPresupuesto.obtener_presupuestos_advertencia(usuario_id, umbral_advertencia)
                    .collect { presupuestos ->
                        _presupuestosAlerta.value = presupuestos
                    }
            } catch (e: Exception) {
                _estadoUiPresupuesto.value = EstadoUiPresupuesto.Error(e.message ?: "Error al cargar advertencias")
            }
        }
    }

    fun limpiar_estado() {
        _estadoUiPresupuesto.value = EstadoUiPresupuesto.Inactivo
        _presupuestoSeleccionado.value = null
    }
}

sealed class EstadoUiPresupuesto {
    object Inactivo : EstadoUiPresupuesto()
    object Cargando : EstadoUiPresupuesto()
    data class Exito(val mensaje: String) : EstadoUiPresupuesto()
    data class Error(val mensaje: String) : EstadoUiPresupuesto()
}

data class EstadisticasPresupuesto(
    val total_presupuestos: Int = 0,
    val monto_total: Double = 0.0,
    val gastado_total: Double = 0.0,
    val monto_restante: Double = 0.0,
    val porcentaje_usado: Double = 0.0
) {
    val presupuestos_con_alerta: Int
        get() = if (porcentaje_usado >= 80) 1 else 0

    val esta_saludable: Boolean
        get() = porcentaje_usado < 80
}
