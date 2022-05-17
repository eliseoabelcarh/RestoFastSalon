package com.restofast.salon.sharedData.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.restofast.salon.entities.enums.TipoCuenta
import com.restofast.salon.entities.enums.TipoFormaDePago
import com.restofast.salon.entities.owners.*
import com.restofast.salon.entities.products.CategoriaProducto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await

class RepoDBRestaurantes constructor(
    private var db: FirebaseFirestore,
) {

    private val restaurantesCollection: CollectionReference = db.collection("restaurantes")

    @ExperimentalCoroutinesApi
    fun getSucursalRealTime(sucursal: SucursalRest): Flow<SucursalRest?> =
        callbackFlow {
            var eventsCollection: DocumentReference? = null
            try {
                eventsCollection = restaurantesCollection.document(sucursal.restauranteID)
            } catch (e: Throwable) {
                close(e)
            }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    if (isActive) {
                        val restaurante = snapshot.toObject<Restaurante>()
                            ?: throw Exception("getSucursalRealTime: Error cargando restaurante")
                        val sucursal = restaurante.getSucursalByID(sucursal.id)
                            ?: throw Exception("getSucursalRealTime: Error obteniendo sucursal")
                        offer(sucursal)
                    }
                } catch (e: Throwable) {
                    throw e
                }
            }
            awaitClose { subscription?.remove() }
        }

    @ExperimentalCoroutinesApi
    fun getSucursalesByOwnerIDRealTime(ownerID: String): Flow<MutableList<SucursalRest>?> =
        callbackFlow {
            // createRestaurante()
            var eventsCollection: Query? = null
            try {
                eventsCollection = restaurantesCollection.whereArrayContains("owners", ownerID)
            } catch (e: Throwable) {
                close(e)
            }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    if (isActive) {
                        val listaDeSucursales: MutableList<SucursalRest> = ArrayList()
                        val restaurantes = snapshot.toObjects<Restaurante>().toMutableList()
                        for (rest in restaurantes) {
                            listaDeSucursales.addAll(rest.listaSucursales)
                        }
                        offer(listaDeSucursales)
                    }
                } catch (e: Throwable) {
                    throw e
                }
            }
            awaitClose { subscription?.remove() }
        }

    fun generateEgreso(egreso: Egreso) {
        val sfDocRef = restaurantesCollection.document(egreso.restauranteID)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)
            val restaurante = snapshot.toObject<Restaurante>()
            if (restaurante != null) {

                restaurante.updateDisponibleParaEgreso(egreso)
                transaction.set(sfDocRef, restaurante)


            } else {
                throw Exception("generateEgreso: NO existia restaurante")
            }
            null
            //success
        }.addOnSuccessListener {
            Log.d("TAG--", "Transaction generateEgreso success!")

        }.addOnFailureListener { e ->
            Log.w("TAG--", "Transaction generateEgreso failure.", e)
        }
    }

    fun generateIngreso(ingreso: Ingreso) {
        val sfDocRef = restaurantesCollection.document(ingreso.restauranteID)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)
            val restaurante = snapshot.toObject<Restaurante>()
            if (restaurante != null) {

                restaurante.updateDisponibleParaIngreso(ingreso)

                //OJOOOO NO DEBE ACTUALIZAR TODOOEL RESTARAUNTE SOLO LO NECESARIO
                transaction.set(sfDocRef, restaurante)


            } else {
                throw Exception("generateIngreso: NO existia restaurante")
            }
            null
            //success
        }.addOnSuccessListener {
            Log.d("TAG--", "Transaction generateIngreso success!")

        }.addOnFailureListener { e ->
            Log.w("TAG--", "Transaction generateIngreso failure.", e)
        }
    }


    fun getRandomID(): String {
        return restaurantesCollection.document().id
    }


    //CREANDO RESTAURANTE DEFAULT PARA USAR LUEGO
    fun createRestaurante() {
        val listaDeCuentas: MutableList<Cuenta> = ArrayList()

        val ctaCaja = Cuenta(
            getRandomID(),
            "caja",
            TipoCuenta.CAJA,
            "Soles",
            "S/.",
            "2000",
            false
        )
        ctaCaja.tipoFormasDePagoAceptadas.add(TipoFormaDePago.EFECTIVO)


        val ctaBanco1 = Cuenta(
            getRandomID(),
            "Banco Interbank",
            TipoCuenta.BANCO,
            "Soles",
            "S/.",
            "3000",
            false
        )
        ctaBanco1.tipoFormasDePagoAceptadas.add(TipoFormaDePago.VENDEMAS)


        listaDeCuentas.add(ctaCaja)
        listaDeCuentas.add(ctaBanco1)


        val categoria1 = CategoriaProducto(getRandomID(),"Pollo",false)
        val categoria2 = CategoriaProducto(getRandomID(),"Bebida",true)
        val categoria3 = CategoriaProducto(getRandomID(),"Envase",true)
        val listaDeCategoriasDeProducto: MutableList<CategoriaProducto> = ArrayList()
        listaDeCategoriasDeProducto.add(categoria1)
        listaDeCategoriasDeProducto.add(categoria2)
        listaDeCategoriasDeProducto.add(categoria3)

        val listaSucursales: MutableList<SucursalRest> = ArrayList()
        val suc1 = SucursalRest(
            "COMAS1",
            "Comas 1",
            "Los Angeles 328",
            "Chifa Funny Chen",
            "CHIFA_FUNNY_CHEN",
            "5000",
            0,
            0,
            0,
            "0.04",
            "0.28",
            "0.25",
            "0.20",
            listaDeCuentas,
            listaDeCategoriasDeProducto
        )
        listaSucursales.add(suc1)


        val listaDeDuenios: MutableList<String> = ArrayList()
        listaDeDuenios.add("qaD02N3nnWgTIHoTgaq9VuVOsij2")
        val restaurante = Restaurante(
            "CHIFA_FUNNY_CHEN",
            "Chifa Funny Chen",
            listaDeDuenios,
            listaSucursales
        )

        restaurantesCollection.document(restaurante.id).set(restaurante)
    }

    suspend fun getCuentaUtilizadaSegunTipoFormaPago(
        tipoFormaDePago: TipoFormaDePago,
        restauranteID: String,
        sucursalID: String
    ): Cuenta {
        val restaurante =
            restaurantesCollection.document(restauranteID).get().await().toObject<Restaurante>()
                ?: throw Exception("ERROR getCuentaUtilizadaSegunTipoFormaPago Restaurante no encontrado")
        val sucursal = restaurante.getSucursalByID(sucursalID)
            ?: throw Exception("ERROR getCuentaUtilizadaSegunTipoFormaPago Sucursal no encontrado")

        return sucursal.getCuentaParaAcreditarSegunTipoFormaDePago(tipoFormaDePago)
            ?: throw Exception(
                "ERROR getCuentaUtilizadaSegunTipoFormaPago Cuengta no encontrado"
            )
    }

    suspend fun getSucursalOneTime(restauranteID: String, sucursalID: String): SucursalRest? {
        val restaurante =
            restaurantesCollection.document(restauranteID).get().await().toObject<Restaurante>()
        return restaurante?.getSucursalByID(sucursalID)
    }

    fun updateUltimoNroBoletaDeSucursal(
        restauranteID: String,
        sucursalID: String,
        nroComprobante: String
    ) {
        val sfDocRef = restaurantesCollection.document(restauranteID)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)
            val restaurante = snapshot.toObject<Restaurante>()
                ?: throw Exception("updateUltimoNroBoletaDeSucursal : restaurante no encontrada")
            val sucursal = restaurante.getSucursalByID(sucursalID)
                ?: throw Exception("updateUltimoNroBoletaDeSucursal : sucursal no encontrada")
            sucursal.nroUltimaBoleta = nroComprobante.toInt()
            Log.d("TAG--", "UTLIMO NO BOLETA PAR SUCURAL: ${nroComprobante.toInt()}")
            transaction.set(sfDocRef, restaurante)
            null
            //success
        }.addOnSuccessListener { Log.d("TAG--", "Transaction success!") }
            .addOnFailureListener { e -> Log.w("TAG--", "Transaction failure.", e) }
    }


}