package com.lvmh.pocketpet.presentacion.pantallas.vistamodelo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvmh.pocketpet.dominio.modelos.Transaccion
import com.lvmh.pocketpet.dominio.repositorios.TransaccionRepositorio
import com.lvmh.pocketpet.dominio.usecases.transacciones.ObtenerTransaccionesCasoUso
import kotlinx.coroutines.launch
import java.util.Calendar

class transaccionvermodelo (
    private val transaccionRepo: TransaccionRepositorio,
    private val obtenerTransaccionesCaso: ObtenerTransaccionesCasoUso
) : ViewModel() {

    // Balance total
    private val _balanceTotal = MutableLiveData<Double>()
    val balanceTotal: LiveData<Double> = _balanceTotal

    // Ingresos del mes
    private val _ingresosMensuales = MutableLiveData<Double>()
    val ingresosMensuales: LiveData<Double> = _ingresosMensuales

    // Gastos del mes
    private val _gastosMensuales = MutableLiveData<Double>()
    val gastosMensuales: LiveData<Double> = _gastosMensuales

    // Ahorro (ingresos - gastos)
    private val _ahorroMensual = MutableLiveData<Double>()
    val ahorroMensual: LiveData<Double> = _ahorroMensual

    // Últimas transacciones
    private val _transaccionesRecientes = MutableLiveData<List<Transaccion>>()
    val transaccionesRecientes: LiveData<List<Transaccion>> = _transaccionesRecientes

    // Estado de carga
    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    // Carga ingresos, gastos y balance del mes
    fun cargarDatosMensuales() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                val calendario = Calendar.getInstance()
                val año = calendario.get(Calendar.YEAR)
                val mes = calendario.get(Calendar.MONTH)

                calendario.set(año, mes, 1, 0, 0, 0)
                val inicioMes = calendario.time

                calendario.set(año, mes, calendario.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
                val finMes = calendario.time

                // Obtener transacciones del mes
                val transacciones = transaccionRepo.obtenerPorRangoDeFecha(inicioMes, finMes)

                val ingresos = transacciones
                    .filter { it.tipo.equals("income", true) }
                    .sumOf { it.monto }

                val gastos = transacciones
                    .filter { it.tipo.equals("expense", true) }
                    .sumOf { it.monto }

                val ahorro = ingresos - gastos

                _ingresosMensuales.value = ingresos
                _gastosMensuales.value = gastos
                _ahorroMensual.value = ahorro

                calcularBalanceTotal()
            } catch (e: Exception) {
                _ingresosMensuales.value = 0.0
                _gastosMensuales.value = 0.0
                _ahorroMensual.value = 0.0
            } finally {
                _cargando.value = false
            }
        }
    }

    // Calcula el balance total (todos los movimientos)
    private suspend fun calcularBalanceTotal() {
        try {
            val transacciones = transaccionRepo.obtenerTodas()

            val ingresos = transacciones
                .filter { it.tipo.equals("income", true) }
                .sumOf { it.monto }

            val gastos = transacciones
                .filter { it.tipo.equals("expense", true) }
                .sumOf { it.monto }

            _balanceTotal.value = ingresos - gastos
        } catch (e: Exception) {
            _balanceTotal.value = 0.0
        }
    }

    // Carga las últimas 5 transacciones
    fun cargarTransaccionesRecientes() {
        viewModelScope.launch {
            try {
                val lista = transaccionRepo.obtenerRecientes(5)
                _transaccionesRecientes.value = lista
            } catch (e: Exception) {
                _transaccionesRecientes.value = emptyList()
            }
        }
    }

    // Busca una transacción por su ID
    suspend fun obtenerTransaccionPorId(id: Long): Transaccion? {
        return try {
            transaccionRepo.obtenerPorId(id)
        } catch (e: Exception) {
            null
        }
    }

    // Refresca todos los datos
    fun refrescarDatos() {
        cargarDatosMensuales()
        cargarTransaccionesRecientes()
    }
}
