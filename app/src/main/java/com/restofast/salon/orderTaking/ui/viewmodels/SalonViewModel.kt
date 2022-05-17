package com.restofast.salon.orderTaking.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.restofast.salon.authentication.usecases.GetEmpleadoUseCase
import com.restofast.salon.entities.order.Table
import com.restofast.salon.entities.persons.Empleado
import com.restofast.salon.orderTaking.usecases.CreateNewPedidoMesaIDUseCase
import com.restofast.salon.orderTaking.usecases.CreateTablesDistributionForFirstTimeEnDatabaseUseCase
import com.restofast.salon.orderTaking.usecases.GetListaDeTablesFromDatabaseUseCase
import com.restofast.salon.orderTaking.usecases.GetTablesDistributionOneTimeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SalonViewModel @ViewModelInject constructor(


    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val getTablesDistributionOneTimeUseCase: GetTablesDistributionOneTimeUseCase,
    private val createTablesDistributionForFirstTimeEnDatabaseUseCase: CreateTablesDistributionForFirstTimeEnDatabaseUseCase,
    private val getListaDeTablesFromDatabaseUseCase: GetListaDeTablesFromDatabaseUseCase,
    private val createNewPedidoMesaIDUseCase: CreateNewPedidoMesaIDUseCase,

    ) : ViewModel() {


    val errorMessage = MutableLiveData<String>()
    val tablesDistribution = MutableLiveData<MutableList<Table>>()
    val currentEmpleado = MutableLiveData<Empleado>()


    @ExperimentalCoroutinesApi
    fun cargarCurrentEmpleado() {
        viewModelScope.launch {
            try {
                getEmpleadoUseCase.invoke().collect {
                    if (it != null) {
                        currentEmpleado.value = it
                    }
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d("TAG--", "Error cargarCurrentEmpleado: ${e.message}")
            }
        }
    }

    private fun crearDistributionSiNoExiste(
        restaurantID: String,
        sucursalID: String,
        dist: ViewBinding
    ) {
        viewModelScope.launch {
            try {
                val lista: MutableList<Table> =
                    getTablesDistributionOneTimeUseCase(restaurantID, sucursalID)
                if (lista.isEmpty()) {
                    createTablesDistributionForFirstTimeEnDatabaseUseCase(dist)
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
                Log.d("TAG--", "Error crearDistributionSiNoExiste: ${e.message}")
            }

        }
    }

    @ExperimentalCoroutinesApi
    fun loadTablesDistribution(restaurantID: String, sucursalID: String, dist: ViewBinding) {
        viewModelScope.launch {
            try {
                crearDistributionSiNoExiste(restaurantID, sucursalID, dist)
                getListaDeTablesFromDatabaseUseCase(restaurantID, sucursalID).collect {
                    if (it == null) {
                        throw Exception("loadTablesDistribution Error en DB Cargando Lista Tables")
                    }
                    tablesDistribution.value = it
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d(
                    "TAG--", "ERROR loadTablesDistribution: ${e.message}"
                )
            }
        }
    }

    fun createOrderTableIDForTable(table: Table): MutableLiveData<String> {
        val liveData = MutableLiveData<String>()
        viewModelScope.launch {
            val orderTableID = createNewPedidoMesaIDUseCase(table)
            liveData.value = orderTableID
        }
        return liveData
    }


}