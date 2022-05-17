package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoRoomUser


class CleanRoomUseCase(var repoRoomUser: RepoRoomUser) {

    suspend fun invoke() = repoRoomUser.cleanRoom()

}