package com.restofast.salon.cashier.usecases


import com.restofast.salon.entities.enums.OrderType
import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.sharedData.data.RepoDBTables
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class LoadOrderUseCase (var repoDBTables: RepoDBTables) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(order: Order): Flow<Order?> = coroutineScope {
        when(order.orderType){
            OrderType.TABLE -> {


                // BORRAR LUEG
                val borrar = emptyFlow<Order?>()
                return@coroutineScope borrar
              /*  return@coroutineScope repoDBTables.loadOrderTable(order as OrderTable)*/



            }
            else -> throw Exception("LoadOrderUseCase: no se encuentra tipo de Order")
        }
    }

}