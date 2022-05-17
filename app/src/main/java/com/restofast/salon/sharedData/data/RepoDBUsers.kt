package com.restofast.salon.sharedData.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.restofast.salon.entities.persons.Empleado
import com.restofast.salon.entities.persons.Owner
import com.restofast.salon.sharedData.domain.entities.UserModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await

class RepoDBUsers(
    private var auth: FirebaseAuth,
    private var db: FirebaseFirestore

) {

    //private val db = Firebase.firestore
    private val usersCollection: CollectionReference = db.collection("users")


    suspend fun createUserWithEmailAndPassword(email: String, password: String): String {
        return auth.createUserWithEmailAndPassword(email, password).await().user?.uid!!
    }

    suspend fun createNewUserInDatabase(userModel: UserModel): Boolean {
        usersCollection.document(userModel.uid).set(userModel).await()
        return coroutineReturnBoolean(true)
    }

    private suspend fun coroutineReturnBoolean(boolean: Boolean): Boolean = coroutineScope {
        return@coroutineScope boolean
    }

    suspend fun userIsAuthenticated(): Boolean = coroutineScope {
        return@coroutineScope auth.currentUser != null
    }

    suspend fun signOut() = coroutineScope {
        auth.signOut()
    }

    suspend fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun getCurrentUserUID(): String = coroutineScope {
        return@coroutineScope auth.currentUser?.uid!!
    }

    fun getCurrentUserUIDNORMAL(): String {
        return auth.currentUser?.uid!!
    }

    suspend fun getUserFromDBByUID(currentUserUID: String): Empleado {
            return usersCollection.document(currentUserUID).get().await().toObject<Empleado>()
                ?: throw Exception("ERROR getUserFromDBByUID Empleado De Firestore")
    }

    suspend fun getOwnerFromDBByUID(currentUserUID: String): Owner {
        return usersCollection.document(currentUserUID).get().await().toObject<Owner>()
            ?: throw Exception("ERROR getOwnerFromDBByUID Owner De Firestore")
    }

    @ExperimentalCoroutinesApi
    fun getUserFromDBByUIDRealTime(currentUserUID: String): Flow<Empleado?> =
        callbackFlow {
            var eventsCollection: DocumentReference? = null
            try {
                eventsCollection = usersCollection.document(currentUserUID)
            } catch (e: Throwable) {
                close(e)
            }
            val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }
                try {
                    if (isActive) {
                        val empleado = snapshot.toObject<Empleado>()
                        if (empleado != null) {
                                offer(empleado)

                        } else {
                            throw Exception("ERROR getUserByUIDFromDatabaseConRoleTypeSegunConfigAppRealTime EMPLEADO no encontrado")
                        }
                    }
                } catch (e: Throwable) {
                    throw e
                }
            }
            awaitClose { subscription?.remove() }
        }


    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun getRestaurantYSucursalIDDeCurrentEmpleado(): String = coroutineScope {
        val empleado =
            usersCollection.document(getCurrentUserUID()).get().await().toObject<Empleado>()
        if (empleado != null) {
            return@coroutineScope empleado.obtenerSucursalRestaurantID()
        } else {
            throw Exception("ERROR getSegunRestaurantYSucursalID EMPLEADO no encontrado")
        }
    }

    suspend fun getUserModelByID(id: String): UserModel {
        return usersCollection.document(id).get().await().toObject<UserModel>()
            ?: throw Exception("ERROR getUserModelByID User no encontrado")
    }


    ////////////////////////////// NUEVOSSSSSSSSSSSSSSSSSSSSSSS
    suspend fun getRestaurantIDDeCurrentEmpleado(): String = coroutineScope {
        val empleado =
            usersCollection.document(getCurrentUserUID()).get().await().toObject<Empleado>()
        if (empleado != null) {
            return@coroutineScope empleado.restauranteID
        } else {
            throw Exception("ERROR getRestaurantIDDeCurrentEmpleado")
        }
    }
    suspend fun getSucursalIDDeCurrentEmpleado(): String = coroutineScope {
        val empleado =
            usersCollection.document(getCurrentUserUID()).get().await().toObject<Empleado>()
        if (empleado != null) {
            return@coroutineScope empleado.sucursalID
        } else {
            throw Exception("ERROR getRestaurantIDDeCurrentEmpleado")
        }
    }

}