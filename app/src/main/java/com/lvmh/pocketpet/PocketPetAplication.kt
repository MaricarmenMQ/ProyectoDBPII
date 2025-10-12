package com.lvmh.pocketpet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PocketPetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicialización de la aplicación
    }
}