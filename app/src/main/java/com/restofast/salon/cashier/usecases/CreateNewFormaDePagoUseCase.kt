package com.restofast.salon.cashier.usecases

import com.restofast.salon.entities.enums.TipoFormaDePago
import com.restofast.salon.entities.payment.FormaDePago
import com.restofast.salon.entities.payment.FormaDePagoEfectivo
import com.restofast.salon.entities.payment.FormaDePagoVendemas
import com.restofast.salon.sharedData.data.RepoDBRestaurantes
import com.restofast.salon.sharedData.data.RepoDBTables
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope

class CreateNewFormaDePagoUseCase(
    var repoDBTables: RepoDBTables,
    var repoDBUsers: RepoDBUsers,
    var repoDBRestaurantes: RepoDBRestaurantes
) {

    suspend operator fun invoke(tipoFormaDePago: TipoFormaDePago): FormaDePago = coroutineScope {
        val idRandom = repoDBTables.getRandomID()
        val userID = repoDBUsers.getCurrentUserUID()
        val user = repoDBUsers.getUserFromDBByUID(userID)
        val restauranteID = user.restauranteID.toString()
        val sucursalID = user.sucursalID.toString()
        val sucursal = repoDBRestaurantes.getSucursalOneTime(restauranteID, sucursalID)
            ?: throw Exception("CreateNewFormaDePagoUseCase: sucursal no encontrada")
        val comisionVendemas = sucursal.comisionVendemas
        return@coroutineScope when (tipoFormaDePago) {
            TipoFormaDePago.EFECTIVO -> FormaDePagoEfectivo(
                idRandom,
                "0.00",
                "0.00",
                "0.00",
                TipoFormaDePago.EFECTIVO,

                )
            TipoFormaDePago.VENDEMAS -> FormaDePagoVendemas(
                idRandom,
                "0.00",
                "0.00",
                "0.00",
                TipoFormaDePago.VENDEMAS,
                comisionVendemas
            )
            else -> throw Exception("no existe tipo para Forma de pago")

        }
    }
}