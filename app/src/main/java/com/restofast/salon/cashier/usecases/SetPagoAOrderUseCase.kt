package com.restofast.salon.cashier.usecases

import androidx.lifecycle.MutableLiveData
import com.restofast.salon.entities.enums.OrderType
import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.entities.payment.FormaDePago
import com.restofast.salon.entities.payment.Pago
import com.restofast.salon.sharedData.data.RepoDBTables
import com.restofast.salon.sharedData.data.RepoDBUsers

class SetPagoAOrderUseCase(
    var repoDBTables: RepoDBTables,
    var repoDBUsers: RepoDBUsers
) {

    suspend operator fun invoke(
        order: Order,
        listaFormasDePago: MutableList<FormaDePago>,
        nroComprobante: String
    ): MutableLiveData<Boolean> {
        val listaFinal = listaFormasDePago
        val empleadoID = repoDBUsers.getCurrentUserUID()
        val pagoCreado = Pago()
        pagoCreado.orderID = order.id
        pagoCreado.empleadoIDPagoCreado = empleadoID
        if (nroComprobante.isNotBlank()) {
            pagoCreado.nroComprobante = nroComprobante
        }
        pagoCreado.setListaDeFormasDePagoYActualizarValores(listaFinal)

        when (order.orderType) {
            OrderType.TABLE -> {
                val orderTable = order as OrderTable

                // BORRA RLUEGO
                val borrar = MutableLiveData<Boolean>()
                return borrar
              /*  return repoDBTables.createPagoParaOrderTable(orderTable, pagoCreado)*/


            }
            else -> throw Exception("Error SetPagoAOrderUseCase: NO EXISTE tipo de Order")
        }

    }


}