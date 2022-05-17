package com.restofast.salon.entities.order

import android.os.Parcelable
import com.restofast.salon.entities.enums.*
import com.restofast.salon.entities.payment.Pago
import com.restofast.salon.entities.products.Product
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
open class Order constructor(

    open var id: String ="",
    open var active: Boolean = false,
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
    var orderItems: MutableList< @RawValue OrderItem> = ArrayList(),

    ) : Parcelable {

    override fun toString(): String {
        return "ID:$id openTime: $openingTime closeTime: $closingTime empl.Open: $empleadoIDOpenOrder empl.Close: $empleadoIDCloseOrder" +
                "orderItems $orderItems"
    }

    fun cerrar(empleadoID: String): Order {
        empleadoIDCloseOrder = empleadoID
        closingTime = Date()
        return this
    }



    fun checkSiEstaPreparadaParaCerrarse(){
        checkSiFaltanCamposRequeridos()
        if(!estaHabilitadoParaGenerarCuenta()){
            throw Exception ("estaPreparadaParaCerrarse:NO ESTA HABILITADO PARA GENERAR CUENTA")
        }
        actualizarSumaDeTotalAPagar()
        if(!sumatoriaPagosCoincideConTotalAPagar()){
            throw Exception ("estaPreparadaParaCerrarse:TOTAL A PAGAR DE ORDER NO COINCIDE CON TOTAL DEL PAGO")
        }
    }

    fun checkSiFaltanCamposRequeridos(){
        if(id.isBlank()){
            throw Exception ("id está en blanco")
        }
        if(openingTime== null){
            throw Exception ("openingTime es nulo")
        }
        if(empleadoIDOpenOrder.isBlank()){
            throw Exception ("empleadoIDOpenOrder está en blanco")
        }
        if(pagoID.isBlank()){
            throw Exception ("pago es nulo")
        }
        if(orderItems.size == 0){
            throw Exception ("orderItems está vacío")
        }
    }

    fun estaHabilitadoParaGenerarCuenta(): Boolean {
        var some = true
        for (orderItem in orderItems) {
            if (orderItem.orderItemState == OrderItemState.SERVIDO ||
                orderItem.orderItemState == OrderItemState.ENTREGA_MOZO ||
                orderItem.orderItemState == OrderItemState.ANULADO
            ) {
                some = some && true
            } else {
                some = some && false
            }
        }
        return some
    }

    fun setCommentForOrderItem(positionOrderItem: Int, comments: String){
        if(positionOrderItem != -1){
            orderItems[positionOrderItem].comentarios = comments
        }else{
            throw Exception ("error setCommentForOrderItem: orderItem no encontrado")
        }

    }

    fun tieneOrderItemsQueFaltanAceptar(): Boolean {
        var some = false
        for (orderItem in orderItems) {
            if (orderItem.orderItemState == OrderItemState.FALTA_ACEPTAR) {
                some = some || true
            }
        }
        return some
    }

    fun tieneOrderItemsQueEsperanAceptacion(): Boolean {
        var some = false
        for (orderItem in orderItems) {
            if (orderItem.orderItemState == OrderItemState.ESPERANDO_ACEPTACION) {
                some = some || true
            }
        }
        return some
    }

    fun tieneUnicamenteOrderItemsQueFaltanAceptar(): Boolean {
        var some = true
        for (orderItem in orderItems) {
            if (orderItem.orderItemState == OrderItemState.FALTA_ACEPTAR) {
                some = some && true
            }
            if (orderItem.orderItemState != OrderItemState.FALTA_ACEPTAR) {
                some = some && false
            }
        }
        return some
    }

    fun enviarOrdersItemsParaAceptacion() {
        for (orderItem in orderItems) {
            if (orderItem.orderItemState == OrderItemState.FALTA_ACEPTAR) {
                orderItem.orderItemState = OrderItemState.ESPERANDO_ACEPTACION
            }
        }
    }

    fun setOrdersItemsEsperandoAceptacionAEstadoPreparando() {
        for (orderItem in orderItems) {
            if (orderItem.orderItemState == OrderItemState.ESPERANDO_ACEPTACION) {
                orderItem.orderItemState = OrderItemState.PREPARANDO
            }
        }
    }

    fun actualizarSumaDeTotalAPagar() {
        var total = BigDecimal("0.00")
        for (orderItem in orderItems) {
            if(orderItem.orderItemState != OrderItemState.ANULADO){
                total = total.add(BigDecimal(orderItem.subTotal))
            }
        }
      /*  totalAPagar = total.toPlainString()*/
    }

    fun addOrderItemBuscandoSiYaExiste(product: Product, randomID: String){
        val indexEncontrado: Int? = buscarSiOrderItemYaExisteYFaltaAceptarOEntregaMozo(product)
        if (indexEncontrado == null) {
            val orderItemCreado: OrderItem = crearOrderItem(product, randomID )
            agregarOrderItem(orderItemCreado)
        }else {
            agregarUnoPorqueExistiaYFaltabaAceptar(indexEncontrado)
        }

    }

    fun removeOrderItemBuscandoSiYaExiste(product: Product){
        val indexEncontrado: Int? = buscarSiOrderItemYaExisteYFaltaAceptarOEntregaMozo(product)
        if (indexEncontrado == null) {
            throw Exception("removeOrderItemBuscandoSiYaExiste: OrderItem ya no existe")
        }else{
            quitarUnoCuandoFaltaAceptar(indexEncontrado)
        }
    }

    private fun crearOrderItem(product: Product, randomID: String): OrderItem {
        /*if (product.productType == ProductType.PLATO) {
            return OrderItem(
                randomID,
                1,
                product,
                product.sellPrice,
                OrderItemState.FALTA_ACEPTAR,
                Date(),
                null,
                "",
                ""
            )
        } else {*/
            return OrderItem(
                /*randomID,
                1,
                product,
                product.sellPrice,
                OrderItemState.ENTREGA_MOZO,
                Date(),
                null,
                "",
                ""*/
            )
        /*}*/
    }

    fun agregarOrderItem(orderItem: OrderItem) {
        orderItems.add(orderItem)
        actualizarSumaDeTotalAPagar()
    }

    private fun agregarUnoPorqueExistiaYFaltabaAceptar(orderItemIndexEncontrado: Int) {
        val orderItemEncontrado = orderItems[orderItemIndexEncontrado]
        orderItemEncontrado.agregarUnoCuandoFaltaAceptar()
        actualizarSumaDeTotalAPagar()
    }

    fun quitarUnoCuandoFaltaAceptar(orderItemIndexEncontrado: Int) {
        val orderItemEncontrado = orderItems[orderItemIndexEncontrado]
        if (orderItemEncontrado.qty > 1) {
            orderItemEncontrado.quitarUnoCuandoFaltaAceptar()
            actualizarSumaDeTotalAPagar()
        } else {
            removerOrderItemPorCompletoYActualizar(orderItemIndexEncontrado)
        }
    }

    fun removeOrderItemCompletoSiExisteYMozoPuedeBorrar(orderItem: OrderItem){
        val orderItemIndexEncontrado: Int? = buscarSiOrderItemExisteYMozoPuedeBorrar(orderItem)
        if (orderItemIndexEncontrado != null) {
            removerOrderItemPorCompletoYActualizar(orderItemIndexEncontrado)
        }else{
            throw Exception("orderitem ya no existee")
        }

    }

    fun removerOrderItemPorCompletoYActualizar(orderItemIndexEncontrado: Int) {
        orderItems.removeAt(orderItemIndexEncontrado)
        actualizarSumaDeTotalAPagar()
    }

    fun anularOrderItemYActualizar(indexOrderItem: Int, idEmpleadoQueAnula: String,motivoAnulacion: String){
        orderItems[indexOrderItem].anular(idEmpleadoQueAnula,motivoAnulacion)
        actualizarSumaDeTotalAPagar()
    }

    fun buscarSiOrderItemYaExisteYFaltaAceptarOEntregaMozo(product: Product): Int? {
        var indexEncontrado: Int? = null
        var i = 0
        while (i < orderItems.size && indexEncontrado == null) {
            val orderItem = orderItems[i]
            if (orderItem.product!!.id == product.id
                &&
                (orderItem.orderItemState == OrderItemState.FALTA_ACEPTAR
                        || orderItem.orderItemState == OrderItemState.ESPERANDO_ACEPTACION
                        || orderItem.orderItemState == OrderItemState.ENTREGA_MOZO)
            ) {
                indexEncontrado = i
            }
            i++
        }
        return indexEncontrado
    }

    fun buscarSiOrderItemExisteYMozoPuedeBorrar(orderItemBuscado: OrderItem): Int? {
        var indexEncontrado: Int? = null
        var i = 0
        while (i < orderItems.size && indexEncontrado == null) {
            val orderItem = orderItems[i]
            if (orderItem.id == orderItemBuscado.id
                &&
                (orderItem.orderItemState == OrderItemState.FALTA_ACEPTAR
                        || orderItem.orderItemState == OrderItemState.ESPERANDO_ACEPTACION
                        || orderItem.orderItemState == OrderItemState.ENTREGA_MOZO)
            ) {
                indexEncontrado = i
            }
            i++
        }
        return indexEncontrado
    }

    fun buscarSiOrderItemExiste(orderItemBuscado: OrderItem): Int? {
        var indexEncontrado: Int? = null
        var i = 0
        while (i < orderItems.size && indexEncontrado == null) {
            val orderItem = orderItems[i]
            if (orderItem.id == orderItemBuscado.id) {
                indexEncontrado = i
            }
            i++
        }
        return indexEncontrado
    }

    fun setStateToOrderItem(state: OrderItemState, index: Int){
        orderItems[index].orderItemState = state
        if(state == OrderItemState.SERVIDO){
            orderItems[index].closingTime = Date()
        }
    }

    fun sumatoriaPagosCoincideConTotalAPagar(): Boolean {
        return true
        //MODIFI8VAR LUEGOOOO
      /*  totalAPagar == pago?.totalSinDsctos*/
    }


    /*fun crearPagoVerificandoImportesCoincidan(pagoCreado: Pago){
        if (totalAPagar == pagoCreado.totalSinDsctos) {
            pago = pagoCreado
            neto = pagoCreado.totalSinDsctos
            bruto = pagoCreado.totalConDsctos
            totalCobrado = pagoCreado.totalConDsctos
        }else{
            throw Exception("crearPagoVerificandoImportesCoincidan: IMPORTES NO COINCIDEN")
        }
    }*/



}