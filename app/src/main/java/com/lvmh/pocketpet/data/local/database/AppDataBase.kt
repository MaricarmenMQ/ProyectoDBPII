package com.lvmh.pocketpet.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lvmh.pocketpet.data.local.oad.transaccionOad


@Database(
    entities = [
        transaccionentidad::class,
        categoria_entidad::class,
        monto_entidad::class,
        estado_mascota_entidad::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transaccionOad(): transaccionOad
    abstract fun categoriaDao(): categoriaOad
    abstract fun montoOad(): montoOad
    abstract fun mascotaOad(): mascotaOad
}