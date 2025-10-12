package com.lvmh.pocketpet.di

import com.lvmh.pocketpet.data.local.oad.categoriaOad
import com.lvmh.pocketpet.data.local.oad.cuentaOad
import com.lvmh.pocketpet.data.local.oad.logroOad
import com.lvmh.pocketpet.data.local.oad.mascotaOad
import com.lvmh.pocketpet.data.local.oad.metaOad
import com.lvmh.pocketpet.data.local.oad.presupuestoOad
import com.lvmh.pocketpet.data.local.oad.transaccionOad
import com.lvmh.pocketpet.data.local.repositorio.logroRepositorio
import com.lvmh.pocketpet.data.local.repositorio.cuentaRepositorio
import com.lvmh.pocketpet.data.local.repositorio.mascotaRepositorio
import com.lvmh.pocketpet.data.local.repositorio.categoriaRepositorio
import com.lvmh.pocketpet.data.local.repositorio.metaRepositorio
import com.lvmh.pocketpet.data.local.repositorio.presupuestoRepositorio
import com.lvmh.pocketpet.data.local.repositorio.transaccionRepositorio
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositorioModule {

    @Provides
    @Singleton
    fun proporcionarRepositorioTransaccion(
        transaccionOad: transaccionOad
    ): transaccionRepositorio {
        return transaccionRepositorio(transaccionOad)
    }

    @Provides
    @Singleton
    fun proporcionarRepositorioCategoria(
        categoriaOad: categoriaOad
    ): categoriaRepositorio {
        return categoriaRepositorio(categoriaOad)
    }

    @Provides
    @Singleton
    fun proporcionarRepositorioCuenta(
        cuentaOad: cuentaOad
    ): cuentaRepositorio {
        return cuentaRepositorio(cuentaOad)
    }

    @Provides
    @Singleton
    fun proporcionarRepositorioPresupuesto(
        presupuestoOad: presupuestoOad
    ): presupuestoRepositorio {
        return presupuestoRepositorio(presupuestoOad)
    }

    @Provides
    @Singleton
    fun proporcionarRepositorioMeta(
        metaOad: metaOad
    ): metaRepositorio {
        return metaRepositorio(metaOad)
    }

    @Provides
    @Singleton
    fun proporcionarRepositorioMascota(
        mascotaOad: mascotaOad
    ): mascotaRepositorio {
        return mascotaRepositorio(mascotaOad)
    }

    @Provides
    @Singleton
    fun proporcionarRepositorioLogro(
        logroOad: logroOad
    ): logroRepositorio {
        return logroRepositorio(logroOad)
    }
}
