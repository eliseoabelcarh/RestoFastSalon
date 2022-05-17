package com.restofast.salon.sharedData.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RepoDBOrdersToGo constructor(

    private var db: FirebaseFirestore
) {

    private val ordersToGoCollection: CollectionReference = db.collection("ordersToGo")

    fun getRandomID(): String {
        return ordersToGoCollection.document().id
    }

 /*   fun createPedidoToGoEnDB(pedidoToGo: PedidoToGo): MutableLiveData<String?> {
        val observableOrderToGoIDCreado = MutableLiveData<String?>()
        val sfDocRef = ordersToGoCollection.document(ordertoGo.id)
        db.runTransaction { transaction ->
            transaction.set(sfDocRef, ordertoGo)
            null
            //success
        }.addOnSuccessListener {
            Log.d("TAG--", "Transaction saveClosedOrderEnDB success!")
            observableOrderToGoIDCreado.value = ordertoGo.id
        }
            .addOnFailureListener { e ->
                Log.w("TAG--", "Transaction saveClosedOrderEnDB failure.", e)
                observableOrderToGoIDCreado.value = null
            }

        return observableOrderToGoIDCreado
    }*/

   /* @ExperimentalCoroutinesApi
    fun getActiveOrdersToGoRealTime(
        restauranteID: String,
        sucursalID: String
    ): Flow<MutableList<OrderToGo>?> =
        callbackFlow {

            var eventsCollection: Query? = null
            try {
                eventsCollection =
                    ordersToGoCollection
                        .whereEqualTo("sucursalID", sucursalID)
                        .whereEqualTo("restauranteID", restauranteID)
                        .whereEqualTo("active",true)
            } catch (e: Throwable) {
                close(e)
            }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    if (isActive) {

                        val listaOrdersToGo = snapshot.toObjects<OrderToGo>().toMutableList()
                        offer(listaOrdersToGo)

                    }
                } catch (e: Throwable) {
                    throw e
                }
            }
            awaitClose { subscription?.remove() }
        }*/


}