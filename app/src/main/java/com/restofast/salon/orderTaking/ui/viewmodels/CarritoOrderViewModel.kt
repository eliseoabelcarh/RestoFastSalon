package com.restofast.salon.orderTaking.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restofast.salon.entities.order.ItemDePedido
import com.restofast.salon.entities.order.Pedido
import com.restofast.salon.entities.products.CategoriaProducto
import com.restofast.salon.entities.products.Product
import com.restofast.salon.orderTaking.usecases.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CarritoOrderViewModel @ViewModelInject constructor(

    private val getCategoriasDeProductoDeSucursalUseCase: GetCategoriasDeProductoDeSucursalUseCase,
    private val getListaProductosDatabaseUseCase: GetListaProductosDatabaseUseCase,
    private val crearItemDePedidoUseCase: CrearItemDePedidoUseCase,
    private val saveItemsEnPedidoDatabaseUseCase: SaveItemsEnPedidoDatabaseUseCase,
    private val getPedidoDesdeDatabaseUseCase: GetPedidoDesdeDatabaseUseCase,
    private val notificarCocinaParaAceptarPedidosUseCase: NotificarCocinaParaAceptarPedidosUseCase,


    ) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val pedidoDatabase = MutableLiveData<Pedido>()
    val listaCategoriasDeProductoDatabase = MutableLiveData<MutableList<CategoriaProducto>>()
    val listaProductosDatabase = MutableLiveData<MutableList<Product>>()

    val itemsGuardados = MutableLiveData<Boolean>()

    fun getCategoriasDeProductoDeSucursal() {
        viewModelScope.launch {
            try {
                listaCategoriasDeProductoDatabase.value =
                    getCategoriasDeProductoDeSucursalUseCase()!!
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }

    fun getProductosDeSucursal() {
        viewModelScope.launch {
            try {
                getListaProductosDatabaseUseCase().collect {
                    listaProductosDatabase.value = it
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
            }
        }
    }

    fun crearItemDePedido(product: Product): ItemDePedido {
        return crearItemDePedidoUseCase(product)
    }

    fun guardarItemsEnPedidoDatabase(
        orderIDEntrante: String,
        listaItemsDePedido: MutableList<ItemDePedido>
    ) {
        viewModelScope.launch {
            try {
                saveItemsEnPedidoDatabaseUseCase(
                    orderIDEntrante,
                    listaItemsDePedido
                ).addOnSuccessListener {
                    itemsGuardados.value = true
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
            }
        }
    }

    suspend fun notificarCocinaParaAceptarPedidos(orderIDEntrante: String) {
        try {
            notificarCocinaParaAceptarPedidosUseCase(orderIDEntrante)
        } catch (e: Exception) {
            if (e.message != "Job was cancelled") {
                errorMessage.value = e.message
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun cargarPedidoDesdeDatabase(orderIDEntrante: String) {
        viewModelScope.launch {
            try {
                getPedidoDesdeDatabaseUseCase(orderIDEntrante).collect {
                    pedidoDatabase.value = it
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
            }
        }
    }


}