package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.sharedData.data.RepoDBTables

class EnviarPedidosACocinaUseCase (var repoDBTables: RepoDBTables) {


    suspend operator fun invoke(order: Order) {
        when(order){
            is OrderTable -> {
               /* repoDBTables.enviarPedidosACocinaParaOrderTable(order)*/
            }
        }
    }



}