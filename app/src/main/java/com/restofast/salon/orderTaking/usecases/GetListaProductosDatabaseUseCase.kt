package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.sharedData.data.RepoDBProducts
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope

class GetListaProductosDatabaseUseCase (var repoDBProducts: RepoDBProducts, var repoDBUsers: RepoDBUsers) {

    suspend operator fun invoke() = coroutineScope {
        val restauranteID = repoDBUsers.getRestaurantIDDeCurrentEmpleado()
        val sucursalID = repoDBUsers.getSucursalIDDeCurrentEmpleado()
        return@coroutineScope repoDBProducts.getListaProductosByRestauranteYSucursal(
            restauranteID,
            sucursalID
        )
    }



}