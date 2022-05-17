package com.restofast.salon.entities.enums

enum class UnitOfMeasurement (val nombre: String){
    KILOS ("Kg."),
    GRAMOS( "grs."),
    UNIDADES("unid."),
    SACO("saco"),
    ATADO("atado"),
    CAJA("caja"),


    ;


    companion object {


        fun getUnitMedidaAsEnum(inputUnitMedidaNombre: String): UnitOfMeasurement {
            return when (inputUnitMedidaNombre) {
                UNIDADES.nombre -> UNIDADES
                ATADO.nombre -> ATADO
                GRAMOS.nombre -> GRAMOS
                KILOS.nombre -> KILOS
                SACO.nombre -> SACO
                CAJA.nombre -> CAJA
                else -> UNIDADES
            }
        }

    }

}