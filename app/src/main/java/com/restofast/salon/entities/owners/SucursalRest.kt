package com.restofast.salon.entities.owners

import android.os.Parcelable
import com.restofast.salon.entities.enums.TipoFormaDePago
import com.restofast.salon.entities.products.CategoriaProducto
import kotlinx.coroutines.coroutineScope
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
open class SucursalRest constructor(

    open var id: String = "",
    open var nombre: String = "",
    open var direccion: String = "",
    open var nombreDeRestaurante: String = "",
    open var restauranteID: String = "",
    open var totalDisponible: String = "",
    open var nroUltimoTicket: Int = 0,
    open var nroUltimaBoleta: Int = 0,
    open var nroUltimaFactura: Int = 0,
    open var comisionVendemas: String = "0.04",
    open var comisionPedidosYa: String = "0.28",
    open var comisionRappi: String = "0.25",
    open var comisionMercadoPago: String = "0.20",
    open var listaDeCuentas: MutableList<Cuenta> = ArrayList(),
    open var listaDeCategoriasDeProducto: MutableList<CategoriaProducto> = ArrayList()

) : Parcelable {

    fun updateDisponibleParaEgreso(egreso: Egreso) {
        val cuentaParaDescontar = egreso.cuentaUtilizada
            ?: throw Exception("updateDisponibleParaEgreso: no se envió cuenta para descontar")
        val cuenta = getCuentaByID(cuentaParaDescontar.id)
            ?: throw Exception("updateDisponibleParaEgreso: no existe cuenta para descontar")
        cuenta.descontar(egreso)
        actualizarImporteSegunSaldoEnCuentas()
    }

    fun updateDisponibleParaIngreso(ingreso: Ingreso) {
        val cuentaParaAcreditar = ingreso.cuentaUtilizada
            ?: throw Exception("updateDisponibleParaIngreso: no se envió cuenta para acreditar")
        val cuenta = getCuentaByID(cuentaParaAcreditar.id)
            ?: throw Exception("updateDisponibleParaIngreso: no existe cuenta para acreditar")
        cuenta.acreditar(ingreso)
        actualizarImporteSegunSaldoEnCuentas()
    }

    private fun actualizarImporteSegunSaldoEnCuentas() {
        var total = BigDecimal("0.00")
        for (cuenta in listaDeCuentas) {
            total = total.add(BigDecimal(cuenta.disponible))
        }
        totalDisponible = total.toPlainString()
    }

    private fun getCuentaByID(id: String): Cuenta? {
        var i = 0
        var encontrado: Cuenta? = null
        while (i < listaDeCuentas.size && encontrado == null) {
            if (listaDeCuentas[i].id == id) {
                encontrado = listaDeCuentas[i]
            }
            i++
        }
        return encontrado
    }

    fun getCuentaByName(nombre: String): Cuenta? {
        var i = 0
        var encontrado: Cuenta? = null
        while (i < listaDeCuentas.size && encontrado == null) {
            if (listaDeCuentas[i].nombre == nombre) {
                encontrado = listaDeCuentas[i]
            }
            i++
        }
        return encontrado
    }

    /*   fun getSiExisteCuentaCaja(): Cuenta? {
           var i = 0
           var encontrado: Cuenta? = null
           while (i < listaDeCuentas.size && encontrado == null) {
               if (listaDeCuentas[i].tipoCuenta == TipoCuenta.CAJA) {
                   encontrado = listaDeCuentas[i]
               }
               i++
           }
           return encontrado
       }*/

    fun getCuentaParaAcreditarSegunTipoFormaDePago(tipoFormaDePago: TipoFormaDePago): Cuenta? {
        var i = 0
        var encontrado: Cuenta? = null

        while (i < listaDeCuentas.size && encontrado == null) {
            if (listaDeCuentas[i].tipoFormasDePagoAceptadas.contains(tipoFormaDePago)) {
                encontrado = listaDeCuentas[i]
            }
            i++
        }
        return encontrado
    }


    /////// nuevossssssssssssss


    fun laCategoriaProductoEsEntregadoPorMozo(categoriaProductoID: String): Boolean  {
        var result = false
        var i = 0
        var categoriaEncontrada: CategoriaProducto? = null
        while (i < listaDeCategoriasDeProducto.size && categoriaEncontrada == null) {
            if (listaDeCategoriasDeProducto[i].id == categoriaProductoID) {
                categoriaEncontrada = listaDeCategoriasDeProducto[i]
                result = categoriaEncontrada.entregadoPorMozo
            }
            i++
        }
        return result
    }


}