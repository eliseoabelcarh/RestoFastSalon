package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.products.Bebida
import com.restofast.salon.entities.products.Envase
import com.restofast.salon.sharedData.data.RepoDBProducts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class GetEnvasesDatabaseByOwnerIDUseCase (var repoDBProducts: RepoDBProducts) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(ownerID: String): Flow<MutableList<Envase>> = coroutineScope {
        return@coroutineScope repoDBProducts.getEnvasesFromDatabaseRealTime(ownerID)
    }

}