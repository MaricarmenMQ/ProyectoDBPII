package com.lvmh.pocketpet.di

import com.lvmh.pocketpet.data.local.database.BaseDatosApp
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
object DatabaseModule  {

    @Provides
    @Singleton
    fun proporcionarBaseDeDatosApp(
        @ApplicationContext contexto: Context
    ): BaseDatosApp  {
        return Room.databaseBuilder(
            contexto,
            BaseDatosApp ::class.java,
            BaseDatosApp .NOMBRE_BASE_DATOS
        )
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun proporcionarTransaccionOad(baseDeDatos: BaseDatosApp ):  transaccionOad {
        return baseDeDatos.transaccionOad()
    }

    @Provides
    @Singleton
    fun proporcionarCategoriaOad(baseDeDatos: BaseDatosApp ):categoriaOad {
        return baseDeDatos.categoriaOad()
    }

    @Provides
    @Singleton
    fun proporcionarCuentaOad(baseDeDatos: BaseDatosApp ): cuentaOad {
        return baseDeDatos.cuentaOad()
    }

    @Provides
    @Singleton
    fun proporcionarPresupuestoOad(baseDeDatos: BaseDatosApp ): presupuestoOad {
        return baseDeDatos.presupuestoOad()
    }

    @Provides
    @Singleton
    fun proporcionarMetaOad(baseDeDatos: BaseDatosApp ): metaOad {
        return baseDeDatos.metaOad()
    }

    @Provides
    @Singleton
    fun proporcionarMascotaOad(baseDeDatos: BaseDatosApp): mascotaOad {
        return baseDeDatos.mascotaOad()
    }

    @Provides
    @Singleton
    fun proporcionarLogroOad(baseDeDatos: BaseDatosApp): logroOad {
        return baseDeDatos.logroOad()
    }
}
