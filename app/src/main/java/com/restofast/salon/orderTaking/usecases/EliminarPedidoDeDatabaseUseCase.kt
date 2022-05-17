package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.sharedData.data.RepoDBOrders

class EliminarPedidoDeDatabaseUseCase (var repoDBOrders: RepoDBOrders) {

    operator fun invoke(pedidoID: String) = repoDBOrders.eliminarPedidoDeDatabase(pedidoID)

}