package com.restofast.salon.entities.order

import com.restofast.salon.entities.enums.OrderDeliveryState
import com.restofast.salon.entities.enums.OrderType
import com.restofast.salon.entities.payment.Pago
import java.util.*

class OrderDelivery constructor(

    override var id: String = "",
    override var active: Boolean = false,
    override var restauranteID: String = "",
    override var sucursalID: String = "",
    override var openingTime: Date? = null,
    override var empleadoIDOpenOrder: String = "",
    override var pagoID: String = "",
    override var orderType: OrderType = OrderType.DELIVERY,
    override var comentariosDePedido: String = "",
    override var customerID: String = "",
    override var closingTime: Date? = null,
    override var empleadoIDCloseOrder: String = "",

    var empleadoDeliveryID: String = "",
    var orderDeliveryState: OrderDeliveryState = OrderDeliveryState.FALTA_ACEPTAR


) : Order(
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