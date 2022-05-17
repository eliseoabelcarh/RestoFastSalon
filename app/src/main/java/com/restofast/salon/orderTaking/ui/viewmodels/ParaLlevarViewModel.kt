package com.restofast.salon.orderTaking.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restofast.salon.entities.order.PedidoToGo
import com.restofast.salon.orderTaking.usecases.CreateOrderToGoUseCase
import com.restofast.salon.orderTaking.usecases.EliminarPedidoDeDatabaseUseCase
import com.restofast.salon.orderTaking.usecases.GetActiveOrdersToGoRealTimeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ParaLlevarViewModel @ViewModelInject constructor(

    private val createOrderToGoUseCase: CreateOrderToGoUseCase,
    private val getActiveOrdersToGoRealTimeUseCase : GetActiveOrdersToGoRealTimeUseCase,
    private val eliminarPedidoDeDatabaseUseCase: EliminarPedidoDeDatabaseUseCase,

    ) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val orderIDCreado = MutableLiveData<String>()

    val listaOrdersToGoDB = MutableLiveData<MutableList<PedidoToGo>>()



    fun crearActiveOrderToGo(
        nombreCliente: String,
        apellidoCliente: String,
        celularCliente: String
    ) {
        viewModelScope.launch {
            createOrderToGoUseCase(
                nombreCliente,
                apellidoCliente,
                celularCliente
            ).observeForever { orderToGoIDCreado ->
                if(orderToGoIDCreado != null){
                    orderIDCreado.value = orderToGoIDCreado
                }else{
                    errorMessage.value = "Error creando Pedido, intente de nuevo"
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun cargarOrdersToGoDB() {
        viewModelScope.launch {
            try {
                getActiveOrdersToGoRealTimeUseCase().collect {
                    if(it != null){
                        if(listaOrdersToGoDB.value.isNullOrEmpty()){
                            listaOrdersToGoDB.value = it
                        }else{
                            listaOrdersToGoDB.value!!.clear()
                            listaOrdersToGoDB.value = it
                        }
                    }else{
                        throw Exception("cargarOrdersToGoDB: Error cargando pedidos en DB")
                    }
                }
            }catch (e: Exception){
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
            }
        }
    }

    fun eliminarPedido(pedidoID: String) {
        viewModelScope.launch {
            try {
                eliminarPedidoDeDatabaseUseCase(pedidoID)
            }catch (e: Exception){
                    errorMessage.value = e.message
            }
        }
    }


}