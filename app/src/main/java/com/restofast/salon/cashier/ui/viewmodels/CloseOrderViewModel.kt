package com.restofast.salon.cashier.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restofast.salon.cashier.usecases.CloseOrderInDBUseCase
import com.restofast.salon.cashier.usecases.LoadOrderUseCase
import com.restofast.salon.entities.order.Order
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CloseOrderViewModel @ViewModelInject constructor(


    private val loadOrderUseCase: LoadOrderUseCase,
    private val closeOrderInDBUseCase : CloseOrderInDBUseCase,


    ) : ViewModel() {


    val errorMessage = MutableLiveData<String>()
    val orderDB = MutableLiveData<Order>()
    val orderClosedExitosamente = MutableLiveData<Boolean>()


    @ExperimentalCoroutinesApi
    fun cargarOrderDB(order: Order) {
        viewModelScope.launch {
            try {
                loadOrderUseCase(order).collect {
                    orderDB.value = it
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d("TAG--", "cargarOrderPayment: ${e.message}")
            }
        }
    }

    fun closeOrderInDB(order: Order, inputNroComprobante:String) {
        viewModelScope.launch {
            try {
                val response = closeOrderInDBUseCase(order, inputNroComprobante)
                response.observeForever { result ->
                    orderClosedExitosamente.value = result
                }

            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d("TAG--", "cargarOrderPayment: ${e.message}")
            }
        }
    }


}