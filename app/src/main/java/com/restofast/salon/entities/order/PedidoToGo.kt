package com.restofast.salon.entities.order

import android.os.Parcelable
import com.restofast.salon.entities.enums.OrderType
import java.util.*

class PedidoToGo constructor(

    override var id: String = "",
    override var active: Boolean = false,
    override var restauranteID: String = "",
    override var sucursalID: String = "",
    override var openingTime: Date? = null,
    override var empleadoIDOpenOrder: String = "",
    override var pagoID: String = "",
    override var orderType: OrderType = OrderType.TOGO,
    override var comentariosDePedido: String = "",
    override var customerID: String = "",
    override var closingTime: Date? = null,
    override var empleadoIDCloseOrder: String = "",

    var customerName: String = "",
    var customerLastname: String = "",
    var customerCellphone: String = "",

    //YA TIENE orderItems EN EL SUPER()


) : Parcelable, Pedido(
    id,
    active,
    restauranteID,
    sucursalID,
    openingTime,
    empleadoIDOpenOrder,
    pagoID,
    orderType,
    comentariosDePedido,
    customerID,
    closingTime,
    empleadoIDCloseOrder
) {




}
