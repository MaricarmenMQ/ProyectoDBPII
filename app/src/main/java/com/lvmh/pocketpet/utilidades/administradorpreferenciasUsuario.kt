package com.lvmh.pocketpet.utilidades
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias_usuario")

class administradorpreferenciasUsuario(private val contexto: Context) {
    private val dataStore = contexto.dataStore

    companion object {
        private val BIENVENIDA_COMPLETADA = booleanPreferencesKey("bienvenida_completada")
        private val NOMBRE_USUARIO = stringPreferencesKey("nombre_usuario")
        private val MONEDA = stringPreferencesKey("moneda")
        private val DIA_INICIO_MES = intPreferencesKey("dia_inicio_mes")
        private val NOTIFICACIONES_ACTIVADAS = booleanPreferencesKey("notificaciones_activadas")
    }
    val bienvenidaCompletada: Flow<Boolean> = contexto.dataStore.data
        .map { preferencias ->
            preferencias[BIENVENIDA_COMPLETADA] ?: false
        }
    suspend fun establecerBienvenidaCompletada(completada: Boolean) {
        dataStore.edit { it[BIENVENIDA_COMPLETADA] = completada }
    }

    fun bienvenidaCompletada(): Flow<Boolean> {
        return dataStore.data.map { it[BIENVENIDA_COMPLETADA] ?: false }
    }

    suspend fun guardarConfiguracionUsuario(nombre: String, moneda: String, diaInicio: Int, notificaciones: Boolean) {
        dataStore.edit {
            it[NOMBRE_USUARIO] = nombre
            it[MONEDA] = moneda
            it[DIA_INICIO_MES] = diaInicio
            it[NOTIFICACIONES_ACTIVADAS] = notificaciones
        }
    }

    // Obtener nombre del usuario
    fun obtenerNombreUsuario(): Flow<String> {
        return dataStore.data.map { it[NOMBRE_USUARIO] ?: "" }
    }

}