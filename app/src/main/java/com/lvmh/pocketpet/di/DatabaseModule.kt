package com.lvmh.pocketpet.di

import android.content.Context
import androidx.room.Room
import com.lvmh.pocketpet.data.local.database.BaseDatosApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule  {

    @Provides
    @Singleton
    fun proporcionarBaseDeDatosApp(
        @ApplicationContext contexto: Context
    ): BaseDeDatosApp {
        return Room.databaseBuilder(
            contexto,
            BaseDeDatosApp::class.java,
            BaseDeDatosApp.NOMBRE_BASE_DATOS
        )
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun proporcionarTransaccionOad(baseDeDatos: BaseDeDatosApp): TransaccionOad {
        return baseDeDatos.transaccionOad()
    }

    @Provides
    @Singleton
    fun proporcionarCategoriaOad(baseDeDatos: BaseDeDatosApp): CategoriaOad {
        return baseDeDatos.categoriaOad()
    }

    @Provides
    @Singleton
    fun proporcionarCuentaOad(baseDeDatos: BaseDeDatosApp): CuentaOad {
        return baseDeDatos.cuentaOad()
    }

    @Provides
    @Singleton
    fun proporcionarPresupuestoOad(baseDeDatos: BaseDeDatosApp): PresupuestoOad {
        return baseDeDatos.presupuestoOad()
    }

    @Provides
    @Singleton
    fun proporcionarMetaOad(baseDeDatos: BaseDeDatosApp): MetaOad {
        return baseDeDatos.metaOad()
    }

    @Provides
    @Singleton
    fun proporcionarMascotaOad(baseDeDatos: BaseDeDatosApp): MascotaOad {
        return baseDeDatos.mascotaOad()
    }

    @Provides
    @Singleton
    fun proporcionarLogroOad(baseDeDatos: BaseDeDatosApp): LogroOad {
        return baseDeDatos.logroOad()
    }
}
