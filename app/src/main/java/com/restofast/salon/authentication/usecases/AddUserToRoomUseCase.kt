package com.restofast.salon.authentication.usecases

import com.restofast.salon.sharedData.data.RepoRoomUser
import com.restofast.salon.sharedData.domain.entities.UserRoom


class AddUserToRoomUseCase(var repoRoomUser: RepoRoomUser) {

    suspend operator fun invoke(userRoom: UserRoom) = repoRoomUser.addUser(userRoom)

}