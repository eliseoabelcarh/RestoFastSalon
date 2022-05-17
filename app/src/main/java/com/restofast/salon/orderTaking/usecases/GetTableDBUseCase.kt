package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.Table
import com.restofast.salon.sharedData.data.RepoDBTables
import kotlinx.coroutines.coroutineScope

class GetTableDBUseCase(var repoDBTables: RepoDBTables) {

    suspend operator fun invoke(table: Table) = coroutineScope {
        return@coroutineScope repoDBTables.getTableDBRealTime(table)
    }

}