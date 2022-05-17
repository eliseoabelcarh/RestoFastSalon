package com.restofast.salon.authentication.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restofast.salon.authentication.usecases.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class RegisterViewModel @ViewModelInject constructor(
    private val createNewUserInDatabaseUseCase: CreateNewUserInDatabaseUseCase,
    private val createModeloEmpleadoUseCase: CreateModeloEmpleadoUseCase,
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase,
    private val userIsAuthenticatedUseCase: UserIsAuthenticatedUseCase,
    private val loginUserUseCase: LoginUserUseCase

) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val userIsSuccesfullyRegistered = MutableLiveData<Boolean>()

    fun registrarUsuario(
        name: String,
        lastname: String,
        dni: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {

        viewModelScope.launch {
            try {
                passwordsMatch(password, confirmPassword)

                val uidCreado = createUserWithEmailAndPasswordUseCase(email, password)
                val isAuthenticated = userIsAuthenticatedUseCase.invoke()
                if (isAuthenticated) {
                    //CREA USER SEGUN CONFIGURATION DE APP
                    val userModelEmpleado =
                        createModeloEmpleadoUseCase(uidCreado, name, lastname, dni, email, password)

                    val success = createNewUserInDatabaseUseCase(userModelEmpleado)
                    if (success) {
                        loginUserUseCase(email, password)
                        //tiene q ser corutina
                        refreshUserIsSuccesfullyRegistered(true)
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            }

        }


    }

    private suspend fun refreshUserIsSuccesfullyRegistered(isSuccesfullyRegistered: Boolean) =
        coroutineScope {
            userIsSuccesfullyRegistered.value = isSuccesfullyRegistered
        }

    private fun passwordsMatch(password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            throw Exception("Passwords no coinciden")
        }
    }

}