package com.restofast.salon.sharedData.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.restofast.salon.entities.order.Table
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await
import kotlinx.parcelize.RawValue
import java.util.*

class RepoDBTables(
    private var db: FirebaseFirestore,
    private val repoDBUsers: RepoDBUsers,
    private val repoDBOrders: RepoDBOrders,
    private val repoDBMovimientos: RepoDBMovimientos,
    private val repoDBRestaurantes: RepoDBRestaurantes,
    private val repoDBProducts: RepoDBProducts
) {

    private val tablesCollection: CollectionReference = db.collection("tables")


    suspend fun createListaDeMesasEnDB(listaDeMesas: MutableList<Table>): Boolean = coroutineScope {
        for (table in listaDeMesas) {
            tablesCollection.document(table.id).set(table).await()
        }
        return@coroutineScope coroutineReturnBoolean(true)
    }

    private suspend fun coroutineReturnBoolean(boolean: Boolean): Boolean = coroutineScope {
        return@coroutineScope boolean
    }

    suspend fun getListaDeTablesFromDatabase(
        restauranteID: String,
        sucursalID: String
    ): MutableList<Table> {
        try {
            /*repoDBRestaurantes.createRestaurante()*/
            /*repoDBProducts.crearTodosProductosDeSucursalEnDB()*/
            return tablesCollection
                .whereEqualTo("restauranteID", restauranteID)
                .whereEqualTo("sucursalID", sucursalID)
                .get().await()
                .toObjects<Table>()
                .toMutableList()
        } catch (e: Exception) {
            throw Exception("ERROR getListaDeTablesFromDatabase De Firestore: ${e.message}")
        }
    }

    @ExperimentalCoroutinesApi
    fun getListaDeTablesFromDatabaseRealTime(
        restauranteID: String,
        sucursalID: String
    ): Flow<MutableList<Table>?> =
        callbackFlow {
            var eventsCollection: Query? = null
            try {
                eventsCollection = tablesCollection
                    .whereEqualTo("restauranteID", restauranteID)
                    .whereEqualTo("sucursalID", sucursalID)
            } catch (e: Throwable) {
                close(e)
            }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    if (isActive) {
                        val result = snapshot.toObjects<Table>().toMutableList()
                        offer(result)
                    }
                } catch (e: Throwable) {
                    throw e
                }
            }
            awaitClose { subscription?.remove() }
        }

    fun getRandomID(): String {
        return tablesCollection.document().id
    }

    /* suspend fun anularOrderItemDeOrderTable(
         orderItem: OrderItem,
         orderTable: OrderTable,
         idEmpleadoQueAnula: String,
         motivoAnulacion: String
     ) =
         coroutineScope {
             val sfDocRef = db.collection("tables").document(orderTable.tableID)
             db.runTransaction { transaction ->
                 val snapshot = transaction.get(sfDocRef)
                 val table = snapshot.toObject<Table>()
                 Log.d("TAG--", "Table encontrada: $table")
                 if (table != null) {
                     val orderTableDB: @RawValue OrderTable? = table.orderTable
                     if (orderTableDB != null) {
                         Log.d("TAG--", "orderTableDB SI EXIStee: $orderTableDB")
                         val orderItemIndexEncontrado: Int? =
                             orderTableDB.buscarSiOrderItemExiste(orderItem)
                         if (orderItemIndexEncontrado != null) {
                             orderTableDB.anularOrderItemYActualizar(
                                 orderItemIndexEncontrado,
                                 idEmpleadoQueAnula,
                                 motivoAnulacion
                             )
                             transaction.set(sfDocRef, table)
                         } else {
                             Log.d("TAG--", "orderItem no existíaaa ")
                         }
                         Log.d("TAG--", "table quedó como: $table")
                     }
                 } else {
                     throw Exception("ERROR agregarPedidoAMesa : table no encontrada")
                 }
                 null
                 //success
             }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
                 .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
         }

     suspend fun addCommentKitchenToOrderItem(
         tableID: String,
         orderItem: OrderItem,
         comments: String
     ) = coroutineScope {
         val sfDocRef = db.collection("tables").document(tableID)
         db.runTransaction { transaction ->
             val snapshot = transaction.get(sfDocRef)
             val table = snapshot.toObject<Table>()
             if (table != null) {
                 val orderTableDB: @RawValue OrderTable? = table.orderTable
                 if (orderTableDB != null) {
                     Log.d("TAG--", "orderTableDB SI EXIStee: $orderTableDB")
                     val orderItemIndexEncontrado: Int? =
                         orderTableDB.buscarSiOrderItemExiste(orderItem)
                     if (orderItemIndexEncontrado != null) {
                         orderTableDB.setCommentForOrderItem(orderItemIndexEncontrado, comments)
                         transaction.set(sfDocRef, table)
                     } else {
                         Log.d("TAG--", "orderItem no existíaaa ")
                     }
                     Log.d("TAG--", "table quedó como: $table")
                 }
             } else {
                 throw Exception("ERROR agregarPedidoAMesa : table no encontrada")
             }
             null
             //success
         }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
             .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
     }

     private suspend fun setOneOrderItemStateInDB(
         orderItem: OrderItem,
         orderTable: OrderTable,
         state: OrderItemState
     ) = coroutineScope {
         val sfDocRef = db.collection("tables").document(orderTable.tableID)
         db.runTransaction { transaction ->
             val snapshot = transaction.get(sfDocRef)
             val table = snapshot.toObject<Table>()
             Log.d("TAG--", "Table encontrada: $table")
             if (table != null) {
                 val orderTableDB: @RawValue OrderTable? = table.orderTable
                 if (orderTableDB != null) {
                     Log.d("TAG--", "orderTableDB SI EXIStee: $orderTableDB")
                     val orderItemIndexEncontrado: Int? =
                         orderTableDB.buscarSiOrderItemExiste(orderItem)
                     if (orderItemIndexEncontrado != null) {
                         orderTableDB.setStateToOrderItem(state, orderItemIndexEncontrado)
                         transaction.set(sfDocRef, table)
                     } else {
                         Log.d("TAG--", "orderItem no existíaaa ")
                     }
                     Log.d("TAG--", "table quedó como: $table")
                 }
             } else {
                 throw Exception("ERROR agregarPedidoAMesa : table no encontrada")
             }
             null
             //success
         }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
             .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
     }

     suspend fun setOneOrderItemAsAccepted(orderTable: OrderTable, orderItem: OrderItem) =
         coroutineScope {
             setOneOrderItemStateInDB(orderItem, orderTable, OrderItemState.PREPARANDO)
         }

     suspend fun setOneOrderItemAsServed(orderTable: OrderTable, orderItem: OrderItem) =
         coroutineScope {
             setOneOrderItemStateInDB(orderItem, orderTable, OrderItemState.SERVIDO)
         }


     suspend fun setOrdersItemsAsAccepted(tableID: String) = coroutineScope {
         val sfDocRef = db.collection("tables").document(tableID)
         db.runTransaction { transaction ->
             val snapshot = transaction.get(sfDocRef)
             val table = snapshot.toObject<Table>()
             Log.d("TAG--", "Table encontrada: $table")
             if (table != null) {
                 val orderTableDB: @RawValue OrderTable? = table.orderTable
                 if (orderTableDB != null) {
                     Log.d("TAG--", "orderTableDB SI EXIStee: $orderTableDB")
                     table.setOrdersItemsEsperandoAceptacionAEstadoPreparando()
                     transaction.set(sfDocRef, table)
                     Log.d("TAG--", "table quedó como: $table")
                 }
             } else {
                 throw Exception("ERROR setOrdersItemsAsAccepted : table no encontrada")
             }
             null
             //success
         }.addOnSuccessListener { Log.d("TAG--", "Transaction setOrdersItemsAsAccepted success!") }
             .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
     }

     @ExperimentalCoroutinesApi
     fun getActiveOrdersRealTime(ownerID: String): Flow<MutableList<Order>?> = callbackFlow {

         var eventsCollection: Query? = null
         try {
             eventsCollection = tablesCollection.whereEqualTo("ownerID", ownerID)
         } catch (e: Throwable) {
             close(e)
         }
         val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
             if (snapshot == null) {
                 return@addSnapshotListener
             }
             try {
                 if (isActive) {
                     val tables = snapshot.toObjects<Table>().toMutableList()
                     val listaDeOrders: MutableList<Order> = ArrayList()
                     for (table in tables) {
                         if (table.orderTable != null && table.state == TableState.OPEN) {
                             listaDeOrders.add(table.orderTable!!)
                         }
                     }

                     //TODO TODO agregar OrdersDelivery dsps
                     offer(listaDeOrders)
                 }
             } catch (e: Throwable) {
                 throw e
             }
         }
         awaitClose { subscription?.remove() }

     }*/

    @ExperimentalCoroutinesApi
    fun getTableDBRealTime(table: Table): Flow<@RawValue Table?> = callbackFlow {
        var eventsCollection: DocumentReference? = null
        try {
            eventsCollection = tablesCollection.document(table.id)
        } catch (e: Throwable) {
            close(e)
        }
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) {
                return@addSnapshotListener
            }
            try {
                if (isActive) {
                    val result = snapshot.toObject<Table>()
                    offer(result)
                }
            } catch (e: Throwable) {
                throw e
            }
        }
        awaitClose { subscription?.remove() }
    }

    /* @ExperimentalCoroutinesApi
     fun loadOrderTable(orderTable: OrderTable): Flow<Order?> = callbackFlow {

         var eventsCollection: DocumentReference? = null
         try {
             eventsCollection = tablesCollection.document(orderTable.tableID)
         } catch (e: Throwable) {
             close(e)
         }
         val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
             if (snapshot == null) {
                 return@addSnapshotListener
             }
             try {
                 if (isActive) {
                     val table = snapshot.toObject<Table>()
                     if (table != null) {

                         offer(table.orderTable)

                     } else {
                         throw Exception("loadOrderTablePayment:table no existe:")
                     }

                 }
             } catch (e: Throwable) {
                 throw e
             }
         }
         awaitClose { subscription?.remove() }

     }


     suspend fun closeOrderTable(
         orderTable: OrderTable,
         inputNroComprobante: String,
         userQueCierra: Empleado,
         venta: Venta,

         ): MutableLiveData<Boolean> =
         coroutineScope {
             val response = MutableLiveData<Boolean>()
             val sfDocRef = db.collection("tables").document(orderTable.tableID)
             db.runTransaction { transaction ->
                 val snapshot = transaction.get(sfDocRef)
                 val table = snapshot.toObject<Table>()
                 if (table != null) {
                     val orderTableDB: @RawValue OrderTable? = table.orderTable
                     if (orderTableDB != null) {
                         try {
                             val orderCerrada = table.cerrarMesaYRestablecer(
                                 userQueCierra.uid,
                                 inputNroComprobante
                             )
                             venta.agregarOrderYActualizarValores(orderCerrada)
                             repoDBMovimientos.registrarVentaYCerrarla(venta)
                             repoDBOrders.saveClosedOrderEnDB(orderCerrada)
                             transaction.set(sfDocRef, table)
                         } catch (e: Exception) {
                             throw e
                         }

                     } else {
                         throw Exception("ERROR createPagoParaOrderTable : OrderTable no encontrada")
                     }
                 } else {
                     throw Exception("ERROR createPagoParaOrderTable : table no encontrada")
                 }
                 null
                 //success
             }.addOnSuccessListener {
                 Log.d("TAG--", "Transaction success!")
                 response.value = true
             }
                 .addOnFailureListener { e ->
                     Log.d("TAG--", "Transaction failure. ${e.message}")
                     response.value = false
                 }
             return@coroutineScope response
         }

     suspend fun createPagoParaOrderTable(orderTable: OrderTable, pagoCreado: Pago)
             : MutableLiveData<Boolean> = coroutineScope {
         val response = MutableLiveData<Boolean>()
         val sfDocRef = db.collection("tables").document(orderTable.tableID)
         db.runTransaction { transaction ->
             val snapshot = transaction.get(sfDocRef)
             val table = snapshot.toObject<Table>()
             if (table != null) {
                 val orderTableDB: @RawValue OrderTable? = table.orderTable
                 if (orderTableDB != null) {
                     table.crearPagoVerificandoImportesCoincidan(pagoCreado)

                     //actualizo ultimoNroBoleta|
                     if (pagoCreado.nroComprobante.isNotBlank()) {
                         repoDBRestaurantes.updateUltimoNroBoletaDeSucursal(
                             orderTableDB.restauranteID, orderTableDB.sucursalID,
                             pagoCreado.nroComprobante
                         )
                     }


                     transaction.set(sfDocRef, table)
                     //TODO TODO TODO AGREGAR A REPOPAGOS TMB
                 } else {
                     throw Exception("ERROR createPagoParaOrderTable : OrderTable no encontrada")
                 }
             } else {
                 throw Exception("ERROR createPagoParaOrderTable : table no encontrada")
             }
             null
             //success
         }.addOnSuccessListener {
             Log.d("TAG--", "Transaction success!")
             response.value = true
         }
             .addOnFailureListener { e ->
                 Log.d("TAG--", "Transaction failure. ${e.message}")
                 response.value = false
             }
         return@coroutineScope response
     }

     suspend fun enviarPedidosACocinaParaOrderTable(orderTable: OrderTable) = coroutineScope {
         val sfDocRef = db.collection("tables").document(orderTable.tableID)
         db.runTransaction { transaction ->
             val snapshot = transaction.get(sfDocRef)
             val table = snapshot.toObject<Table>()
             if (table != null) {
                 table.enviarOrdersItemsParaAceptacion()
                 transaction.set(sfDocRef, table)
             } else {
                 throw Exception("ERROR enviarPedidosACocinaParaOrderTable : table no encontrada")
             }
             null
             //success
         }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
             .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
     }

     suspend fun removerOrderItemPorCompleto(orderTable: OrderTable, orderItem: OrderItem) =
         coroutineScope {
             val sfDocRef = db.collection("tables").document(orderTable.tableID)
             db.runTransaction { transaction ->
                 val snapshot = transaction.get(sfDocRef)
                 val table = snapshot.toObject<Table>()
                 if (table != null) {
                     val orderTableDB: @RawValue OrderTable? = table.orderTable
                     if (orderTableDB != null) {
                         table.removerOrderItemCompletoSiExisteYMozoPuedeBorrar(orderItem)
                         transaction.set(sfDocRef, table)
                     } else {
                         throw Exception("ERROR removerOrderItemPorCompleto : OrderTable no encontrada")
                     }
                 } else {
                     throw Exception("ERROR removerOrderItemPorCompleto : table no encontrada")
                 }
                 null
                 //success
             }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
                 .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
         }

     suspend fun removerProductoDeOrderTable(orderTable: OrderTable, product: Product) =
         coroutineScope {
             val sfDocRef = db.collection("tables").document(orderTable.tableID)
             db.runTransaction { transaction ->
                 val snapshot = transaction.get(sfDocRef)
                 val table = snapshot.toObject<Table>()
                 if (table != null) {
                     val orderTableDB: @RawValue OrderTable? = table.orderTable
                     if (orderTableDB != null) {
                         table.removeOrderItemBuscandoSiYaExiste(product)
                         transaction.set(sfDocRef, table)
                     } else {
                         throw Exception("ERROR removerProductoDeMesa : orderTable no encontrada")
                     }
                 } else {
                     throw Exception("ERROR removerProductoDeMesa : table no encontrada")
                 }
                 null
                 //success
             }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
                 .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }

         }

     suspend fun agregarProductoATable(
         table: Table,
         product: Product,
         restauranteID: String,
         sucursalID: String
     ) = coroutineScope {
         val sfDocRef = db.collection("tables").document(table.id)
         db.runTransaction { transaction ->
             val snapshot = transaction.get(sfDocRef)
             val table = snapshot.toObject<Table>()
             if (table != null) {
                 val orderTableDB: @RawValue OrderTable? = table.orderTable
                 if (orderTableDB == null) {
                     val currentUserID = repoDBUsers.getCurrentUserUIDNORMAL()
                     table.crearOrderTable(currentUserID, getRandomID(), restauranteID, sucursalID)
                 }
                 table.addOrderItemBuscandoSiYaExiste(product, getRandomID())
                 transaction.set(sfDocRef, table)
             } else {
                 throw Exception("ERROR agregarProductoATable : table no encontrada")
             }
             null
             //success
         }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
             .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
     }
 */


    //////  new changesssssssssssssssssssssss para abajo

    suspend fun addOrderTableIDToTable(table: Table, newOrderID: String) = coroutineScope {
        val sfDocRef = db.collection("tables").document(table.id)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)
            val table = snapshot.toObject<Table>()
            if (table != null) {
                table.addOrderTableID(newOrderID)
                transaction.set(sfDocRef, table)
            } else {
                throw Exception("ERROR agregarProductoATable : table no encontrada")
            }
            null
            //success
        }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
            .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
    }


}