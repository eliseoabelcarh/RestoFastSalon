package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderDelivery
import com.restofast.salon.entities.order.OrderItem
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.sharedData.data.RepoDBTables


class AddCommentKitchenToOrderItemUseCase(var repoDBTables: RepoDBTables) {

    suspend operator fun invoke (order: Order, orderItem: OrderItem, comments: String) {
        if(order is OrderTable){
            val tableID = order.tableID
           /* repoDBTables.addCommentKitchenToOrderItem(tableID, orderItem, comments)*/
        }
        if(order is OrderDelivery){
            //do some
        }

    }

}