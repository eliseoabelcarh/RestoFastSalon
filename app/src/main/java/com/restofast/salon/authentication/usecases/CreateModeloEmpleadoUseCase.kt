package com.restofast.salon.authentication.usecases

import com.restofast.salon.entities.enums.RoleType
import com.restofast.salon.entities.formaters.StringFormater
import com.restofast.salon.entities.persons.Empleado
import kotlinx.coroutines.coroutineScope

class CreateModeloEmpleadoUseCase() {

    suspend operator fun invoke(
        uid: String,
        name: String,
        lastname: String,
        dni: String,
        email: String,
        password: String
    ): Empleado = coroutineScope {
        validarCampo(uid)
        validarCampo(name)
        validarCampo(lastname)
        validarCampo(dni)
        validarCampo(email)
        validarCampo(password)
        val lista: MutableList<RoleType> = ArrayList()


        //HABILITAR SOLO SI AGREGAR MINIMO MOZO AL CREAR
        lista.add(RoleType.MOZO)

        return@coroutineScope Empleado(
            uid,
            name,
            lastname,
            dni,
            email,
            StringFormater.hashPassword(password),
            lista
        )
    }


    private fun validarCampo(string: String) {
        if (string.isBlank()) {
            throw Exception("campo CreateModeloEmpleadoUseCase vacío")
        }
    }


}