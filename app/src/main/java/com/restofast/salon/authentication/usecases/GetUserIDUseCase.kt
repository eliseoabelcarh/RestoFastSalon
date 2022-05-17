package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoDBUsers


class GetUserIDUseCase (var repoDBUsers: RepoDBUsers) {
    suspend operator fun invoke() = repoDBUsers.getCurrentUserUID()
}