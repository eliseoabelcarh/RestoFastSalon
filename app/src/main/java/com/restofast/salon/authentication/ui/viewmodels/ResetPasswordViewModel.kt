package com.restofast.salon.authentication.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restofast.salon.authentication.usecases.ResetPasswordUseCase
import kotlinx.coroutines.launch

class ResetPasswordViewModel @ViewModelInject constructor(

    private val resetPasswordUseCase: ResetPasswordUseCase

): ViewModel() {

    val successMessage = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()



    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                resetPasswordUseCase(email)
                successMessage.value = "Te hemos enviado un email para restablecer tu password"
            }catch (e:Exception){
                errorMessage.value = e.message
            }


        }
    }










}