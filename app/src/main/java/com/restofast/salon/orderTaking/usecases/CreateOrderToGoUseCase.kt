package com.restofast.salon.orderTaking.usecases

import androidx.lifecycle.MutableLiveData
import com.restofast.salon.entities.order.PedidoToGo
import com.restofast.salon.entities.persons.Empleado
import com.restofast.salon.sharedData.data.RepoDBOrders
import com.restofast.salon.sharedData.data.RepoDBUsers
import com.restofast.salon.sharedData.data.RepoDBOrdersToGo
import kotlinx.coroutines.coroutineScope
import java.util.*

class CreateOrderToGoUseCase(var repoDBUsers: RepoDBUsers, var repoDBOrders: RepoDBOrders) {

    suspend operator fun invoke(
        nombreCliente: String, apellidoCliente: String, celularCliente: String
    ) : MutableLiveData<String?> = coroutineScope {

        val currentUserID = repoDBUsers.getCurrentUserUID()
        val currentEmpleado = repoDBUsers.getUserFromDBByUID(currentUserID)
        val restauranteID = currentEmpleado.restauranteID
        val sucursalID = currentEmpleado.sucursalID

        val pedidoToGo = crearPedidoToGo(
            currentUserID,
            restauranteID,
            sucursalID,
            nombreCliente,
            apellidoCliente,
            celularCliente
        )
        return@coroutineScope  repoDBOrders.createPedidoToGoEnDB(pedidoToGo)

    }

    private fun crearPedidoToGo(
        currentEmpleadoID: String,
        restauranteID: String,
        sucursalID: String,
        nombreCliente: String,
        apellidoCliente: String,
        celularCliente: String,
    ): PedidoToGo {

        return PedidoToGo(
            id = repoDBOrders.getRandomID(),
            active = true,
            restauranteID = restauranteID,
            sucursalID = sucursalID,
            openingTime = Date(),
            empleadoIDOpenOrder = currentEmpleadoID,
            customerName = nombreCliente,
            customerLastname = apellidoCliente,
            customerCellphone = celularCliente

        )
    }

}