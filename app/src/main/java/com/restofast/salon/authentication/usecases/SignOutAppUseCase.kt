package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoDBUsers
import com.restofast.salon.sharedData.data.RepoRoomUser


class SignOutAppUseCase(var repoDBUsers: RepoDBUsers, var repoRoomUser: RepoRoomUser) {

    suspend operator fun invoke() {
        repoRoomUser.cleanRoom()
        repoDBUsers.signOut()
    }


}