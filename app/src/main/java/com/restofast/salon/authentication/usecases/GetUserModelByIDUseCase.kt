package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoDBUsers

class GetUserModelByIDUseCase(var repoDBUsers: RepoDBUsers) {

    suspend operator fun invoke(id: String) = repoDBUsers.getUserModelByID(id)

}