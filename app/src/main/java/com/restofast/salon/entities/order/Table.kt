package com.restofast.salon.entities.order

import android.os.Parcelable
import com.restofast.salon.entities.enums.OrderType
import com.restofast.salon.entities.enums.RestauranteID
import com.restofast.salon.entities.enums.Sucursal
import com.restofast.salon.entities.enums.TableState
import com.restofast.salon.entities.payment.Pago
import com.restofast.salon.entities.products.Product
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*

@Parcelize
class Table constructor(

    var id: String = "",
    var number: String = "",
    var cardViewID: String = "",
    var restauranteID: String = "",
    var sucursalID: String = "",
    var orderTableID: String = "",
    var state: TableState = TableState.CLOSED,
    var showAlertIconMozo: Boolean = false,




    ) : Parcelable {

    //HECHO POR MI
    override fun toString(): String {
        return "NUMBER: $number - STATE: $state - showAlert: $showAlertIconMozo"
    }
    //FIN DE HECHO POR MI

   /* fun obtenerIDRestauranteSucursalOIgualOwnerID(): String {
        return ownerID
    }

    fun getTotalAPagar(): String {
        if (orderTable != null) {
            return orderTable!!.totalAPagar
        } else {
            return "0.00"
        }
    }

    fun crearPagoVerificandoImportesCoincidan(pagoCreado: Pago) {
        if (orderTable != null) {
            orderTable!!.crearPagoVerificandoImportesCoincidan(pagoCreado)
            actualizarValoresSegunEstado()
        } else {
            throw Exception("OrderTable de Mesa es NULL")
        }
    }

    fun actualizarValoresSegunEstado(){
        if(orderTable != null ){
            if(state == TableState.CLOSED){
                abrirMesa()
            }
            if (tieneUnicamenteOrderItemsQueFaltanAceptar()) {
                restablecerMesaSinAfectarOrderTable()
            }
            showAlertIconMozo = tieneOrderItemsQueFaltanAceptar()
            if (orderTable!!.orderItems.isEmpty()) {
                restablecerMesa()
            }
        }
    }

    fun abrirMesa() {
        openingTime = Date()
        state = TableState.OPEN
    }

    fun cerrarMesaYRestablecer(empleadoID: String,  nroComprobante: String): OrderTable {
        val orderTableCerrada = cerrarOrderTable(empleadoID, nroComprobante)
        restablecerMesa()
        return orderTableCerrada
    }

    fun cerrarOrderTable(empleadoID: String, nroComprobante: String): OrderTable {
        orderTable!!.pago!!.nroComprobante  = nroComprobante
        orderTable!!.checkSiEstaPreparadaParaCerrarse()
        return (orderTable!!.cerrar(empleadoID)as OrderTable)
    }

    fun restablecerMesa() {
        state = TableState.CLOSED
        showAlertIconMozo = false
        showAlertIconCocina = false
        showAlertIconDelivery = false
        openingTime = null
        orderTable = null
    }

    fun restablecerMesaSinAfectarOrderTable() {
        state = TableState.CLOSED
        showAlertIconMozo = false
        showAlertIconCocina = false
        showAlertIconDelivery = false
        openingTime = null
    }

    fun estaHabilitadoParaGenerarCuenta(): Boolean {
        if (orderTable != null) {
            return orderTable!!.estaHabilitadoParaGenerarCuenta()
        } else {
            throw Exception("OrderTable de Mesa es NULL")
        }
    }

    fun tieneOrderItemsQueFaltanAceptar(): Boolean {
        if (orderTable != null) {
            return orderTable!!.tieneOrderItemsQueFaltanAceptar()
        } else {
            throw Exception("OrderTable de Mesa es NULL")
        }
    }

    fun tieneUnicamenteOrderItemsQueFaltanAceptar(): Boolean {
        if (orderTable != null) {
            return orderTable!!.tieneUnicamenteOrderItemsQueFaltanAceptar()
        } else {
            throw Exception("OrderTable de Mesa es NULL")
        }
    }

    fun enviarOrdersItemsParaAceptacion() {
        if (orderTable != null) {
            orderTable!!.enviarOrdersItemsParaAceptacion()
            actualizarValoresSegunEstado()
        } else {
            throw Exception("OrderTable de Mesa es NULL")
        }
    }

    fun setOrdersItemsEsperandoAceptacionAEstadoPreparando() {
        if (orderTable != null) {
            orderTable!!.setOrdersItemsEsperandoAceptacionAEstadoPreparando()
        } else {
            throw Exception("OrderTable de Mesa es NULLLL")
        }
    }

    fun addOrderItemBuscandoSiYaExiste(product: Product, randomID: String) {
        if (orderTable != null) {
            orderTable!!.addOrderItemBuscandoSiYaExiste(product, randomID )
            actualizarValoresSegunEstado()
        } else {
            throw Exception("OrderTable de Mesa es NULL")
        }
    }
    fun removeOrderItemBuscandoSiYaExiste(product: Product) {
        if (orderTable != null) {
            orderTable!!.removeOrderItemBuscandoSiYaExiste(product)
            actualizarValoresSegunEstado()
        } else {
            throw Exception("OrderTable de Mesa es NULLL")
        }
    }
    fun removerOrderItemCompletoSiExisteYMozoPuedeBorrar(orderItem: OrderItem) {
        if (orderTable != null) {
            orderTable!!.removeOrderItemCompletoSiExisteYMozoPuedeBorrar(orderItem)
            actualizarValoresSegunEstado()
        } else {
            throw Exception("OrderTable de Mesa es NULLL")
        }
    }

    fun crearOrderTable(
        empleadoID: String,
        randomID: String,
        restauranteID: String,
        sucursalID: String
    ) {
        orderTable = OrderTable(
            randomID,
            ownerID,
            restauranteID,
            sucursalID,
            empleadoID,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            null,
           // sucursal,
            false,
            false,
            OrderType.TABLE,
            "",
            Date(),
            null,
            id,
            nroMesa = number
        )
    }
*/

    /////////////////////////nuevas funciones


    fun addOrderTableID(newOrderID: String) {
        if(orderTableID.isNotBlank()){
            throw Exception("Order ya tiene un orderID asignado")
        }
        this.orderTableID = newOrderID
    }


}

