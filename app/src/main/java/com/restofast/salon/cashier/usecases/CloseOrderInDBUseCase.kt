package com.restofast.salon.cashier.usecases

import androidx.lifecycle.MutableLiveData
import com.restofast.salon.entities.enums.CategoriaIngreso
import com.restofast.salon.entities.enums.OrderType
import com.restofast.salon.entities.enums.TipoMovimiento
import com.restofast.salon.entities.order.Order
import com.restofast.salon.entities.order.OrderTable
import com.restofast.salon.entities.owners.Cuenta
import com.restofast.salon.entities.owners.Ingreso
import com.restofast.salon.entities.owners.SucursalRest
import com.restofast.salon.entities.owners.Venta
import com.restofast.salon.entities.persons.Empleado
import com.restofast.salon.sharedData.data.RepoDBMovimientos
import com.restofast.salon.sharedData.data.RepoDBRestaurantes
import com.restofast.salon.sharedData.data.RepoDBTables
import com.restofast.salon.sharedData.data.RepoDBUsers
import kotlinx.coroutines.coroutineScope
import java.util.*
import kotlin.collections.ArrayList

class CloseOrderInDBUseCase(
    var repoDBTables: RepoDBTables, var repoDBUsers: RepoDBUsers,
    var repoDBRestaurantes: RepoDBRestaurantes,
    var repoDBMovimientos: RepoDBMovimientos,
) {

    suspend operator fun invoke(
        order: Order,
        inputNroComprobante: String
    ): MutableLiveData<Boolean> = coroutineScope {
        when (order.orderType) {
            OrderType.TABLE -> {
                val orderTable = order as OrderTable
                val empleadoIDQueCierra = repoDBUsers.getCurrentUserUIDNORMAL()
                val userQueCierra = repoDBUsers.getUserFromDBByUID(empleadoIDQueCierra)
                val restauranteID = order.restauranteID
                val sucursalID = order.sucursalID

                val venta = crearVentaSinOrderNiIngresos(
                    userQueCierra,
                    restauranteID,
                    sucursalID
                )

                val sucursal = repoDBRestaurantes.getSucursalOneTime(restauranteID, sucursalID)
                    ?: throw Exception("sucursal no hallada")

                val listaIngresos: MutableList<Ingreso> = ArrayList()


                //BORRARRRRR O ARREGLAR NBIEN
                /*val formasDePagoUtilizadasEnPedido
                // = orderTable.pago?.formasDePago!!*/

              /*  for (formaPago in formasDePagoUtilizadasEnPedido) {
                    val ctaParaAcreditar =
                        sucursal.getCuentaParaAcreditarSegunTipoFormaDePago(formaPago.tipo)
                            ?: throw Exception("Cta no tiene forma de apgo aceptada")
                    val ingreso = crearIngreso(
                        userQueCierra.uid,
                        userQueCierra.name,
                        restauranteID,
                        sucursal.nombreDeRestaurante,
                        sucursalID,
                        sucursal.nombre,
                        "Pago de Venta",
                        formaPago.realCobradoQuitandoComisiones,
                        ctaParaAcreditar,
                        CategoriaIngreso.VENTA
                    )
                    listaIngresos.add(ingreso)
                }*/

                venta.listaMovimientos.addAll(listaIngresos)


                //BORRAR LUEGO
                val borrar = MutableLiveData<Boolean>()
                return@coroutineScope borrar
            /*    return@coroutineScope repoDBTables.closeOrderTable(
                    orderTable,
                    inputNroComprobante,
                    userQueCierra,
                    venta
                )*/


            }
            else -> throw Exception("Error CloseOrderInDBUseCase:NO EXISTE TIPO DE ORDER")
        }
    }

    private suspend fun crearVentaSinOrderNiIngresos(
        userQueCierra: Empleado,
        restauranteID: String,
        sucursalID: String
    ): Venta {
        val sucursal: SucursalRest =
            repoDBRestaurantes.getSucursalOneTime(restauranteID, sucursalID)
                ?: throw Exception("crearVentaSinOrder: no existe sucursal")
        val sucursalNombre = sucursal.nombre
        val restauranteNombre = sucursal.nombreDeRestaurante

        /*val cajaDeSucursal: Cuenta? =
            sucursal.getCuentaCaja()
                ?: throw Exception("crearVentaSinOrder:no hay tipo cuenta caja")*/

        val concepto = "Venta concretada"


        return Venta(
            repoDBMovimientos.getRandomID(),
            Calendar.getInstance().timeInMillis.toString(),
            TipoMovimiento.VENTA,
            userQueCierra.uid,
            userQueCierra.name,
            restauranteID,
            restauranteNombre,
            sucursalID,
            sucursalNombre,
            concepto,
            "",
            false,
            null,
            null,
            false,
            null,
            //   cajaDeSucursal,
            // CategoriaIngreso.VENTA,

        )

    }

    private fun crearIngreso(
        userID: String,
        userNombre: String,
        restauranteID: String,
        restauranteNombre: String,
        sucursalID: String,
        sucursalNombre: String,
        conceptoIngreso: String,
        importeTotalIngreso: String,
        cuentaParaAcreditar: Cuenta,
        categoriaIngreso: CategoriaIngreso

    ): Ingreso {
        return Ingreso(
            repoDBMovimientos.getRandomID(),
            Calendar.getInstance().timeInMillis.toString(),
            TipoMovimiento.INGRESO,
            userID,
            userNombre,
            restauranteID,
            restauranteNombre,
            sucursalID,
            sucursalNombre,
            conceptoIngreso,
            importeTotalIngreso,
            false,
            Date(),
            Date(),
            false,
            null,
            cuentaParaAcreditar,
            categoriaIngreso
        )
    }


}