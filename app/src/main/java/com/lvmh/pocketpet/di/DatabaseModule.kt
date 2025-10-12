package com.lvmh.pocketpet.di

import com.lvmh.pocketpet.data.local.database.AppDatabase
import com.lvmh.pocketpet.data.local.oad.categoriaOad
import com.lvmh.pocketpet.data.local.oad.cuentaOad
import com.lvmh.pocketpet.data.local.oad.presupuestoOad
import com.lvmh.pocketpet.data.local.oad.metaOad
import com.lvmh.pocketpet.data.local.oad.mascotaOad
import com.lvmh.pocketpet.data.local.oad.logroOad
import com.lvmh.pocketpet.data.local.oad.transaccionOad

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun proporcionarBaseDeDatosApp(
        @ApplicationContext contexto: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            contexto,
            AppDatabase::class.java,
            AppDatabase.NOMBRE_BASE_DATOS
        )
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun proporcionarTransaccionOad(baseDeDatos: AppDatabase): transaccionOad {
        return baseDeDatos.transaccionOad()
    }

    @Provides
    @Singleton
    fun proporcionarCategoriaOad(baseDeDatos: AppDatabase): categoriaOad {
        return baseDeDatos.categoriaOad()
    }

    @Provides
    @Singleton
    fun proporcionarCuentaOad(baseDeDatos: AppDatabase): cuentaOad {
        return baseDeDatos.cuentaOad()
    }

    @Provides
    @Singleton
    fun proporcionarPresupuestoOad(baseDeDatos: AppDatabase): presupuestoOad {
        return baseDeDatos.presupuestoOad()
    }

    @Provides
    @Singleton
    fun proporcionarMetaOad(baseDeDatos: AppDatabase): metaOad {
        return baseDeDatos.metaOad()
    }

    @Provides
    @Singleton
    fun proporcionarMascotaOad(baseDeDatos: AppDatabase): mascotaOad {
        return baseDeDatos.mascotaOad()
    }

    @Provides
    @Singleton
    fun proporcionarLogroOad(baseDeDatos: AppDatabase): logroOad {
        return baseDeDatos.logroOad()
    }
}