package com.lvmh.pocketpet.presentacion.pantallas.vistamodelo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvmh.pocketpet.dominio.repositorios.RecordatorioRepositorio
import com.lvmh.pocketpet.dominio.repositorios.PresupuestoRepositorio
import com.lvmh.pocketpet.dominio.repositorios.LogroRepositorio
import kotlinx.coroutines.launch

class mainvermodelo(
    private val recordatorioRepo: RecordatorioRepositorio,
    private val presupuestoRepo: PresupuestoRepositorio,
    private val logroRepo: LogroRepositorio
) : ViewModel() {

    // Nombre del usuario
    private val _nombreUsuario = MutableLiveData<String>()
    val nombreUsuario: LiveData<String> = _nombreUsuario

    // Total de alertas
    private val _cantidadAlertas = MutableLiveData<Int>()
    val cantidadAlertas: LiveData<Int> = _cantidadAlertas

    // Tipos de alertas
    private val _hayRecordatorios = MutableLiveData<Boolean>()
    val hayRecordatorios: LiveData<Boolean> = _hayRecordatorios

    private val _hayPresupuestosExcedidos = MutableLiveData<Boolean>()
    val hayPresupuestosExcedidos: LiveData<Boolean> = _hayPresupuestosExcedidos

    private val _hayRetosCompletados = MutableLiveData<Boolean>()
    val hayRetosCompletados: LiveData<Boolean> = _hayRetosCompletados

    // Carga el perfil del usuario
    fun cargarPerfilUsuario() {
        viewModelScope.launch {
            _nombreUsuario.value = "Usuario"
        }
    }

    // Carga todas las alertas
    fun cargarAlertas() {
        viewModelScope.launch {
            var total = 0

            // Verificar recordatorios
            val recordatorios = recordatorioRepo.obtenerRecordatoriosProximos()
            val hayRec = recordatorios.isNotEmpty()
            _hayRecordatorios.value = hayRec
            if (hayRec) total += recordatorios.size

            // Verificar presupuestos excedidos
            val presupuestos = presupuestoRepo.obtenerPresupuestosExcedidos()
            val hayPres = presupuestos.isNotEmpty()
            _hayPresupuestosExcedidos.value = hayPres
            if (hayPres) total += presupuestos.size

            // Verificar logros completados
            val retos = logroRepo.obtenerLogrosCompletados()
            val hayRetos = retos.isNotEmpty()
            _hayRetosCompletados.value = hayRetos
            if (hayRetos) total += retos.size

            _cantidadAlertas.value = total
        }
    }

    // Cambia el nombre del usuario
    fun actualizarNombre(nombre: String) {
        _nombreUsuario.value = nombre
    }
}