package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.products.Plato
import com.restofast.salon.sharedData.data.RepoDBProducts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class GetPlatosDatabaseByOwnerIDUseCase(var repoDBProducts: RepoDBProducts) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(ownerID: String): Flow<MutableList<Plato>> = coroutineScope {
       return@coroutineScope repoDBProducts.getPlatosFromDatabaseRealTime(ownerID)
    }



}