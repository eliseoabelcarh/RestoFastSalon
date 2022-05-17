package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.products.Bebida
import com.restofast.salon.sharedData.data.RepoDBProducts

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class GetBebidasDatabaseByOwnerIDUseCase (var repoDBProducts: RepoDBProducts) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(ownerID: String): Flow<MutableList<Bebida>> = coroutineScope {
        return@coroutineScope repoDBProducts.getBebidasFromDatabaseRealTime(ownerID)
    }

}