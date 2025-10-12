package com.lvmh.pocketpet.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters

import com.lvmh.pocketpet.data.local.entidades.logro_entidad
import com.lvmh.pocketpet.data.local.entidades.mascota_entidad
import com.lvmh.pocketpet.data.local.entidades.meta_entidad
import com.lvmh.pocketpet.data.local.entidades.presupuesto_entidad
import com.lvmh.pocketpet.data.local.entidades.transaccion_entidad
import com.lvmh.pocketpet.data.local.entidades.categoria_entidad
import com.lvmh.pocketpet.data.local.entidades.cuenta_entidad

import com.lvmh.pocketpet.data.local.oad.mascotaOad
import com.lvmh.pocketpet.data.local.oad.transaccionOad
import com.lvmh.pocketpet.data.local.oad.categoriaOad
import com.lvmh.pocketpet.data.local.oad.metaOad
import com.lvmh.pocketpet.data.local.oad.presupuestoOad
import com.lvmh.pocketpet.data.local.oad.cuentaOad
import com.lvmh.pocketpet.data.local.oad.logroOad

@Database(
    entidades = [
        transaccion_entidad::class,
        categoria_entidad::class,
        cuenta_entidad::class,
        presupuesto_entidad::class,
        meta_entidad::class,
        mascota_entidad::class,
        logro_entidad::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun transaccionOad(): transaccionOad
    abstract fun categoriaOad(): categoriaOad
    abstract fun cuentaOad(): cuentaOad
    abstract fun presupuestoOad(): presupuestoOad
    abstract fun metaOad(): metaOad
    abstract fun mascotaOad(): mascotaOad
    abstract fun logroOad(): logroOad

    companion object {
        @Volatile
        private var instancia: AppDataBase? = null
        private val BLOQUEO = Any()

        const val NOMBRE_BASE_DATOS = "PocketPet_database"

        fun obtenerInstancia(context: Context): AppDataBase {
            return instancia ?: synchronized(BLOQUEO) {
                instancia ?: crearBaseDatos(context).also { instancia = it }
            }
        }

        private fun crearBaseDatos(context: Context): AppDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                NOMBRE_BASE_DATOS
            )
                .addTypeConverter(RoomTypeConverter())
                .enableMultiInstanceInvalidation()
                .fallbackToDestructiveMigration()
                .build()
        }

        fun reiniciarBaseDatos(context: Context) {
            synchronized(BLOQUEO) {
                instancia?.close()
                instancia = null
                context.deleteDatabase(NOMBRE_BASE_DATOS)
            }
        }
    }
}
