package com.restofast.salon.orderTaking.usecases

import android.util.Log
import com.restofast.salon.sharedData.data.RepoDBRestaurantes
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope

class GetCategoriasDeProductoDeSucursalUseCase (var repoDBRestaurantes: RepoDBRestaurantes, var repoDBUsers: RepoDBUsers) {

    suspend operator fun invoke() = coroutineScope {
        val restauranteID = repoDBUsers.getRestaurantIDDeCurrentEmpleado()
        val sucursalID = repoDBUsers.getSucursalIDDeCurrentEmpleado()
        val sucursal = repoDBRestaurantes.getSucursalOneTime(restauranteID, sucursalID)
        if (sucursal != null) {
            return@coroutineScope sucursal.listaDeCategoriasDeProducto
        }else{
            throw Exception("Error al obtener Categorias de Productos")
        }
    }
}