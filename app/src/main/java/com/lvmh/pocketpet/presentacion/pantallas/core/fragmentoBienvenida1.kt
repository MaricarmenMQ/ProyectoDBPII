package com.lvmh.pocketpet.presentacion.pantallas.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lvmh.pocketpet.databinding.Fragmentobienvenida1Binding

class fragmentoBienvenida1 : Fragment() {

    private var _vincular: Fragmentobienvenida1Binding? = null
    private val vincular get() = _vincular!!

    override fun onCreateView(
        inflador: LayoutInflater,
        contenedor: ViewGroup?,
        estadoGuardado: Bundle?
    ): View {
        _vincular = Fragmentobienvenida1Binding.inflate(inflador, contenedor, false)
        return vincular.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vincular = null
    }
}