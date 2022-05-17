package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.ItemDePedido
import com.restofast.salon.sharedData.data.RepoDBOrders
import com.restofast.salon.sharedData.data.RepoDBRestaurantes
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope

class SaveItemsEnPedidoDatabaseUseCase(
    var repoDBOrders: RepoDBOrders,
    var repoDBUsers: RepoDBUsers,
    var repoDBRestaurantes: RepoDBRestaurantes
) {

    suspend operator fun invoke(
        orderIDEntrante: String,
        listaItemsDePedido: MutableList<ItemDePedido>
    )
      = repoDBOrders.saveItemsEnPedidoDatabase(orderIDEntrante, listaItemsDePedido)




}