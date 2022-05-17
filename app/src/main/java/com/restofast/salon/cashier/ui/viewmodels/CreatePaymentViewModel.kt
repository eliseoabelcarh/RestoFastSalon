package com.restofast.salon.cashier.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restofast.salon.authentication.usecases.GetEmpleadoUseCase
import com.restofast.salon.authentication.usecases.GetUserIDUseCase
import com.restofast.salon.cashier.usecases.CreateNewFormaDePagoUseCase
import com.restofast.salon.cashier.usecases.GetUltimoNroBoletaSugeridoUseCase
import com.restofast.salon.cashier.usecases.LoadOrderUseCase
import com.restofast.salon.cashier.usecases.SetPagoAOrderUseCase
import com.restofast.salon.entities.enums.TipoFormaDePago
import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.payment.FormaDePago
import com.restofast.salon.entities.persons.Empleado
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreatePaymentViewModel @ViewModelInject constructor(
    private val loadOrderUseCase: LoadOrderUseCase,
    private val createNewFormaDePagoUseCase: CreateNewFormaDePagoUseCase,
    private val setPagoAOrderUseCase: SetPagoAOrderUseCase,
    private val getUserIDUseCase: GetUserIDUseCase,
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val getUltimoNroBoletaSugeridoUseCase: GetUltimoNroBoletaSugeridoUseCase

) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val orderDB = MutableLiveData<Order>()

    val listaMetodosPago = MutableLiveData<MutableList<FormaDePago>>()
    val pagoCreadoExitosamente = MutableLiveData<Boolean>()

    val currentEmpleado = MutableLiveData<Empleado>()

    val ultimoNroBoletaSugerido = MutableLiveData<String>()


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

    fun agregarListaYaExistente(lista: MutableList<FormaDePago>) {
        viewModelScope.launch {
            val listaVacia: MutableList<FormaDePago> = ArrayList()
            listaVacia.addAll(lista)
            listaMetodosPago.value = listaVacia
        }
    }

    fun agregarFormaDePago(tipoFormaDePago: TipoFormaDePago) {
        viewModelScope.launch {
            val one = createNewFormaDePagoUseCase(tipoFormaDePago)
            if (listaMetodosPago.value.isNullOrEmpty()) {
                val listaVacia: MutableList<FormaDePago> = ArrayList()
                listaVacia.add(one)
                listaMetodosPago.value = listaVacia
            } else {
                val lista: MutableList<FormaDePago> = listaMetodosPago.value!!
                lista.add(one)
                listaMetodosPago.value = lista
            }

        }
    }

    fun removeFormaDePagoDeListaMetodosPago(formaDePago: FormaDePago) {
        viewModelScope.launch {
            if (!listaMetodosPago.value.isNullOrEmpty()) {
                var lista = listaMetodosPago.value!!
                lista = removeFromLista(lista, formaDePago)
                listaMetodosPago.value = lista
            }
        }
    }

    private fun removeFromLista(
        lista: MutableList<FormaDePago>,
        formaDePago: FormaDePago
    ): MutableList<FormaDePago> {
        var encontrado: FormaDePago? = null
        var i = 0
        while (i < lista.size && encontrado == null) {
            if (lista[i].id == formaDePago.id) {
                encontrado = lista[i]
                lista.removeAt(i)
            }
            i++
        }
        return lista
    }

    fun btnSavePayment(
        order: Order,
        listaFormasDePago: MutableList<FormaDePago>,
        nroComprobante: String
    ) {
        viewModelScope.launch {
            try {
                val response = setPagoAOrderUseCase(order, listaFormasDePago, nroComprobante)
                response.observeForever { result ->
                    pagoCreadoExitosamente.value = result
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun cargarCurrentEmpleado() {
        viewModelScope.launch {
            try {
                getEmpleadoUseCase().collect {
                    currentEmpleado.value = it
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d("TAG--", "Error cargarCurrentEmpleadOOo: ${e.message}")
            }
        }
    }

    fun cargarUltimoNroBoletaSugerido() {
        viewModelScope.launch {
            try {
                val nro: Int = getUltimoNroBoletaSugeridoUseCase()
                ultimoNroBoletaSugerido.value = nro.toString()
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }


}