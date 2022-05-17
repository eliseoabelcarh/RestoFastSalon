package com.restofast.salon.authentication.usecases


import com.restofast.salon.entities.formaters.StringFormater
import com.restofast.salon.sharedData.data.RepoDBUsers
import com.restofast.salon.sharedData.data.RepoRoomUser


class LoginUserUseCase(var repoDBUsers: RepoDBUsers, var repoRoomUser: RepoRoomUser) {

    suspend operator fun invoke(email: String, password: String) {
        repoDBUsers.loginUser(email, StringFormater.hashPassword(password))
        if (repoDBUsers.userIsAuthenticated()) {
            val currentUserUID = repoDBUsers.getCurrentUserUID()
            val empleado = repoDBUsers.getUserFromDBByUID(currentUserUID)
            val userRoom = empleado.castToUserRoom()
            repoRoomUser.addUser(userRoom)
        }


    }

}