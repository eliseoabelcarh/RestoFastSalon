package com.restofast.salon.entities.order

import android.os.Parcelable
import com.restofast.salon.entities.enums.OrderType
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
open class Pedido constructor(
    open var id: String ="",
    open var active: Boolean = true,
    open var restauranteID: String = "",
    open var sucursalID : String = "",
    open var openingTime: Date?=null,
    open var empleadoIDOpenOrder: String = "",
    open var pagoID: String = "",
    open var orderType: OrderType = OrderType.SIN_ASIGNAR,
    open var comentariosDePedido: String ="",
    open var customerID: String= "",
    open var closingTime: Date? = null,
    open var empleadoIDCloseOrder: String = "",
    var itemsDePedido: MutableList<ItemDePedido> = ArrayList(),

) : Parcelable {

    fun mozoPuedeEliminarPedido(): Boolean{
        var siPuede = true
        if(itemsDePedido.isNotEmpty()){
            for (item in itemsDePedido){
                siPuede = siPuede && item.mozoPuedeBorrar()
            }
        }
       return siPuede
    }

    fun cualquieraPuedeEliminarPedido(): Boolean {
        return mozoPuedeEliminarPedido()
    }


}