package com.restofast.salon.sharedData.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.restofast.salon.entities.enums.CategoriaEgreso
import com.restofast.salon.entities.enums.CategoriaIngreso
import com.restofast.salon.entities.enums.TipoMovimiento
import com.restofast.salon.entities.enums.*
import com.restofast.salon.entities.owners.*
import com.restofast.salon.entities.products.CompraItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

class RepoDBMovimientos constructor(
    private var db: FirebaseFirestore,
    private var repoDBRestaurantes: RepoDBRestaurantes,
) {

    private val movimientosCollection: CollectionReference = db.collection("movimientos")


    suspend fun getActiveCompraIDDeUserSiExiste(
        userID: String,
        restauranteID: String,
        sucursalID: String
    ): String? {
        var compraIDEncontrada: String? = null
        val egresos = movimientosCollection.whereEqualTo("tipoMovimiento", TipoMovimiento.EGRESO)
            .get()
            .await()
            .toObjects<Egreso>()
            .toMutableList()
        val filtrados = egresos.filter { mov ->
            mov.userID == userID
            mov.restauranteID == restauranteID &&
                    mov.sucursalID == sucursalID &&
                    mov.active
        }.toMutableList()
        if (filtrados.isNotEmpty()) {
            val compra = getCompraSiExisteEnListaEgresos(filtrados)
            if (compra != null) {
                compraIDEncontrada = compra.id
            }
        }
        return compraIDEncontrada

    }

    private fun getCompraSiExisteEnListaEgresos(listaEgresos: MutableList<Egreso>): Egreso? {
        var i = 0
        var encontrado: Egreso? = null
        while (i < listaEgresos.size && encontrado == null) {
            if (listaEgresos[i].categoria == CategoriaEgreso.COMPRA) {
                encontrado = listaEgresos[i]
            }
            i++
        }
        return encontrado
    }

    @ExperimentalCoroutinesApi
    fun getActiveCompra(activeCompraID: String): Flow<Compra?> =
        callbackFlow {

            var eventsCollection: DocumentReference? = null
            try {
                eventsCollection =
                    movimientosCollection.document(activeCompraID)
            } catch (e: Throwable) {
                close(e)
            }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    if (isActive) {

                        val compra = snapshot.toObject<Compra>()
                        offer(compra)

                    }
                } catch (e: Throwable) {
                    throw e
                }
            }
            awaitClose { subscription?.remove() }
        }

    @ExperimentalCoroutinesApi
    fun getMovimientosBySucursalIDRealTime(
        sucursalID: String,
        listadeIDSRestaurantes: MutableList<String>
    ): Flow<MutableList<Movimiento>?> =
        callbackFlow {

            var eventsCollection: Query? = null
            try {
                eventsCollection = movimientosCollection.whereEqualTo("sucursalID", sucursalID)
            } catch (e: Throwable) {
                close(e)
            }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    if (isActive) {

                        val documentos = snapshot.documents
                        val listaMovs: MutableList<Movimiento> = ArrayList()
                        for (doc in documentos) {

                            val movimiento = doc.toObject<Movimiento>()
                            if (movimiento != null && !movimiento.active && listadeIDSRestaurantes.contains(
                                    movimiento.restauranteID
                                )
                            ) {
                                when (movimiento.tipoMovimiento) {
                                    TipoMovimiento.INGRESO -> {
                                        val ingreso = doc.toObject<Ingreso>()
                                        if (ingreso != null) {
                                            if (ingreso.categoria == CategoriaIngreso.VENTA) {
                                                val venta = doc.toObject<Venta>()
                                                if (venta != null) {
                                                    listaMovs.add(venta)
                                                }
                                            } else {
                                                listaMovs.add(ingreso)
                                            }
                                        }
                                    }
                                    TipoMovimiento.EGRESO -> {
                                        val egreso = doc.toObject<Egreso>()
                                        if (egreso != null) {
                                            if (egreso.categoria == CategoriaEgreso.COMPRA) {
                                                val compra = doc.toObject<Compra>()
                                                if (compra != null) {
                                                    listaMovs.add(compra)
                                                }
                                            } else {
                                                listaMovs.add(egreso)
                                            }
                                        }
                                    }
                                    TipoMovimiento.VENTA -> {
                                        val venta = doc.toObject<Venta>()
                                        if (venta != null) {
                                            listaMovs.add(venta)
                                        }
                                    }

                                    else -> listaMovs.add(movimiento)
                                }


                            }
                        }
                        offer(listaMovs)
                    }
                } catch (e: Throwable) {
                    throw e
                }
            }
            awaitClose { subscription?.remove() }
        }

    suspend fun getEgresoByID(id: String): Egreso? {
        return movimientosCollection.document(id).get().await().toObject<Egreso>()
    }

    suspend fun getCompraById(id: String): Compra? {
        return movimientosCollection.document(id).get().await().toObject<Compra>()
    }

    suspend fun getIngresoByID(id: String): Ingreso? {
        return movimientosCollection.document(id).get().await().toObject<Ingreso>()
    }

    suspend fun getVentaById(id: String): Venta? {
        return movimientosCollection.document(id).get().await().toObject<Venta>()
    }


    fun getRandomID(): String {
        return movimientosCollection.document().id
    }

    suspend fun updateConceptoACompra(compra: Compra, conceptoActualizado: String) =
        coroutineScope {
            val sfDocRef = movimientosCollection.document(compra.id)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(sfDocRef)
                val compra = snapshot.toObject<Compra>()
                if (compra != null) {
                    compra.concepto = conceptoActualizado
                    transaction.set(sfDocRef, compra)
                } else {
                    throw Exception("createNewCompraItemAMovimientoCompra: NO existia compra")
                }

                null
                //success
            }.addOnSuccessListener {
                Log.d("TAG--", "Transaction updateMovimiento success!")

            }.addOnFailureListener { e ->
                Log.w("TAG--", "Transaction updateMovimiento failure.", e)
            }
        }


    suspend fun updateCuentaParaDescontarACompra(compra: Compra, cuentaParaDescontar: Cuenta) =
        coroutineScope {
            val sfDocRef = movimientosCollection.document(compra.id)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(sfDocRef)
                val compra = snapshot.toObject<Compra>()
                if (compra != null) {
                    compra.cuentaUtilizada = cuentaParaDescontar
                    transaction.set(sfDocRef, compra)
                } else {
                    throw Exception("updateCuentaParaDescontarACompra: NO existia compra")
                }

                null
                //success
            }.addOnSuccessListener {
                Log.d("TAG--", "Transaction updateCuentaParaDescontarACompra success!")

            }.addOnFailureListener { e ->
                Log.w("TAG--", "Transaction updateCuentaParaDescontarACompra failure.", e)
            }
        }


    suspend fun createNewCompraItemACompra(movimiento: Movimiento, compraItem: CompraItem) =
        coroutineScope {
            val sfDocRef = movimientosCollection.document(movimiento.id)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(sfDocRef)
                val compra = snapshot.toObject<Compra>()
                if (compra != null) {

                    compra.agregarCompraItemYActualizarValores(compraItem)

                    transaction.set(sfDocRef, compra)
                } else {
                    throw Exception("createNewCompraItemAMovimientoCompra: NO existia compra")
                }
                null
                //success
            }.addOnSuccessListener {
                Log.d("TAG--", "Transaction updateMovimiento success!")

            }.addOnFailureListener { e ->
                Log.w("TAG--", "Transaction updateMovimiento failure.", e)
            }
        }

    suspend fun updateCompraItem(
        compraID: String,
        compraItemID: String,
        nombreProducto: String,
        cantidad: Int,
        medida: UnitOfMeasurement,
        importe: String
    ) =
        coroutineScope {
            val sfDocRef = movimientosCollection.document(compraID)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(sfDocRef)
                val compra = snapshot.toObject<Compra>()
                if (compra != null) {
                    val compraItemActualizado = compra.updateCompraItemYActualizarValores(
                        compraItemID,
                        nombreProducto,
                        cantidad,
                        medida,
                        importe
                    )
                    if (compraItemActualizado != null) {
                        transaction.set(sfDocRef, compra)
                    } else {
                        throw Exception("updateCompraItem: NO se pudo actuañlizar compraItem")
                    }
                } else {
                    throw Exception("updateCompraItem: NO existia compra")
                }
                null
                //success
            }.addOnSuccessListener {
                Log.d("TAG--", "Transaction updateCompraItem success!")

            }.addOnFailureListener { e ->
                Log.w("TAG--", "Transaction updateCompraItem failure.", e)
            }
        }

    suspend fun removeCompraItemDeCompra(compraID: String, compraItemID: String) =
        coroutineScope {
            val sfDocRef = movimientosCollection.document(compraID)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(sfDocRef)
                val compra = snapshot.toObject<Compra>()
                if (compra != null) {
                    val compraItemEliminada =
                        compra.removerCompraItemSiExisteYActualizarImporte(compraItemID)
                    if (compraItemEliminada != null) {
                        transaction.set(sfDocRef, compra)
                    } else {
                        throw Exception("removeCompraItemDeCompra: NO se pudo eliminar compraItem")
                    }
                } else {
                    throw Exception("removeCompraItemDeCompra: NO existia compra")
                }
                null
                //success
            }.addOnSuccessListener {
                Log.d("TAG--", "Transaction removeCompraItemDeCompra success!")

            }.addOnFailureListener { e ->
                Log.w("TAG--", "Transaction removeCompraItemDeCompra failure.", e)
            }
        }

    suspend fun createMovimientoEnDB(movimiento: Movimiento): Boolean = coroutineScope {
        try {
            movimientosCollection.document(movimiento.id).set(movimiento).await()
            return@coroutineScope true
        } catch (e: Exception) {
            return@coroutineScope false
        }

    }

    suspend fun registrarYCerrarEgreso(egreso: Egreso) =
        coroutineScope {
            val sfDocRef = movimientosCollection.document(egreso.id)
            db.runTransaction { transaction ->


                //actualizo en RESTAURANTE SUCURSAL DISPONIBLE

                repoDBRestaurantes.generateEgreso(egreso)
                transaction.set(sfDocRef, egreso)



                null
                //success
            }.addOnSuccessListener {
                Log.d("TAG--", "Transaction registrarYCerrarEgreso success!")
                return@addOnSuccessListener

            }.addOnFailureListener { e ->
                Log.w("TAG--", "Transaction registrarYCerrarEgreso failure.", e)
            }
        }

    suspend fun registrarYCerrarIngreso(ingreso: Ingreso) =
        coroutineScope {
            val sfDocRef = movimientosCollection.document(ingreso.id)
            db.runTransaction { transaction ->

                //actualizo en RESTAURANTE SUCURSAL DISPONIBLE

                repoDBRestaurantes.generateIngreso(ingreso)
                transaction.set(sfDocRef, ingreso)

                null
                //success
            }.addOnSuccessListener {
                Log.d("TAG--", "Transaction registrarYCerrarIngreso success!")
                return@addOnSuccessListener

            }.addOnFailureListener { e ->
                Log.w("TAG--", "Transaction registrarYCerrarIngreso failure.", e)
            }
        }


    fun registrarVentaYCerrarla(venta: Venta) {
        val sfDocRef = movimientosCollection.document(venta.id)
        db.runTransaction { transaction ->


            // NO GENERAR INGRESO SINO GENRAR UNA VENTA DEBE TENER LOGICA DIFERENTE

            val listaMovimientos = venta.listaMovimientos
            for (movimiento in listaMovimientos) {
                if (movimiento is Ingreso) {
                    repoDBRestaurantes.generateIngreso(movimiento)
                }
            }


            //ACA LO SUBO COMO MOVIMIENTO
            transaction.set(sfDocRef, venta)

            null
            //success
        }.addOnSuccessListener {
            Log.d("TAG--", "Transaction registrarVentaYCerrarla success!")
            return@addOnSuccessListener

        }.addOnFailureListener { e ->
            Log.w("TAG--", "Transaction registrarVentaYCerrarla failure.", e)
        }
    }

    suspend fun registrarYCerrarCompra(compra: Compra) =
        coroutineScope {
            val sfDocRef = movimientosCollection.document(compra.id)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(sfDocRef)
                val compra = snapshot.toObject<Compra>()
                if (compra != null) {

                    //ACTUALIZO COMPRA
                    compra.cerrarCompraYActualizar()
                    //actualizo en RESTAURANTE SUCURSAL DISPONIBLE

                    repoDBRestaurantes.generateEgreso(compra)
                    transaction.set(sfDocRef, compra)


                } else {
                    throw Exception("registrarYCerrarCompra: NO existia compra")
                }
                null
                //success
            }.addOnSuccessListener {
                Log.d("TAG--", "Transaction registrarYCerrarCompra success!")

            }.addOnFailureListener { e ->
                Log.w("TAG--", "Transaction registrarYCerrarCompra failure.", e)
            }
        }

    suspend fun getMovimientoOneTime(idMovimiento: String): Movimiento {
        val movimiento =
            movimientosCollection.document(idMovimiento).get().await().toObject<Movimiento>()
                ?: throw Exception("getMovimientoOneTime: Movimiento no encontrado")
        when (movimiento.tipoMovimiento) {
            TipoMovimiento.EGRESO -> {
                val egreso =
                    movimientosCollection.document(idMovimiento).get().await().toObject<Egreso>()
                        ?: throw Exception("getMovimientoOneTime: Egreso no encontrado")
                if (egreso.categoria == CategoriaEgreso.COMPRA) {
                    return movimientosCollection.document(idMovimiento).get().await()
                        .toObject<Compra>()
                        ?: throw Exception("getMovimientoOneTime: Compra no encontrado")
                } else {
                    return egreso
                }
            }
            TipoMovimiento.INGRESO -> {
                val ingreso =
                    movimientosCollection.document(idMovimiento).get().await().toObject<Ingreso>()
                        ?: throw Exception("getMovimientoOneTime: Ingreso no encontrado")
                return ingreso
            }
            TipoMovimiento.VENTA -> {
                val venta =
                    movimientosCollection.document(idMovimiento).get().await().toObject<Venta>()
                        ?: throw Exception("getMovimientoOneTime: Venta no encontrado")
                return venta
            }

            else -> throw Exception("getMovimientoOneTime: Movimiento no es tipo válido")
        }
    }


}