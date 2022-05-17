package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoRoomUser
import com.restofast.salon.sharedData.domain.entities.UserRoom


class GetUserFromRoomUseCase(var repoRoomUser: RepoRoomUser) {

    suspend operator fun invoke(): UserRoom? {
        return repoRoomUser.getUserInRoom()
    }

}