package com.lvmh.pocketpet.data.local.database

import androidx.room.TypeConverter
import com.lvmh.pocketpet.data.local.entidades.
import com.lvmh.pocketpet.data.local.entidades.CategoriaMeta
import com.lvmh.pocketpet.data.local.entidades.EstadoMeta
import com.lvmh.pocketpet.data.local.entidades.periodo_presupuesto
import com.lvmh.pocketpet.data.local.entidades.PrioridadMeta
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RoomTypeConverter{

    private val formateadorFechaHora = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun desdeFechaHoraLocal(valor: LocalDateTime?): String? {
        return valor?.format(formateadorFechaHora)
    }

    @TypeConverter
    fun aFechaHoraLocal(valor: String?): LocalDateTime? {
        return valor?.let { LocalDateTime.parse(it, formateadorFechaHora) }
    }

    @TypeConverter
    fun desdePeriodoPresupuesto(valor: periodo_presupuesto?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aPeriodoPresupuesto(valor: String?): periodo_presupuesto? {
        return valor?.let { periodo_presupuesto.valueOf(it) }
    }

    @TypeConverter
    fun desdeEstadoMeta(valor: EstadoMeta?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aEstadoMeta(valor: String?): EstadoMeta? {
        return valor?.let { EstadoMeta.valueOf(it) }
    }

    @TypeConverter
    fun desdePrioridadMeta(valor: PrioridadMeta?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aPrioridadMeta(valor: String?): PrioridadMeta? {
        return valor?.let { PrioridadMeta.valueOf(it) }
    }

    @TypeConverter
    fun desdeCategoriaMeta(valor: CategoriaMeta?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aCategoriaMeta(valor: String?): CategoriaMeta? {
        return valor?.let { CategoriaMeta.valueOf(it) }
    }

    @TypeConverter
    fun desdeTipoMascota(valor: TipoMascota?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aTipoMascota(valor: String?): TipoMascota? {
        return valor?.let { TipoMascota.valueOf(it) }
    }

    @TypeConverter
    fun desdeEstadoMascota(valor: EstadoMascota?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aEstadoMascota(valor: String?): EstadoMascota? {
        return valor?.let { EstadoMascota.valueOf(it) }
    }

    @TypeConverter
    fun desdeEstadoAnimoMascota(valor: EstadoAnimoMascota?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aEstadoAnimoMascota(valor: String?): EstadoAnimoMascota? {
        return valor?.let { EstadoAnimoMascota.valueOf(it) }
    }

    @TypeConverter
    fun desdeTipoLogro(valor: TipoLogro?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aTipoLogro(valor: String?): TipoLogro? {
        return valor?.let { TipoLogro.valueOf(it) }
    }

    @TypeConverter
    fun desdeNivelLogro(valor: NivelLogro?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aNivelLogro(valor: String?): NivelLogro? {
        return valor?.let { NivelLogro.valueOf(it) }
    }

    @TypeConverter
    fun desdeRarezaLogro(valor: RarezaLogro?): String? {
        return valor?.name
    }

    @TypeConverter
    fun aRarezaLogro(valor: String?): RarezaLogro? {
        return valor?.let { RarezaLogro.valueOf(it) }
    }
}