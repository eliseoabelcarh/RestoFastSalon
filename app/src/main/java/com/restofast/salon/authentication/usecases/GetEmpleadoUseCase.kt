package com.restofast.salon.authentication.usecases


import com.restofast.salon.sharedData.data.RepoDBUsers
import com.restofast.salon.entities.persons.Empleado
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class GetEmpleadoUseCase(var repoDBUsers: RepoDBUsers) {

    @ExperimentalCoroutinesApi
    operator fun invoke(): Flow<Empleado?> {
         return   repoDBUsers.getUserFromDBByUIDRealTime(repoDBUsers.getCurrentUserUIDNORMAL())
    }
}