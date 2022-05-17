package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.sharedData.data.RepoDBOrders
import com.restofast.salon.sharedData.data.RepoDBRestaurantes
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope

class NotificarCocinaParaAceptarPedidosUseCase(
    var repoDBOrders: RepoDBOrders,
    var repoDBRestaurantes: RepoDBRestaurantes,
    var repoDBUsers: RepoDBUsers
) {

    suspend operator fun invoke(pedidoID: String) = coroutineScope {
        val restauranteID = repoDBUsers.getRestaurantIDDeCurrentEmpleado()
        val sucursalID = repoDBUsers.getSucursalIDDeCurrentEmpleado()
        val sucursalRest = repoDBRestaurantes.getSucursalOneTime(restauranteID, sucursalID)
            ?: throw Exception("Error obtendieno sucrusal ..")
        repoDBOrders.enviarItemsPedidoParaAceptacionACocina(pedidoID, sucursalRest)

    }

}