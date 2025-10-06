package com.lvmh.pocketpet.presentacion.adaptadores

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lvmh.pocketpet.presentacion.pantallas.core.fragmentoBienvenida1
import com.lvmh.pocketpet.presentacion.pantallas.core.fragmentoBienvenida2
import com.lvmh.pocketpet.presentacion.pantallas.core.fragmentoBienvenida3

class adaptadorBienvenida (actividad: FragmentActivity) : FragmentStateAdapter(actividad) {

    override fun getItemCount(): Int = 3 // 3 diapositivas

    override fun createFragment(posicion: Int): Fragment {
        return when (posicion) {
            0 -> fragmentoBienvenida1()
            1 -> fragmentoBienvenida2()
            2 -> fragmentoBienvenida3()
            else -> fragmentoBienvenida1()
        }
    }
}