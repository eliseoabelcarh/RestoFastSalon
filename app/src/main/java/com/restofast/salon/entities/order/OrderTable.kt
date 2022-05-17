package com.restofast.salon.entities.order

import android.os.Parcelable
import com.restofast.salon.entities.enums.OrderType
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class OrderTable constructor(

    override var id: String = "",
    override var active: Boolean = false,
    override var restauranteID: String = "",
    override var sucursalID: String = "",
    override var openingTime: Date? = null,
    override var empleadoIDOpenOrder: String = "",
    override var pagoID: String = "",
    override var orderType: OrderType = OrderType.TABLE,
    override var comentariosDePedido: String = "",
    override var customerID: String = "",
    override var closingTime: Date? = null,
    override var empleadoIDCloseOrder: String = "",

    var tableID: String = "",
    var nroMesa: String = "",

    //YA TIENE orderItems EN EL SUPER()


) : Parcelable, Order(
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

