package com.lvmh.pocketpet.presentacion.pantallas.vistamodelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import com.lvmh.pocketpet.data.local.repositorio.transaccionRepositorio
import com.lvmh.pocketpet.data.local.repositorio.cuentaRepositorio
import com.lvmh.pocketpet.data.local.repositorio.categoriaRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transaccionRepositorio: transaccionRepositorio,
    private val cuentaRepositorio: cuentaRepositorio,
    private val categoriaRepositorio: categoriaRepositorio
) : ViewModel() {

    private val _estado_ui = MutableStateFlow<EstadoUiTransaccion>(EstadoUiTransaccion.Inactivo)
    val estado_ui: StateFlow<EstadoUiTransaccion> = _estado_ui.asStateFlow()

    private val _lista_transacciones = MutableStateFlow<List<transaccion_entidad>>(emptyList())
    val lista_transacciones: StateFlow<List<transaccion_entidad>> = _lista_transacciones.asStateFlow()

    private val _transacciones_recientes = MutableStateFlow<List<transaccion_entidad>>(emptyList())
    val transacciones_recientes: StateFlow<List<transaccion_entidad>> = _transacciones_recientes.asStateFlow()

    private val _total_ingresos = MutableStateFlow(0.0)
    val total_ingresos: StateFlow<Double> = _total_ingresos.asStateFlow()

    private val _total_gastos = MutableStateFlow(0.0)
    val total_gastos: StateFlow<Double> = _total_gastos.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    fun crear_transaccion(
        tipo: String,
        monto: Double,
        categoria_id: Long,
        cuenta_id: Long,
        descripcion: String,
        fecha: Long = System.currentTimeMillis(),
        imagen_uri: String? = null
    ) {
        if (monto <= 0) {
            _estado_ui.value = EstadoUiTransaccion.Error("El monto debe ser mayor a 0")
            return
        }

        if (descripcion.isBlank()) {
            _estado_ui.value = EstadoUiTransaccion.Error("La descripción es requerida")
            return
        }

        viewModelScope.launch {
            try {
                _estado_ui.value = EstadoUiTransaccion.Cargando

                val transaccion = transaccion_entidad(
                    tipo = tipo,
                    monto = monto,
                    id_categoria = categoria_id.toString(),
                    id_cuenta = cuenta_id.toString(),
                    descripcion = descripcion,
                    fecha = fecha,
                    imagenUri = imagen_uri
                )

                val transaccion_id = transaccionRepositorio.crear_transaccion(transaccion)

                // Actualizar balance de cuenta
                when (tipo.lowercase()) {
                    "ingreso" -> cuentaRepositorio.incrementar_balance(cuenta_id.toString(), monto)
                    "gasto" -> cuentaRepositorio.decrementar_balance(cuenta_id.toString(), monto)
                }

                _estado_ui.value = EstadoUiTransaccion.Exito("Transacción registrada exitosamente")

            } catch (e: Exception) {
                _estado_ui.value = EstadoUiTransaccion.Error(e.message ?: "Error al crear transacción")
            }
        }
    }

    fun cargar_transacciones_recientes() {
        viewModelScope.launch {
            try {
                transaccionRepositorio.obtener_transacciones_recientes().collect { transacciones ->
                    _transacciones_recientes.value = transacciones
                }
            } catch (e: Exception) {
                _estado_ui.value = EstadoUiTransaccion.Error(e.message ?: "Error al cargar transacciones")
            }
        }
    }

    fun cargar_transacciones_por_rango(fecha_inicio: Long, fecha_fin: Long) {
        viewModelScope.launch {
            try {
                _estado_ui.value = EstadoUiTransaccion.Cargando

                transaccionRepositorio.obtener_transacciones_por_rango_fechas(fecha_inicio, fecha_fin)
                    .collect { transacciones ->
                        _lista_transacciones.value = transacciones
                        _estado_ui.value = EstadoUiTransaccion.Inactivo
                    }
            } catch (e: Exception) {
                _estado_ui.value = EstadoUiTransaccion.Error(e.message ?: "Error al cargar transacciones")
            }
        }
    }

    fun cargar_totales_periodo(fecha_inicio: Long, fecha_fin: Long) {
        viewModelScope.launch {
            try {
                transaccionRepositorio.obtener_total_ingresos(fecha_inicio, fecha_fin).collect { ingresos ->
                    _total_ingresos.value = ingresos ?: 0.0
                }

                transaccionRepositorio.obtener_total_gastos(fecha_inicio, fecha_fin).collect { gastos ->
                    _total_gastos.value = gastos ?: 0.0
                }

                _balance.value = _total_ingresos.value - _total_gastos.value

            } catch (e: Exception) {
                _estado_ui.value = EstadoUiTransaccion.Error(e.message ?: "Error al cargar totales")
            }
        }
    }

    fun cargar_datos_mes_actual() {
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

        cargar_transacciones_por_rango(inicio_mes, fin_mes)
        cargar_totales_periodo(inicio_mes, fin_mes)
    }

    fun limpiar_estado() {
        _estado_ui.value = EstadoUiTransaccion.Inactivo
    }
}

sealed class EstadoUiTransaccion {
    object Inactivo : EstadoUiTransaccion()
    object Cargando : EstadoUiTransaccion()
    data class Exito(val mensaje: String) : EstadoUiTransaccion()
    data class Error(val mensaje: String) : EstadoUiTransaccion()
}