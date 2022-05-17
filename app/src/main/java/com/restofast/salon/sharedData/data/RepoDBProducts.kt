package com.restofast.salon.sharedData.data


import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.restofast.salon.entities.enums.ProductType
import com.restofast.salon.entities.order.Table
import com.restofast.salon.entities.owners.Restaurante
import com.restofast.salon.entities.products.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await

class RepoDBProducts constructor(
    private var db: FirebaseFirestore,
) {
    private val productsCollection: CollectionReference = db.collection("products")


    suspend fun crearTodosProductosDeSucursalEnDB(){
        val listaProds = ProductosDeSucursal.listaProductos
        for(prod in listaProds){
            createProductoEnDatabase(prod)
        }
    }

    suspend fun createProductoEnDatabase(product: Product): Boolean {
        product.id = productsCollection.document().id
        productsCollection.document(product.id).set(product).await()
        return coroutineReturnBoolean(true)
    }

    private suspend fun coroutineReturnBoolean(boolean: Boolean) : Boolean = coroutineScope{
        return@coroutineScope boolean
    }


    @ExperimentalCoroutinesApi
    fun getPlatosFromDatabaseRealTime(ownerID: String): Flow<MutableList<Plato>> = callbackFlow {

        //USAR SOLO UNA VEZ PARA CREAR PRODCUTOS
     // crearTodosProductosDeSucursalEnDB()

        var eventsCollection : Query? = null
        try {
            eventsCollection = productsCollection
                .whereEqualTo("productType", ProductType.PLATO.name)
                //.orderBy("name")
        } catch (e: Throwable) {
            close(e)
        }
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                if(isActive) {
                    val result = snapshot.toObjects<Plato>().toMutableList()
                  //  Log.d("TAG--", "pepepepe: ${result}")
                    val listaFinal = result.filter { p -> p.ownerID == ownerID } as MutableList<Plato>
                    offer(listaFinal)
                }
            } catch (e: Throwable) {
                throw e
            }
        }
        awaitClose { subscription?.remove() }
    }
    @ExperimentalCoroutinesApi
    fun getBebidasFromDatabaseRealTime(ownerID: String): Flow<MutableList<Bebida>> = callbackFlow {
        var eventsCollection : Query? = null
        try {
            eventsCollection = productsCollection
                .whereEqualTo("productType", ProductType.BEBIDA.name)
              //  .orderBy("name")
        } catch (e: Throwable) {
            close(e)
        }
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                if(isActive) {
                    val result = snapshot.toObjects<Bebida>().toMutableList()
                    val listaFinal = result.filter { p -> p.ownerID == ownerID } as MutableList<Bebida>
                    //Log.d("TAG--", "vvpepepepe: ${listaFinal}")
                    offer(listaFinal)
                }
            } catch (e: Throwable) {
                throw e
            }
        }
        awaitClose { subscription?.remove() }
    }

    @ExperimentalCoroutinesApi
    fun getEnvasesFromDatabaseRealTime(ownerID: String): Flow<MutableList<Envase>> = callbackFlow {
        var eventsCollection : Query? = null
        try {
            eventsCollection = productsCollection
                .whereEqualTo("productType", ProductType.ENVASE_DESCARTABLE)
        } catch (e: Throwable) {
            close(e)
        }
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                if(isActive) {
                    val result = snapshot.toObjects<Envase>().toMutableList()
                    val listaFinal = result.filter { p -> p.ownerID == ownerID } as MutableList<Envase>
                    offer(listaFinal)
                }
            } catch (e: Throwable) {
                throw e
            }
        }
        awaitClose { subscription?.remove() }
    }


    @ExperimentalCoroutinesApi
    fun getProductosCartaBySucursal(restauranteID: String, sucursualID: String): Flow<MutableList<Product>> = callbackFlow {
      val codigo = "$restauranteID-$sucursualID"
        var eventsCollection : Query? = null
        try {
            eventsCollection = productsCollection
                .whereEqualTo("ownerID", codigo)
        } catch (e: Throwable) {
            close(e)
        }
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                if(isActive) {
                    val listaDeProductos : MutableList<Product> = ArrayList()

                    val documents = snapshot.documents

                    for (doc in documents){

                        val producto = doc.toObject<Product>()
                       /* when(producto?.productType){
                            ProductType.PLATO-> doc.toObject<Plato>()?.let{listaDeProductos.add(it)}
                            ProductType.BEBIDA-> doc.toObject<Bebida>()?.let{listaDeProductos.add(it)}
                            ProductType.ENVASE_DESCARTABLE-> doc.toObject<Envase>()?.let{listaDeProductos.add(it)}
                            ProductType.INGREDIENTE-> { }
                            ProductType.SIN_ASIGNAR-> { }
                        }*/
                    }

                    offer(listaDeProductos)


                }
            } catch (e: Throwable) {
                throw e
            }
        }
        awaitClose { subscription?.remove() }
    }


    //*////////////////// nuevossssssssssssssssss

   /* suspend fun getListaProductosByRestauranteYSucursal(restauranteID: String, sucursalID: String): MutableList<Product> {
        try {
            return productsCollection
                .whereEqualTo("restauranteID", restauranteID)
                .whereEqualTo("sucursalID", sucursalID)
                .get().await()
                .toObjects<Product>()
                .toMutableList()
        } catch (e: Exception) {
            throw Exception("ERROR getListaProductosByRestauranteYSucursal De Firestore: ${e.message}")
        }
    }*/
    @ExperimentalCoroutinesApi
    fun getListaProductosByRestauranteYSucursal(restauranteID: String, sucursalID: String): Flow<MutableList<Product>> = callbackFlow {
        var eventsCollection : Query? = null
        try {
            eventsCollection = productsCollection
                .whereEqualTo("restauranteID", restauranteID)
                .whereEqualTo("sucursalID", sucursalID)
        } catch (e: Throwable) {
            close(e)
        }
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) { return@addSnapshotListener }
            try {
                if(isActive) {
                    val result = snapshot.toObjects<Product>().toMutableList()
                    offer(result)
                }
            } catch (e: Throwable) {
                throw e
            }
        }
        awaitClose { subscription?.remove() }
    }

    suspend fun getProductoByID(id: String): Product? {
        try {
            return productsCollection.document(id)
                .get().await()
                .toObject<Product>()
        } catch (e: Exception) {
            throw Exception("ERROR getProductoByID De Firestore: ${e.message}")
        }
    }

}