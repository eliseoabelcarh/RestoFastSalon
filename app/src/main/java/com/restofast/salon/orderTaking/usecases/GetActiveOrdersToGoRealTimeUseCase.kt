package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.PedidoToGo
import com.restofast.salon.sharedData.data.RepoDBOrders
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class GetActiveOrdersToGoRealTimeUseCase(
    var repoDBUsers: RepoDBUsers,
    var repoDBOrders: RepoDBOrders
) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(): Flow<MutableList<PedidoToGo>?> = coroutineScope {
        val currentUserID = repoDBUsers.getCurrentUserUIDNORMAL()
        val currentUser = repoDBUsers.getUserFromDBByUID(currentUserID)
        val restauranteID = currentUser.restauranteID
        val sucursalID = currentUser.sucursalID

        return@coroutineScope repoDBOrders.getActivePedidosToGoRealTime(restauranteID, sucursalID)
    }
}