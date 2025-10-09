package com.lvmh.pocketpet.data.local.oad

import androidx.room.*
import com.lvmh.pocketpet.data.local.entidades.mascota_entidad
import kotlinx.coroutines.flow.Flow


@Dao
interface mascotaOad {

    @Query("SELECT * FROM mascota WHERE id = 1 LIMIT 1")
    fun obtener_mascota(): Flow<mascota_entidad?>

    @Query("SELECT * FROM mascota WHERE id = 1 LIMIT 1")
    suspend fun obtener_mascota_sincrona(): mascota_entidad?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar_mascota(mascota: mascota_entidad)


    @Update
    suspend fun actualizar_mascota(mascota: mascota_entidad)

    @Query("UPDATE mascota SET salud = :nueva_salud, coincidencia_animacion = :estado, ultima_actualizacion = :timestamp WHERE id = 1")
    suspend fun actualizar_salud(nueva_salud: Int, estado: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE mascota SET nivel = :nuevo_nivel, ultima_actualizacion = :timestamp WHERE id = 1")
    suspend fun actualizar_nivel(nuevo_nivel: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE mascota SET nombre = :nuevo_nombre, ultima_actualizacion = :timestamp WHERE id = 1")
    suspend fun actualizar_nombre(nuevo_nombre: String, timestamp: Long = System.currentTimeMillis())

    @Query("""
        UPDATE mascota 
        SET salud = CASE 
            WHEN salud + :incremento > 100 THEN 100 
            ELSE salud + :incremento 
        END,
        ultima_actualizacion = :timestamp 
        WHERE id = 1
    """)
    suspend fun incrementar_salud(incremento: Int, timestamp: Long = System.currentTimeMillis())


    @Query("""
        UPDATE mascota 
        SET salud = CASE 
            WHEN salud - :decremento < 0 THEN 0 
            ELSE salud - :decremento 
        END,
        ultima_actualizacion = :timestamp 
        WHERE id = 1
    """)
    suspend fun decrementar_salud(decremento: Int, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT salud FROM mascota WHERE id = 1")
    suspend fun obtener_nivel_salud(): Int?

    @Query("SELECT COUNT(*) FROM mascota WHERE id = 1")
    suspend fun existe_mascota(): Int

    @Query("UPDATE mascota SET salud = 50, nivel = 1, coincidencia_animacion = 'ESTABLE', nombre = 'FinanPet', ultima_actualizacion = :timestamp WHERE id = 1")
    suspend fun reiniciar_mascota(timestamp: Long = System.currentTimeMillis())
}