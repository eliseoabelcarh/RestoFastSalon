package com.restofast.salon.entities.enums

enum class CategoriaEgreso(val nombreComun: String) {
    SIN_CATEGORIA("Sin Categoría"),
    COMPRA("Compra"),
    PAGO_PROVEEDOR("Pago a proveedor"),
    PAGO_EMPLEADO("Pago a empleado"),
    PAGO_SERVICIO("Pago de Servicio"),
    DEVOLUCION_CAPITAL("Devolución Capital"),
    OTROS("Otros");

    companion object {
        fun getCategoriaAsEnum(inputCategoriaNombre: String): CategoriaEgreso {
            return when (inputCategoriaNombre) {
                COMPRA.nombreComun -> COMPRA
                PAGO_EMPLEADO.nombreComun -> PAGO_EMPLEADO
                PAGO_PROVEEDOR.nombreComun -> PAGO_PROVEEDOR
                PAGO_SERVICIO.nombreComun -> PAGO_SERVICIO
                DEVOLUCION_CAPITAL.nombreComun -> DEVOLUCION_CAPITAL
                OTROS.nombreComun -> OTROS
                else -> SIN_CATEGORIA
            }
        }
    }
}