package com.lvmh.pocketpet.datos.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lvmh.pocketpet.datos.local.entidades.categoriaentidad
import com.lvmh.pocketpet.datos.local.entidades.estadomascotaentidad
import com.lvmh.pocketpet.datos.local.entidades.montoentidad
import com.lvmh.pocketpet.datos.local.entidades.transaccionesentidades
import com.lvmh.pocketpet.datos.local.oad.transaccionOad
import com.lvmh.pocketpet.datos.local.oad.categoriaOad
import com.lvmh.pocketpet.datos.local.oad.montoOad
import com.lvmh.pocketpet.datos.local.oad.mascotaOad


@Database(
    entities = [
        transaccionesentidades::class,
        categoriaentidad::class,
        montoentidad::class,
        estadomascotaentidad::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transaccionOad(): transaccionOad
    abstract fun categoriaDao(): categoriaOad
    abstract fun montoOad(): montoOad
    abstract fun mascotaOad(): mascotaOad
}