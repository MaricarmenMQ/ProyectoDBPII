package com.lvmh.pocketpet.presentacion.pantallas.vistamodelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvmh.pocketpet.data.local.repositorio.transaccionRepositorio
import com.lvmh.pocketpet.data.local.repositorio.cuentaRepositorio
import com.lvmh.pocketpet.data.local.repositorio.presupuestoRepositorio
import com.lvmh.pocketpet.data.local.repositorio.metaRepositorio
import com.lvmh.pocketpet.data.local.repositorio.mascotaRepositorio
import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import com.lvmh.pocketpet.data.local.entidades.cuenta_entidad
import com.lvmh.pocketpet.data.local.entidades.mascota_entidad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transaccion_repositorio: transaccionRepositorio,
    private val cuenta_repositorio: cuentaRepositorio,
    private val presupuesto_repositorio: presupuestoRepositorio,
    private val meta_repositorio: metaRepositorio,
    private val mascota_repositorio: mascotaRepositorio
) : ViewModel() {

    private val _estado_ui = MutableStateFlow<EstadoUiMain>(EstadoUiMain.Cargando)
    val estado_ui: StateFlow<EstadoUiMain> = _estado_ui.asStateFlow()

    // Usuario
    private val _nombre_usuario = MutableStateFlow("")
    val nombre_usuario: StateFlow<String> = _nombre_usuario.asStateFlow()

    // Balance y totales
    private val _balance_total = MutableStateFlow(0.0)
    val balance_total: StateFlow<Double> = _balance_total.asStateFlow()

    private val _ingresos_mensuales = MutableStateFlow(0.0)
    val ingresos_mensuales: StateFlow<Double> = _ingresos_mensuales.asStateFlow()

    private val _gastos_mensuales = MutableStateFlow(0.0)
    val gastos_mensuales: StateFlow<Double> = _gastos_mensuales.asStateFlow()

    private val _ahorro_mensual = MutableStateFlow(0.0)
    val ahorro_mensual: StateFlow<Double> = _ahorro_mensual.asStateFlow()

    // Transacciones recientes
    private val _transacciones_recientes = MutableStateFlow<List<transaccion_entidad>>(emptyList())
    val transacciones_recientes: StateFlow<List<transaccion_entidad>> = _transacciones_recientes.asStateFlow()

    // Cuentas
    private val _cuentas = MutableStateFlow<List<cuenta_entidad>>(emptyList())
    val cuentas: StateFlow<List<cuenta_entidad>> = _cuentas.asStateFlow()

    // Mascota
    private val _mascota = MutableStateFlow<mascota_entidad?>(null)
    val mascota: StateFlow<mascota_entidad?> = _mascota.asStateFlow()

    // Alertas
    private val _cantidad_alertas = MutableStateFlow(0)
    val cantidad_alertas: StateFlow<Int> = _cantidad_alertas.asStateFlow()

    private val _hay_presupuestos_excedidos = MutableStateFlow(false)
    val hay_presupuestos_excedidos: StateFlow<Boolean> = _hay_presupuestos_excedidos.asStateFlow()

    init {
        cargar_datos_iniciales()
    }

    fun cargar_datos_iniciales(usuario_id: String = "usuario_default") {
        viewModelScope.launch {
            try {
                _estado_ui.value = EstadoUiMain.Cargando

                // Cargar datos en paralelo
                launch { cargar_balance_total(usuario_id) }
                launch { cargar_datos_mes_actual(usuario_id) }
                launch { cargar_transacciones_recientes() }
                launch { cargar_cuentas(usuario_id) }
                launch { cargar_mascota() }
                launch { cargar_alertas(usuario_id) }

                _estado_ui.value = EstadoUiMain.Exito

            } catch (e: Exception) {
                _estado_ui.value = EstadoUiMain.Error(e.message ?: "Error al cargar datos")
            }
        }
    }

    private suspend fun cargar_balance_total(usuario_id: String) {
        try {
            cuenta_repositorio.obtener_balance_total_flow(usuario_id).collect { balance ->
                _balance_total.value = balance ?: 0.0
            }
        } catch (e: Exception) {
            _balance_total.value = 0.0
        }
    }

    private suspend fun cargar_datos_mes_actual(usuario_id: String) {
        val calendario = java.util.Calendar.getInstance()

        calendario.set(java.util.Calendar.DAY_OF_MONTH, 1)
        calendario.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendario.set(java.util.Calendar.MINUTE, 0)
        calendario.set(java.util.Calendar.SECOND, 0)
        val inicio_mes = calendario.timeInMillis

        calendario.set(java.util.Calendar.DAY_OF_MONTH, calendario.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
        calendario.set(java.util.Calendar.HOUR_OF_DAY, 23)
        calendario.set(java.util.Calendar.MINUTE, 59)
        calendario.set(java.util.Calendar.SECOND, 59)
        val fin_mes = calendario.timeInMillis

        try {
            transaccion_repositorio.obtener_total_ingresos(inicio_mes, fin_mes).collect { ingresos ->
                _ingresos_mensuales.value = ingresos ?: 0.0
            }

            transaccion_repositorio.obtener_total_gastos(inicio_mes, fin_mes).collect { gastos ->
                _gastos_mensuales.value = gastos ?: 0.0
            }

            _ahorro_mensual.value = _ingresos_mensuales.value - _gastos_mensuales.value

        } catch (e: Exception) {
            _ingresos_mensuales.value = 0.0
            _gastos_mensuales.value = 0.0
            _ahorro_mensual.value = 0.0
        }
    }

    private suspend fun cargar_transacciones_recientes() {
        try {
            transaccion_repositorio.obtener_transacciones_recientes().collect { transacciones ->
                _transacciones_recientes.value = transacciones
            }
        } catch (e: Exception) {
            _transacciones_recientes.value = emptyList()
        }
    }

    private suspend fun cargar_cuentas(usuario_id: String) {
        try {
            cuenta_repositorio.obtener_cuentas_activas(usuario_id).collect { cuentas ->
                _cuentas.value = cuentas
            }
        } catch (e: Exception) {
            _cuentas.value = emptyList()
        }
    }

    private suspend fun cargar_mascota() {
        try {
            mascota_repositorio.obtener_mascota().collect { mascota ->
                _mascota.value = mascota
            }
        } catch (e: Exception) {
            _mascota.value = null
        }
    }

    private suspend fun cargar_alertas(usuario_id: String) {
        try {
            var total_alertas = 0

            // Presupuestos excedidos
            val presupuestos_excedidos = presupuesto_repositorio.obtener_cantidad_presupuestos_excedidos(usuario_id)
            total_alertas += presupuestos_excedidos
            _hay_presupuestos_excedidos.value = presupuestos_excedidos > 0

            // Metas urgentes
            meta_repositorio.obtenerMetasUrgentes(usuario_id).collect { metas ->
                total_alertas += metas.size
            }

            _cantidad_alertas.value = total_alertas

        } catch (e: Exception) {
            _cantidad_alertas.value = 0
        }
    }

    fun actualizar_nombre_usuario(nombre: String) {
        _nombre_usuario.value = nombre
    }

    fun refrescar_datos(usuario_id: String = "usuario_default") {
        cargar_datos_iniciales(usuario_id)
    }
}

sealed class EstadoUiMain {
    object Cargando : EstadoUiMain()
    object Exito : EstadoUiMain()
    data class Error(val mensaje: String) : EstadoUiMain()
}