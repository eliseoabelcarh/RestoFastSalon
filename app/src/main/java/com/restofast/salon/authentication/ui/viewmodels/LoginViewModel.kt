package com.restofast.salon.authentication.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restofast.salon.authentication.usecases.GetUserFromRoomUseCase
import com.restofast.salon.authentication.usecases.LoginUserUseCase
import com.restofast.salon.authentication.usecases.UserIsAuthenticatedUseCase
import com.restofast.salon.sharedData.domain.entities.UserRoom
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel @ViewModelInject constructor(
    private val userIsAuthenticatedUseCase: UserIsAuthenticatedUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val getUserFromRoomUseCase: GetUserFromRoomUseCase
) : ViewModel() {


    val errorMessage = MutableLiveData<String>()
    val userIsAuthenticated = MutableLiveData<Boolean>()

    fun checkIfUserIsAuthenticatedAndRedirect() {
        viewModelScope.launch {
            try {
                if (userIsAuthenticatedUseCase.invoke()) {
                    refreshUserIsAuthenticated(true)
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }

    fun loginUsuario(email: String, password: String) {
        viewModelScope.launch {
            try {
                loginUserUseCase(email, password)
                if (userIsAuthenticatedUseCase.invoke()) {
                    refreshUserIsAuthenticated(true)
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            }

        }

    }

    private suspend fun refreshUserIsAuthenticated(isAuthenticated: Boolean) = coroutineScope {
        userIsAuthenticated.value = isAuthenticated
        checkIfRoomIsEmpty()
    }

    fun checkIfRoomIsEmpty() {
        viewModelScope.launch {
            val userRoom: UserRoom? = getUserFromRoomUseCase()
            if (userRoom != null) {
                Log.d("TAG--", "user en Room: ${userRoom.email}")
            }else{
                Log.d("TAG--", "ROOM ESTA VACIAAA")
            }
        }
    }


}