package com.restofast.salon.orderTaking.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restofast.salon.authentication.usecases.GetUserIDUseCase
import com.restofast.salon.authentication.usecases.GetEmpleadoUseCase
import com.restofast.salon.authentication.usecases.GetIDSegunRestaurantYSucursalDeEmpleadoUseCase
import com.restofast.salon.entities.enums.ProductType
import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderItem
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.entities.order.Table
import com.restofast.salon.entities.persons.Empleado
import com.restofast.salon.entities.products.Product
import com.restofast.salon.orderTaking.usecases.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TableOrderViewModel @ViewModelInject constructor(
    private val createProductoEnDatabaseUseCase: CreateProductoEnDatabaseUseCase,
    private val getIDSegunRestaurantYSucursalDeEmpleadoUseCase: GetIDSegunRestaurantYSucursalDeEmpleadoUseCase,
    private val getPlatosDatabaseByOwnerIDUseCase: GetPlatosDatabaseByOwnerIDUseCase,
    private val getBebidasDatabaseByOwnerIDUseCase: GetBebidasDatabaseByOwnerIDUseCase,
    private val getEnvasesDatabaseByOwnerIDUseCase: GetEnvasesDatabaseByOwnerIDUseCase,
    private val getTableDBUseCase: GetTableDBUseCase,
    private val addProductToTableUseCase: AddProductToTableUseCase,
    private val removeProductFromOrderUseCase: RemoveProductFromOrderUseCase,
    private val removeCompleteOrderItemFromOrderUseCase : RemoveCompleteOrderItemFromOrderUseCase,
    private val enviarPedidosACocinaUseCase : EnviarPedidosACocinaUseCase,
    private val addCommentKitchenToOrderItemUseCase : AddCommentKitchenToOrderItemUseCase,
    private val getUserIDUseCase: GetUserIDUseCase,
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val getProductosCartaUseCase : GetProductosCartaUseCase

    // private val getOrderTableDeMesaUseCase: GetOrderTableDeMesaUseCase,
    //  private val addOneOrderItemToOrderTableUseCase: AddOneOrderItemToOrderTableUseCase,
    //   private val minusOneOrderItemToOrderTableUseCase: MinusOneOrderItemToOrderTableUseCase,
    // private val removeOrderItemToOrderTableUseCase: RemoveOrderItemToOrderTableUseCase,


) : ViewModel() {

    val listaProductosDatabase = MutableLiveData<MutableList<Product>>()
    val errorMessage = MutableLiveData<String>()

    var tableDB = MutableLiveData<Table>()
    var orderItemsDeTableDB = MutableLiveData<MutableList<OrderItem>>()
    val currentEmpleado = MutableLiveData<Empleado>()


    @ExperimentalCoroutinesApi
    fun cargarListaProductosDeCarta(){
        viewModelScope.launch {
            try {
                getProductosCartaUseCase.invoke().collect {
                    listaProductosDatabase.value = it
                }
            }catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d("TAG--", "Error cargarListaProductosDeCarta: ${e.message}")
            }
        }
    }

  /*  fun cargarTableDB(table: Table) {
        viewModelScope.launch {
            try {
                getTableDBUseCase(table).collect {
                    if (it != null) {
                        tableDB.value = it
                        if (it.orderTable != null) {
                            orderItemsDeTableDB.value = it.orderTable!!.orderItems
                        }else{
                            val listaVacia : MutableList<OrderItem> = ArrayList()
                            orderItemsDeTableDB.value = listaVacia
                        }
                    }
                }
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d("TAG--", "Error cargarTableDB: ${e.message}")
            }
        }
    }*/
    fun agregarProductoAPedidoDeMesa(table: Table, product: Product) {
        viewModelScope.launch {
            try {
                addProductToTableUseCase(table, product)
            } catch (e: Exception) {
                errorMessage.value = e.message
                Log.d("TAG--", "Error agregarProductoAPedidoDeMesa: ${e.message}")
            }
        }
    }

    fun addOneOrderItemToOrderTable(table: Table, orderItem: OrderItem) {
        viewModelScope.launch {
            try {
                agregarProductoAPedidoDeMesa(table, orderItem.product!!)
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d("TAG--", "Error addOneOrderItemToOrderTable: ${e.message}")
            }
        }
    }

    /*fun minusOneOrderItemToOrderTable(table: Table, orderItem: OrderItem) {
        viewModelScope.launch {
            try {
                if(table.orderTable == null){
                    throw Exception("minusOneOrderItemToOrderTable: orderTable es null")
                }
                removeProductFromOrderUseCase(table.orderTable!!, orderItem.product!!)
            } catch (e: Exception) {
                if (e.message != "Job was cancelled") {
                    errorMessage.value = e.message
                }
                Log.d("TAG--", "Error minusOneOrderItemToOrderTable: ${e.message}")
            }
        }
    }

    fun removeCompleteOrderItemFromTable(table: Table, orderItem: OrderItem) {
        viewModelScope.launch {
            try {
                if(table.orderTable == null){
                    throw Exception("minusOneOrderItemToOrderTable: orderTable es null")
                }
                removeCompleteOrderItemFromOrderUseCase(table.orderTable!!, orderItem)
            } catch (e: Exception) {
                errorMessage.value = e.message
                Log.d("TAG--", "Error removeCompleteOrderItemFromTable: ${e.message}")
            }
        }
    }
*/
    fun enviarPedidosACocina(order: Order?) {
        viewModelScope.launch {
            try {
                if(order == null){
                    throw Exception ("enviarPedidosACocina Error: orderTable es null")
                }
                enviarPedidosACocinaUseCase(order)
            } catch (e: Exception) {
                errorMessage.value = e.message
                Log.d("TAG--", "Error enviarPedidosACocina: ${e.message}")
            }
        }
    }

   /* fun addCommentToOrderItem(table: Table, orderItem: OrderItem, comments: String) {
        viewModelScope.launch {
            try {
                if(table.orderTable != null){
                    addCommentKitchenToOrderItemUseCase(table.orderTable!!, orderItem, comments)
                }

            } catch (e: Exception) {
                errorMessage.value = e.message
                Log.d("TAG--", "Error agregarProductoAPedidoDeMesa: ${e.message}")
            }
        }
    }
*/
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
                Log.d("TAG--", "Error cargarCurrentEmpleado: ${e.message}")
            }
        }
    }


}