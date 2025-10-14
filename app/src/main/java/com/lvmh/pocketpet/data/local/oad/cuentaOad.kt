package com.lvmh.pocketpet.data.local.oad

import androidx.room.*
import com.lvmh.pocketpet.data.local.entidades.cuenta_entidad
import kotlinx.coroutines.flow.Flow

@Dao
interface cuentaOad {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(cuenta: cuenta_entidad)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar_todas(cuentas: List<cuenta_entidad>)

    @Update
    suspend fun actualizar(cuenta: cuenta_entidad)

    @Delete
    suspend fun eliminar(cuenta: cuenta_entidad)

    @Query("DELETE FROM cuentas WHERE id = :cuenta_id")
    suspend fun eliminar_por_id(cuenta_id: String)

    @Query("SELECT * FROM cuentas WHERE id = :cuenta_id")
    suspend fun obtener_por_id(cuenta_id: String): cuenta_entidad?

    @Query("SELECT * FROM cuentas WHERE usuario_id = :usuario_id ORDER BY es_principal DESC, nombre ASC")
    fun obtener_todas_por_usuario(usuario_id: String): Flow<List<cuenta_entidad>>

    @Query("SELECT * FROM cuentas WHERE usuario_id = :usuario_id AND esta_activa = 1 ORDER BY es_principal DESC, nombre ASC")
    fun obtener_cuentas_activas(usuario_id: String): Flow<List<cuenta_entidad>>

    @Query("SELECT * FROM cuentas WHERE usuario_id = :usuario_id AND tipo_cuenta = :tipo ORDER BY nombre ASC")
    fun obtener_por_tipo(usuario_id: String, tipo: String): Flow<List<cuenta_entidad>>

    @Query("SELECT * FROM cuentas WHERE usuario_id = :usuario_id AND es_principal = 1 LIMIT 1")
    suspend fun obtener_cuenta_principal(usuario_id: String): cuenta_entidad?

    @Query("SELECT SUM(balance) FROM cuentas WHERE usuario_id = :usuario_id AND incluir_en_balance_total = 1 AND esta_activa = 1")
    fun obtener_balance_total(usuario_id: String): Flow<Double?>

    @Query("""
        UPDATE cuentas 
        SET balance = balance + :monto,
            actualizado_en = :actualizado_en
        WHERE id = :cuenta_id
    """)
    suspend fun incrementar_balance(cuenta_id: String, monto: Double, actualizado_en: Long = System.currentTimeMillis())

    @Query("""
        UPDATE cuentas 
        SET balance = balance - :monto,
            actualizado_en = :actualizado_en
        WHERE id = :cuenta_id
    """)
    suspend fun decrementar_balance(cuenta_id: String, monto: Double, actualizado_en: Long = System.currentTimeMillis())

    @Query("""
        UPDATE cuentas 
        SET balance = :nuevo_balance,
            actualizado_en = :actualizado_en
        WHERE id = :cuenta_id
    """)
    suspend fun actualizar_balance(cuenta_id: String, nuevo_balance: Double, actualizado_en: Long = System.currentTimeMillis())

    @Query("""
        UPDATE cuentas 
        SET esta_activa = :esta_activa,
            actualizado_en = :actualizado_en
        WHERE id = :cuenta_id
    """)
    suspend fun actualizar_estado(cuenta_id: String, esta_activa: Boolean, actualizado_en: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM cuentas WHERE usuario_id = :usuario_id")
    suspend fun contar_cuentas(usuario_id: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM cuentas WHERE usuario_id = :usuario_id AND nombre = :nombre)")
    suspend fun existe_cuenta_con_nombre(usuario_id: String, nombre: String): Boolean
}