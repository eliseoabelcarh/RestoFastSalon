package com.restofast.salon.sharedData.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.restofast.salon.entities.enums.OrderType
import com.restofast.salon.entities.enums.TableState
import com.restofast.salon.entities.order.*
import com.restofast.salon.entities.owners.SucursalRest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal

class RepoDBOrders constructor(
    private var db: FirebaseFirestore
) {

    private val ordersCollection: CollectionReference = db.collection("orders")
    private val tablesCollection: CollectionReference = db.collection("tables")
    private val restaurantesCollection: CollectionReference = db.collection("restaurantes")

    fun saveClosedOrderEnDB(order: Order) {

        //  ordersCollection.document(order.id).set(order)

        val sfDocRef = ordersCollection.document(order.id)
        db.runTransaction { transaction ->
            transaction.set(sfDocRef, order)
            null
            //success
        }.addOnSuccessListener {
            Log.d("TAG--", "Transaction saveClosedOrderEnDB success!")

        }
            .addOnFailureListener { e ->
                Log.w("TAG--", "Transaction saveClosedOrderEnDB failure.", e)
            }

    }

    fun getRandomID(): String {
        return ordersCollection.document().id
    }

    @ExperimentalCoroutinesApi
    fun getDisponibleHoyDeSucursalRealTimeByOwnerID(ownerID: String): Flow<String> = callbackFlow {
        var eventsCollection: Query? = null
        try {
            eventsCollection = ordersCollection.whereEqualTo("ownerID", ownerID)
        } catch (e: Throwable) {
            close(e)
        }
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) {
                return@addSnapshotListener
            }
            try {
                //   Log.d("TAG--", "${snap.data}")
                if (isActive) {

                    var disponibleNetoHoy = BigDecimal("0.00")
                    val listaClosedOrders = snapshot.toObjects<Order>().toMutableList()
                    if (listaClosedOrders.size != 0) {
                        for (order in listaClosedOrders) {
                            /////// CORREGIR LUEGOOOOOOOOO
                            /*     disponibleNetoHoy += disponibleNetoHoy.add(BigDecimal(order.neto))*/
                        }
                        offer(disponibleNetoHoy.toPlainString())

                    } else {
                        offer("Sin Registros")
                    }
                }
            } catch (e: Throwable) {
                throw e
            }
        }
        awaitClose { subscription?.remove() }
    }

/*
    suspend fun getOrderByID(orderID: String): Order {
        val order = ordersCollection.document(orderID).get().await().toObject<Order>()
        if (order != null) {
            when (order.orderType) {
                OrderType.TABLE -> {
                    return ordersCollection.document(orderID).get().await().toObject<OrderTable>()
                        ?: throw Exception("getOrderByID: no se pudo cargar OrderTable")
                }
                else -> throw Exception("getOrderByID: orderDB no es de tipo conocido")
            }
        } else {
            throw Exception("getOrderByID: no existe order en DB")
        }

    }
*/


    /////////////////////// NUEVOSAS FUNCIONESSSSSSS PA ABAJOO

    suspend fun saveOrderTableEnDB(pedidoMesa: PedidoMesa): String = coroutineScope {
        val randomID = getRandomID()
        ordersCollection.document(randomID).set(pedidoMesa).await()
        return@coroutineScope randomID
    }

    suspend fun saveItemsEnPedidoDatabase(
        orderIDEntrante: String,
        listaItemsDePedido: MutableList<ItemDePedido>
    ) = coroutineScope {
        val sfDocRef = ordersCollection.document(orderIDEntrante)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)
            val pedido = snapshot.toObject<Pedido>() ?: throw Exception("pedido no casteadoo")
            when (pedido.orderType) {
                OrderType.TABLE -> {
                    actualizarStateDeMesa(snapshot, listaItemsDePedido)
                }
                OrderType.TOGO -> {
                }
                OrderType.DELIVERY -> {
                }
                OrderType.SIN_ASIGNAR -> {
                }
            }
            transaction.update(
                sfDocRef, mapOf(
                    "itemsDePedido" to listaItemsDePedido
                )
            )
            null
            //success
        }.addOnFailureListener {
            throw Exception("Error actualizando items en Pedido DB")
        }
    }

    private fun actualizarStateDeMesa(
        snapshot: DocumentSnapshot,
        listaItemsDePedido: MutableList<ItemDePedido>
    ) {
        val pedidoMesa =
            snapshot.toObject<PedidoMesa>() ?: throw Exception("pedidoMesa no casteadoo")
        var stateActual = TableState.CLOSED
        if (listaItemsDePedido.isNotEmpty()) {
            stateActual = TableState.OPEN
        }
        tablesCollection.document(pedidoMesa.tableID).update(
            mapOf(
                "state" to stateActual
            )
        )
    }

    @ExperimentalCoroutinesApi
    fun getPedidoRealTime(pedidoID: String): Flow<Pedido> = callbackFlow {
        var eventsCollection: DocumentReference? = null
        try {
            eventsCollection = ordersCollection.document(pedidoID)
        } catch (e: Throwable) {
            close(e)
        }
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) {
                return@addSnapshotListener
            }
            try {
                if (isActive) {
                    val pedido = snapshot.toObject<Pedido>()
                        ?: throw Exception("Error casteando pedido en get Items de Pedido")
                    when (pedido.orderType) {
                        OrderType.TABLE -> {
                            val pedidoMesa = snapshot.toObject<PedidoMesa>()
                                ?: throw Exception("Error casteando pedidoMesa en get Items de Pedido")
                            offer(pedidoMesa)
                        }
                        OrderType.TOGO -> {
                            val pedidoToGo = snapshot.toObject<PedidoToGo>()
                                ?: throw Exception("Error casteando pedidoToGo en get Items de Pedido")
                            offer(pedidoToGo)
                        }
                        OrderType.DELIVERY -> {
                        }
                        else -> {
                            throw Exception("tipopedido no hallado")
                        }
                    }
                }
            } catch (e: Throwable) {
                throw e
            }
        }
        awaitClose { subscription?.remove() }
    }

    fun enviarItemsPedidoParaAceptacionACocina(pedidoID: String, sucursal: SucursalRest) {
        val sfDocRef = ordersCollection.document(pedidoID)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)
            val pedido = snapshot.toObject<Pedido>() ?: throw Exception("pedido no casteadoo")
            val listaItems = pedido.itemsDePedido
            val listaFinal: MutableList<ItemDePedido> = ArrayList()
            val categoriasDeProducto = sucursal.listaDeCategoriasDeProducto
            for (item in listaItems) {
                val itemActualizado =
                    item.aplicarStateParaEnviarAceptacionEnCocinaSegunCategoriaProducto(
                        categoriasDeProducto
                    )
                listaFinal.add(itemActualizado)
            }
            transaction.update(
                sfDocRef, mapOf(
                    "itemsDePedido" to listaFinal
                )
            )
            null
            //success
        }.addOnFailureListener {
            throw Exception("Error actualizando items en Pedido DB")
        }
    }

    fun createPedidoToGoEnDB(pedidoToGo: PedidoToGo): MutableLiveData<String?> {
        val observableOrderToGoIDCreado = MutableLiveData<String?>()
        val sfDocRef = ordersCollection.document(pedidoToGo.id)
        db.runTransaction { transaction ->
            transaction.set(sfDocRef, pedidoToGo)
            null
            //success
        }.addOnSuccessListener {
            observableOrderToGoIDCreado.value = pedidoToGo.id
        }.addOnFailureListener { e ->
            observableOrderToGoIDCreado.value = null
        }
        return observableOrderToGoIDCreado
    }

    @ExperimentalCoroutinesApi
    fun getActivePedidosToGoRealTime(
        restauranteID: String,
        sucursalID: String
    ): Flow<MutableList<PedidoToGo>?> =
        callbackFlow {
            var eventsCollection: Query? = null
            try {
                eventsCollection =
                    ordersCollection
                        .whereEqualTo("sucursalID", sucursalID)
                        .whereEqualTo("restauranteID", restauranteID)
                        .whereEqualTo("active",true)
                        .whereEqualTo("orderType", OrderType.TOGO)
            } catch (e: Throwable) {
                close(e)
            }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    if (isActive) {
                        val listaOrdersToGo = snapshot.toObjects<PedidoToGo>().toMutableList()
                        offer(listaOrdersToGo)

                    }
                } catch (e: Throwable) {
                    throw e
                }
            }
            awaitClose { subscription?.remove() }
        }

    fun eliminarPedidoDeDatabase(pedidoID: String) {
        val sfDocRef = ordersCollection.document(pedidoID)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)
            val pedido = snapshot.toObject<Pedido>() ?: throw Exception("pedido no casteadoo")
            if(pedido.cualquieraPuedeEliminarPedido()){
                transaction.delete(sfDocRef)
            }
            null
            //success
        }.addOnFailureListener {
            throw Exception("No se pudo borrar Pedido de la DB")
        }
    }


}