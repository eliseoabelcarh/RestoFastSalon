package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoDBUsers
import com.restofast.salon.sharedData.domain.entities.UserModel


class CreateNewUserInDatabaseUseCase (var repoDBUsers: RepoDBUsers) {

    suspend operator fun invoke(userModel: UserModel) : Boolean {
        return repoDBUsers.createNewUserInDatabase(userModel)
    }

}