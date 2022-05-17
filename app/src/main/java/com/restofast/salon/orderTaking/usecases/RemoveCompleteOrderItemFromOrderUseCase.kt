package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderItem
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.entities.order.Table
import com.restofast.salon.sharedData.data.RepoDBTables

class RemoveCompleteOrderItemFromOrderUseCase (var repoDBTables: RepoDBTables) {
    suspend operator fun invoke (order: Order, orderItem: OrderItem) {

        when(order){
            is OrderTable -> {
                /*repoDBTables.removerOrderItemPorCompleto(order,orderItem)*/
            }
        }



    }
}