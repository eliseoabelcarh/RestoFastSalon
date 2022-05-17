package com.restofast.salon.sharedData.data

import com.restofast.salon.sharedData.domain.daos.UserDao
import com.restofast.salon.sharedData.domain.entities.UserRoom
import kotlinx.coroutines.coroutineScope

class RepoRoomUser constructor(private val userDao: UserDao) {

    suspend fun cleanRoom() {
        userDao.cleanRoom()
    }

    suspend fun addUser(userRoom: UserRoom) {
        userDao.insert(userRoom)
    }

    suspend fun getUserInRoom(): UserRoom?  = coroutineScope {
        var userRoom : UserRoom? = null
        val lista = userDao.loadAll()
        if (lista != null) {
            if (lista.isNotEmpty()) {
                userRoom = lista[0]
            }
        }
        return@coroutineScope userRoom
    }

}