package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoDBUsers


class ResetPasswordUseCase (var repoDBUsers: RepoDBUsers) {

    suspend operator fun invoke(email: String) {
        repoDBUsers.resetPassword(email)
    }



}