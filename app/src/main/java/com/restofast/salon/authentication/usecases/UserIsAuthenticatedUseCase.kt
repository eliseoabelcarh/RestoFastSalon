package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoDBUsers


class UserIsAuthenticatedUseCase (var repoDBUsers: RepoDBUsers){

    suspend operator fun invoke() : Boolean {
        return repoDBUsers.userIsAuthenticated()
    }

}