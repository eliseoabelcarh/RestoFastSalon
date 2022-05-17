package com.restofast.salon.orderTaking.usecases

import com.restofast.salon.entities.order.Table
import com.restofast.salon.sharedData.data.RepoDBTables

class GetTablesDistributionOneTimeUseCase (var repoDBTables: RepoDBTables) {

    suspend operator fun invoke(restaurantID : String,sucursalID: String): MutableList<Table> = repoDBTables.getListaDeTablesFromDatabase(restaurantID,sucursalID)
}