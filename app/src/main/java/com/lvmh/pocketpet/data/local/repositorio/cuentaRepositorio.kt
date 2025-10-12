package com.lvmh.pocketpet.data.local.repositorio

import com.lvmh.pocketpet.data.local.entidades.cuenta_entidad
import com.lvmh.pocketpet.data.local.oad.cuentaOad
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class cuentaRepositorio @Inject constructor(
    private val cuentaOad: cuentaOad
) {

    suspend fun crear_cuenta(cuenta: cuenta_entidad) {
        cuentaOad.insertar(cuenta)
    }

    suspend fun crear_cuentas_iniciales(cuentas: List<cuenta_entidad>) {
        cuentaOad.insertar_todas(cuentas)
    }

    suspend fun obtener_cuenta_por_id(cuenta_id: String): cuenta_entidad? {
        return cuentaOad.obtener_por_id(cuenta_id)
    }

    fun obtener_todas_cuentas(usuario_id: String): Flow<List<cuenta_entidad>> {
        return cuentaOad.obtener_todas_por_usuario(usuario_id)
    }

    fun obtener_cuentas_activas(usuario_id: String): Flow<List<cuenta_entidad>> {
        return cuentaOad.obtener_cuentas_activas(usuario_id)
    }

    fun obtener_por_tipo(usuario_id: String, tipo: String): Flow<List<cuenta_entidad>> {
        return cuentaOad.obtener_por_tipo(usuario_id, tipo)
    }

    suspend fun obtener_cuenta_principal(usuario_id: String): cuenta_entidad? {
        return cuentaOad.obtener_cuenta_principal(usuario_id)
    }

    fun obtener_balance_total_flow(usuario_id: String): Flow<Double?> {
        return cuentaOad.obtener_balance_total(usuario_id)
    }

    suspend fun actualizar_cuenta(cuenta: cuenta_entidad) {
        cuentaOad.actualizar(cuenta.copy(actualizado_en = System.currentTimeMillis()))
    }

    suspend fun eliminar_cuenta(cuenta: cuenta_entidad) {
        cuentaOad.eliminar(cuenta)
    }

    suspend fun eliminar_cuenta_por_id(cuenta_id: String) {
        cuentaOad.eliminar_por_id(cuenta_id)
    }

    suspend fun incrementar_balance(cuenta_id: String, monto: Double) {
        cuentaOad.incrementar_balance(cuenta_id, monto)
    }

    suspend fun decrementar_balance(cuenta_id: String, monto: Double) {
        cuentaOad.decrementar_balance(cuenta_id, monto)
    }

    suspend fun actualizar_balance(cuenta_id: String, nuevo_balance: Double) {
        cuentaOad.actualizar_balance(cuenta_id, nuevo_balance)
    }

    suspend fun actualizar_estado(cuenta_id: String, esta_activa: Boolean) {
        cuentaOad.actualizar_estado(cuenta_id, esta_activa)
    }

    suspend fun contar_cuentas(usuario_id: String): Int {
        return cuentaOad.contar_cuentas(usuario_id)
    }

    suspend fun existe_cuenta_con_nombre(usuario_id: String, nombre: String): Boolean {
        return cuentaOad.existe_cuenta_con_nombre(usuario_id, nombre)
    }

    suspend fun transferir_entre_cuentas(
        cuenta_origen_id: String,
        cuenta_destino_id: String,
        monto: Double
    ) {
        if (monto <= 0) return

        decrementar_balance(cuenta_origen_id, monto)
        incrementar_balance(cuenta_destino_id, monto)
    }
}