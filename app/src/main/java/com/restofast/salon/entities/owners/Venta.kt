package com.restofast.salon.entities.owners

import com.restofast.salon.entities.enums.TipoMovimiento
import com.restofast.salon.entities.order.Order
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
open class Venta constructor(

    override var id: String = "",
    override var nroOperacion: String = "",
    override var tipoMovimiento: TipoMovimiento = TipoMovimiento.VENTA,
    override var userID: String = "",
    override var userNombre: String = "",
    override var restauranteID: String = "",
    override var restauranteNombre: String = "",
    override var sucursalID: String = "",
    override var sucursalNombre: String = "",
    override var concepto: String = "",
    override var importe: String = "",
    override var active: Boolean = false,
    override var creacion: Date? = null,
    override var cerrado: Date? = null,
    override var editado: Boolean = false,
    override var ultimaEdicion: Date? = null,

    //override var cuentaUtilizada: Cuenta? = null,
    //override var categoria: CategoriaIngreso = CategoriaIngreso.SIN_CATEGORIA,

    open var order: Order? = null,

    open var listaMovimientos: MutableList<Movimiento> = ArrayList()


) : Movimiento(
    id,
    nroOperacion,
    tipoMovimiento,
    userID,
    userNombre,
    restauranteID,
    restauranteNombre,
    sucursalID,
    sucursalNombre,
    concepto,
    importe,
    active,
    creacion,
    cerrado,
    editado,
    ultimaEdicion,
    // cuentaUtilizada,
    //  categoria
) {

    fun agregarOrderYActualizarValores(orderCerrada: Order) {
       /* order = orderCerrada
        importe = orderCerrada.totalCobrado
        creacion = orderCerrada.openingTime
        cerrado = Date()*/
    }
}