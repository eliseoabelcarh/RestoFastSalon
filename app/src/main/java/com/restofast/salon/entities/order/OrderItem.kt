package com.restofast.salon.entities.order

import com.restofast.salon.entities.enums.OrderItemState
import com.restofast.salon.entities.products.Product
import kotlinx.parcelize.RawValue
import java.math.BigDecimal
import java.util.*

class OrderItem(

    var id: String = "",
    var qty: Int = 0,
    var product: @RawValue Product? = null,
    var subTotal: String = "0.00",
    var orderItemState: OrderItemState = OrderItemState.FALTA_ACEPTAR,
    var openingTime: Date? = null,
    var closingTime: Date? = null,
    var comentarios : String = "",
    var motivoAnulacion : String = "",
    var empleadoIDQueAnula : String = "",

    ) {


    override fun toString(): String {
        return "cant: $qty  productoName: ${product?.name} subttoal: $subTotal  orderItemState: $orderItemState openTime: $openingTime closeTime: $closingTime"
    }

    fun anular(empleadoIDQueAnula: String, motivo: String){
        if(motivo.isNotBlank()){
            this.empleadoIDQueAnula = empleadoIDQueAnula
            motivoAnulacion = motivo
            closingTime = Date()
            orderItemState = OrderItemState.ANULADO
        }
    }
    fun agregarUnoCuandoFaltaAceptar() {
       /* qty++
        val priceProduct = product?.sellPrice
        subTotal = (BigDecimal(subTotal).add(BigDecimal(priceProduct))).toPlainString()*/
    }

    fun quitarUnoCuandoFaltaAceptar() {
      /*  qty--
        val priceProduct = product?.sellPrice
        subTotal = (BigDecimal(subTotal).subtract(BigDecimal(priceProduct))).toPlainString()*/
    }

    fun mozoPuedeAgregarComentarios(): Boolean {
        return orderItemState == OrderItemState.ENTREGA_MOZO
                || orderItemState == OrderItemState.FALTA_ACEPTAR
                || orderItemState == OrderItemState.ESPERANDO_ACEPTACION
    }

    fun cocineroPuedeAnularItem(): Boolean {
        return orderItemState == OrderItemState.PREPARANDO
                || orderItemState == OrderItemState.SERVIDO
    }
}