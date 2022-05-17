package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.products.Product
import com.restofast.salon.sharedData.data.RepoDBProducts
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class GetProductosCartaUseCase (val repoDBUsers: RepoDBUsers,val repoDBProducts: RepoDBProducts) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(): Flow<MutableList<Product>> = coroutineScope {
        val userID = repoDBUsers.getCurrentUserUID()
        val user = repoDBUsers.getUserFromDBByUID(userID)
        val restauranteID = user.restauranteID.toString()
        val sucursalID = user.sucursalID.toString()
        return@coroutineScope repoDBProducts.getProductosCartaBySucursal(restauranteID,sucursalID)
    }


}