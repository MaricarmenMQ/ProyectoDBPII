package com.lvmh.pocketpet.dominio.modelos

data class transaccion (
    val id:Long =0,
    val tipo: Tipotransaccion,
    val monto: Double,
    val categoria: String,
    val descripcion: String,
    val fecha: Long = System.currentTimeMillis()
)
enum class Tipotransaccion{
    Ingreso,Gasto
}
