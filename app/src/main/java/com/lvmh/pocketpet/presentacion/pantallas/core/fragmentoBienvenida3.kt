package com.lvmh.pocketpet.presentacion.pantallas.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lvmh.pocketpet.databinding.Fragmentobienvenida3Binding

class fragmentoBienvenida3 : Fragment() {

    private var _vincular: Fragmentobienvenida3Binding? = null
    private val vincular get() = _vincular!!

    override fun onCreateView(
        inflador: LayoutInflater,
        contenedor: ViewGroup?,
        estadoGuardado: Bundle?
    ): View {
        _vincular = Fragmentobienvenida3Binding.inflate(inflador, contenedor, false)
        return vincular.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vincular= null
    }
}