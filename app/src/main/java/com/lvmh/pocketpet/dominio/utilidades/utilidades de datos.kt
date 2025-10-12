package com.lvmh.pocketpet.dominio.utilidades

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val formatoFechaCompleta = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "PE"))
    private val formatoFechaCorta = SimpleDateFormat("dd/MM/yyyy", Locale("es", "PE"))
    private val formatoHora = SimpleDateFormat("HH:mm", Locale("es", "PE"))
    private val formatoMesAnio = SimpleDateFormat("MMMM yyyy", Locale("es", "PE"))
    private val formatoMesCorto = SimpleDateFormat("MMM", Locale("es", "PE"))
    private val formatoDiaSemana = SimpleDateFormat("EEEE", Locale("es", "PE"))
    private val formatoDia = SimpleDateFormat("dd", Locale("es", "PE"))

    fun formatear_fecha_completa(timestamp: Long): String {
        return formatoFechaCompleta.format(Date(timestamp))
    }

    fun formatear_fecha_corta(timestamp: Long): String {
        return formatoFechaCorta.format(Date(timestamp))
    }

    fun formatear_hora(timestamp: Long): String {
        return formatoHora.format(Date(timestamp))
    }

    fun formatear_mes_anio(timestamp: Long): String {
        return formatoMesAnio.format(Date(timestamp)).capitalize()
    }

    fun formatear_mes_corto(timestamp: Long): String {
        return formatoMesCorto.format(Date(timestamp)).capitalize()
    }

    fun formatear_dia_semana(timestamp: Long): String {
        return formatoDiaSemana.format(Date(timestamp)).capitalize()
    }

    fun formatear_dia(timestamp: Long): String {
        return formatoDia.format(Date(timestamp))
    }

    fun formatear_relativo(timestamp: Long): String {
        val ahora = System.currentTimeMillis()
        val diferencia = ahora - timestamp

        val segundos = diferencia / 1000
        val minutos = segundos / 60
        val horas = minutos / 60
        val dias = horas / 24

        return when {
            segundos < 60 -> "Hace un momento"
            minutos < 60 -> "Hace ${minutos}m"
            horas < 24 -> "Hace ${horas}h"
            dias == 1L -> "Ayer"
            dias < 7 -> "Hace ${dias} días"
            dias < 30 -> "Hace ${dias / 7} semanas"
            dias < 365 -> "Hace ${dias / 30} meses"
            else -> "Hace ${dias / 365} años"
        }
    }

    fun es_hoy(timestamp: Long): Boolean {
        val calendario_hoy = Calendar.getInstance()
        val calendario_fecha = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        return calendario_hoy.get(Calendar.YEAR) == calendario_fecha.get(Calendar.YEAR) &&
                calendario_hoy.get(Calendar.DAY_OF_YEAR) == calendario_fecha.get(Calendar.DAY_OF_YEAR)
    }

    fun es_ayer(timestamp: Long): Boolean {
        val calendario_ayer = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        val calendario_fecha = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        return calendario_ayer.get(Calendar.YEAR) == calendario_fecha.get(Calendar.YEAR) &&
                calendario_ayer.get(Calendar.DAY_OF_YEAR) == calendario_fecha.get(Calendar.DAY_OF_YEAR)
    }

    fun es_esta_semana(timestamp: Long): Boolean {
        val calendario_hoy = Calendar.getInstance()
        val calendario_fecha = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        return calendario_hoy.get(Calendar.YEAR) == calendario_fecha.get(Calendar.YEAR) &&
                calendario_hoy.get(Calendar.WEEK_OF_YEAR) == calendario_fecha.get(Calendar.WEEK_OF_YEAR)
    }

    fun es_este_mes(timestamp: Long): Boolean {
        val calendario_hoy = Calendar.getInstance()
        val calendario_fecha = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        return calendario_hoy.get(Calendar.YEAR) == calendario_fecha.get(Calendar.YEAR) &&
                calendario_hoy.get(Calendar.MONTH) == calendario_fecha.get(Calendar.MONTH)
    }

    fun obtener_inicio_dia(timestamp: Long = System.currentTimeMillis()): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun obtener_fin_dia(timestamp: Long = System.currentTimeMillis()): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    fun obtener_inicio_mes(timestamp: Long = System.currentTimeMillis()): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun obtener_fin_mes(timestamp: Long = System.currentTimeMillis()): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    fun obtener_inicio_anio(timestamp: Long = System.currentTimeMillis()): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun obtener_fin_anio(timestamp: Long = System.currentTimeMillis()): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 31)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    fun dias_entre_fechas(fecha_inicio: Long, fecha_fin: Long): Int {
        val diferencia = fecha_fin - fecha_inicio
        return (diferencia / (1000 * 60 * 60 * 24)).toInt()
    }

    fun agregar_dias(timestamp: Long, dias: Int): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            add(Calendar.DAY_OF_YEAR, dias)
        }.timeInMillis
    }

    fun agregar_meses(timestamp: Long, meses: Int): Long {
        return Calendar.getInstance().apply {
            timeInMillis = timestamp
            add(Calendar.MONTH, meses)
        }.timeInMillis
    }

    fun obtener_nombre_mes(mes: Int): String {
        val calendario = Calendar.getInstance().apply {
            set(Calendar.MONTH, mes - 1)
        }
        return formatoMesAnio.format(calendario.time).split(" ")[0].capitalize()
    }

    fun obtener_mes_actual(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    fun obtener_anio_actual(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }
}