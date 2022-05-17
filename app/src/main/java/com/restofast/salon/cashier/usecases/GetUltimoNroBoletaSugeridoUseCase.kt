package com.restofast.salon.cashier.usecases

import com.restofast.salon.sharedData.data.RepoDBRestaurantes
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope

class GetUltimoNroBoletaSugeridoUseCase(
    var repoDBRestaurantes: RepoDBRestaurantes,
    var repoDBUsers: RepoDBUsers
) {

    suspend operator fun invoke() = coroutineScope {
        val userID = repoDBUsers.getCurrentUserUID()
        val user = repoDBUsers.getUserFromDBByUID(userID)
        val restauranteID = user.restauranteID.toString()
        val sucursalID = user.sucursalID.toString()
        val sucursal = repoDBRestaurantes.getSucursalOneTime(restauranteID, sucursalID)
            ?: throw Exception("GetUltimoNroBoletaSugeridoUseCase: sucursal no encontrada")
        return@coroutineScope sucursal.nroUltimaBoleta + 1
    }

}