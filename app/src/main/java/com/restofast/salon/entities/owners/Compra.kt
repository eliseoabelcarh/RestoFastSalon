package com.restofast.salon.entities.owners

import com.restofast.salon.entities.enums.CategoriaEgreso
import com.restofast.salon.entities.enums.TipoMovimiento
import com.restofast.salon.entities.enums.UnitOfMeasurement
import com.restofast.salon.entities.products.CompraItem
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
class Compra constructor(

    override var id: String = "",
    override var nroOperacion: String = "",
    override var tipoMovimiento: TipoMovimiento = TipoMovimiento.EGRESO,
    override var userID: String = "",
    override var userNombre: String = "",
    override var restauranteID: String = "",
    override var restauranteNombre: String = "",
    override var sucursalID: String = "",
    override var sucursalNombre: String = "",
    override var concepto: String = "",
    override var importe: String = "",
    override var active: Boolean = false,
    override var creacion: Date? = null,
    override var cerrado: Date? = null,
    override var editado: Boolean = false,
    override var ultimaEdicion: Date? = null,
    override var cuentaUtilizada: Cuenta? = null,
    override var categoria: CategoriaEgreso = CategoriaEgreso.COMPRA,

    var listaCompraItems: MutableList<@RawValue CompraItem> = ArrayList()

) : Egreso(
    id,
    nroOperacion,
    tipoMovimiento,
    userID,
    userNombre,
    restauranteID,
    restauranteNombre,
    sucursalID,
    sucursalNombre,
    concepto,
    importe,
    active,
    creacion,
    cerrado,
    editado,
    ultimaEdicion,
    cuentaUtilizada,
    categoria
) {

    fun agregarCompraItemYActualizarValores(compraItem: CompraItem) {
        listaCompraItems.add(compraItem)
        sumarAlImporteDeCompraActual(compraItem.subTotal)
    }

    fun updateCompraItemYActualizarValores(
        compraItemID: String,
        nombreProducto: String,
        cantidad: Int,
        medida: UnitOfMeasurement,
        importe: String
    ): CompraItem? {
        val compraItemEncontrado = removerCompraItemSiExisteYActualizarImporte(compraItemID)
        if (compraItemEncontrado != null) {
            compraItemEncontrado.productName = nombreProducto
            compraItemEncontrado.cant = cantidad
            compraItemEncontrado.unitOfMeasurement = medida
            compraItemEncontrado.subTotal = importe
            agregarCompraItemYActualizarValores(compraItemEncontrado)
        }
        return compraItemEncontrado
    }

    fun removerCompraItemSiExisteYActualizarImporte(compraItemID: String): CompraItem? {
        var i = 0
        var encontrado: CompraItem? = null
        while (i < listaCompraItems.size && encontrado == null) {
            if (listaCompraItems[i].id == compraItemID) {
                encontrado = listaCompraItems[i]
                restarAlImporteDeCompraActual(listaCompraItems[i].subTotal)
                listaCompraItems.removeAt(i)
            }
            i++
        }
        return encontrado
    }

    private fun sumarAlImporteDeCompraActual(monto: String) {
        var totalActual = BigDecimal(importe)
        totalActual = (totalActual.add(BigDecimal(monto)))
        importe = totalActual.toPlainString()
    }

    private fun restarAlImporteDeCompraActual(monto: String) {
        var totalActual = BigDecimal(importe)
        totalActual = (totalActual.subtract(BigDecimal(monto)))
        importe = totalActual.toPlainString()
    }

    fun cerrarCompraYActualizar() {
        cerrado = Date()
        active = false
        sumarItems()
    }

    private fun sumarItems() {
        var total = BigDecimal("0.00")
        for (item in listaCompraItems) {
            total = total.add(BigDecimal(item.subTotal))
        }
        importe = total.toPlainString()
    }

}