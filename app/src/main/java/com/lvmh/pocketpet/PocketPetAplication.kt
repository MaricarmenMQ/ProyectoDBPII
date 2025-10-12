package com.lvmh.pocketpet

import android.app.Application
import android.util.Log


class PocketPetAplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, " Iniciando PocketPet...")
        inicializarApp()
        Log.d(TAG, " PocketPet iniciado correctamente")
    }

    private fun inicializarApp() {

    }

    companion object {
        private const val TAG = "FinancePetApp"
    }
}