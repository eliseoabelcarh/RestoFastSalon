package com.restofast.salon.orderTaking.usecases


import com.restofast.salon.entities.order.Table
import com.restofast.salon.entities.products.Product
import com.restofast.salon.sharedData.data.RepoDBTables
import com.restofast.salon.sharedData.data.RepoDBUsers


class AddProductToTableUseCase(
    var repoDBTables: RepoDBTables,
    var repoDBUsers: RepoDBUsers
) {

    suspend operator fun invoke(table: Table, product: Product) {
        var userID = repoDBUsers.getCurrentUserUID()
        var user =  repoDBUsers.getUserFromDBByUID(userID)
        val restauranteID = user.restauranteID.toString()
        val sucursalID = user.sucursalID.toString()
        /*repoDBTables.agregarProductoATable(table, product,restauranteID,sucursalID)*/
    }

}