package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.PedidoMesa
import com.restofast.salon.entities.order.Table
import com.restofast.salon.sharedData.data.RepoDBOrders
import com.restofast.salon.sharedData.data.RepoDBTables
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope
import java.util.*

class CreateNewPedidoMesaIDUseCase(var repoDBOrders: RepoDBOrders, var repoDBTables: RepoDBTables, var repoDBUsers: RepoDBUsers) {

    suspend operator fun invoke(table: Table) = coroutineScope {
        val orderTable = crearNuevaOrderTable(table)
        val newOrderID = repoDBOrders.saveOrderTableEnDB(orderTable)
        repoDBTables.addOrderTableIDToTable(table, newOrderID)
        return@coroutineScope newOrderID
    }

    private fun crearNuevaOrderTable(table: Table): PedidoMesa {
        return PedidoMesa(
            id = repoDBOrders.getRandomID(),
            active = true,
            restauranteID = table.restauranteID,
            sucursalID = table.sucursalID,
            openingTime = Date(),
            empleadoIDOpenOrder = repoDBUsers.getCurrentUserUIDNORMAL(),
            tableID = table.id,
            nroMesa = table.number
        )
    }

}