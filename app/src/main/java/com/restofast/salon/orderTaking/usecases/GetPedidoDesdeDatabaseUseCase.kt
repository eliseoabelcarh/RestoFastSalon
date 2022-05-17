package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.Pedido
import com.restofast.salon.sharedData.data.RepoDBOrders
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class GetPedidoDesdeDatabaseUseCase(var repoDBOrders: RepoDBOrders) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(pedidoID: String): Flow<Pedido> = coroutineScope {
        repoDBOrders.getPedidoRealTime(pedidoID)
    }

}