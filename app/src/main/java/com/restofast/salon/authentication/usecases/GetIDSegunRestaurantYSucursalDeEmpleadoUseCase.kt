package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoDBUsers


class GetIDSegunRestaurantYSucursalDeEmpleadoUseCase (var repoDBUsers: RepoDBUsers) {


    suspend operator fun invoke () : String =  repoDBUsers.getRestaurantYSucursalIDDeCurrentEmpleado()


}