package com.restofast.salon.authentication.usecases


import com.restofast.salon.sharedData.domain.entities.UserRoom
import kotlinx.coroutines.coroutineScope

class CreateUserRoomModelUseCase {

    suspend operator fun invoke(uid: String, name: String,lastname: String,dni: String, email: String, password: String): UserRoom = coroutineScope {
        validarCampo(uid)
        validarCampo(name)
        validarCampo(lastname)
        validarCampo(dni)
        validarCampo(email)
        validarCampo(password)
        return@coroutineScope UserRoom(uid, name,lastname,dni, email, password)
    }


    private fun validarCampo(string: String){
        if(string.isBlank()){
            throw Exception("campo CreateUserRoomModelUseCase vacío")
        }
    }



}