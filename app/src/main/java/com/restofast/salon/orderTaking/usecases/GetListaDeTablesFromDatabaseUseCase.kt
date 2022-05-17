package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.Table
import com.restofast.salon.sharedData.data.RepoDBTables
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class GetListaDeTablesFromDatabaseUseCase(var repoDBTables: RepoDBTables) {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(restauranteID : String, sucursalID: String): Flow<MutableList<Table>?> = coroutineScope{
        return@coroutineScope repoDBTables.getListaDeTablesFromDatabaseRealTime(restauranteID,sucursalID)
    }


}