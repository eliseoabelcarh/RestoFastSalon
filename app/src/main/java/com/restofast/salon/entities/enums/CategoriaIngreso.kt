package com.restofast.salon.entities.enums

enum class CategoriaIngreso(val nombreComun: String) {
    SIN_CATEGORIA("Sin Categoría"),
    VENTA("Venta"),
    PRESTAMO_A_CAJA("Prestamo a caja"),
    INGRESO_DE_CAPITAL("Ingreso de Capital"),
    OTROS("Otros")

    ;


    companion object {


        fun getCategoriaAsEnum(inputCategoriaNombre: String): CategoriaIngreso {
            return when (inputCategoriaNombre) {
                VENTA.nombreComun -> VENTA
                PRESTAMO_A_CAJA.nombreComun -> PRESTAMO_A_CAJA
                INGRESO_DE_CAPITAL.nombreComun -> INGRESO_DE_CAPITAL
                OTROS.nombreComun -> OTROS
                else -> SIN_CATEGORIA
            }
        }

    }
}