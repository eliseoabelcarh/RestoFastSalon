package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.entities.order.Table
import com.restofast.salon.entities.products.Product
import com.restofast.salon.sharedData.data.RepoDBTables


class RemoveProductFromOrderUseCase (var repoDBTables: RepoDBTables) {

    suspend operator fun invoke (order: Order, product: Product) {
        when(order){
            is OrderTable -> {
               /* repoDBTables.removerProductoDeOrderTable(order,product)*/
            }
        }



    }

}