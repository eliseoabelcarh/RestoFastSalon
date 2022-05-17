package com.restofast.salon.entities.order

import android.os.Parcelable
import com.restofast.salon.entities.enums.ItemDePedidoState
import com.restofast.salon.entities.products.CategoriaProducto
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Parcelize
class ItemDePedido constructor(

    var id: String = "",
    var cantidadDeProductos: Int = 0,
    var productoID: String = "",
    var categoriaProductoID: String = "",
    var precioUnitarioDeProducto: String = "",
    var subTotal: String = "0.00",
    var state: ItemDePedidoState = ItemDePedidoState.FALTA_ACEPTAR,
    var openingTime: Date? = null,
    var closingTime: Date? = null,
    var empleadoIDQueCreaItemDePedido: String = "",
    var comentarios: String = "",
    var motivoAnulacion: String = "",
    var empleadoIDQueAnula: String = "",

    ) : Parcelable {

    fun agregarUnoYActualizarPrecio() {
        cantidadDeProductos++
        var subTotalDecimal = BigDecimal(subTotal)
        subTotalDecimal = subTotalDecimal.add(BigDecimal(precioUnitarioDeProducto))
        subTotal = subTotalDecimal.setScale(2, RoundingMode.HALF_DOWN).toPlainString()
    }

    fun restarUnoYActualizarPrecio() {
        cantidadDeProductos--
        var subTotalDecimal = BigDecimal(subTotal)
        subTotalDecimal = subTotalDecimal.subtract(BigDecimal(precioUnitarioDeProducto))
        subTotal = subTotalDecimal.setScale(2, RoundingMode.HALF_DOWN).toPlainString()
    }

    fun mozoPuedeAgregarComentarios(): Boolean {
        return state == ItemDePedidoState.ENTREGA_MOZO
                || state == ItemDePedidoState.FALTA_ACEPTAR
                || state == ItemDePedidoState.ESPERANDO_ACEPTACION
    }

    fun mozoPuedeBorrar(): Boolean {
        return state == ItemDePedidoState.ENTREGA_MOZO
                || state == ItemDePedidoState.FALTA_ACEPTAR
                || state == ItemDePedidoState.ESPERANDO_ACEPTACION
    }
    fun mozoPuedeAumentarEnUno(): Boolean {
        return state == ItemDePedidoState.ENTREGA_MOZO
                || state == ItemDePedidoState.FALTA_ACEPTAR
                || state == ItemDePedidoState.ESPERANDO_ACEPTACION
    }
    fun mozoPuedeRestarEnUno(): Boolean {
        return state == ItemDePedidoState.ENTREGA_MOZO
                || state == ItemDePedidoState.FALTA_ACEPTAR
                || state == ItemDePedidoState.ESPERANDO_ACEPTACION
    }

    fun aplicarStateParaEnviarAceptacionEnCocinaSegunCategoriaProducto(
        categoriasDeProducto: MutableList<CategoriaProducto>
    ): ItemDePedido {
        val stateInicial = this.state
        val miCategoriaProductoID = this.categoriaProductoID
        if (stateInicial == ItemDePedidoState.FALTA_ACEPTAR) {
            val miCategoriaProducto: CategoriaProducto? =
                buscarMiCategoria(categoriasDeProducto, miCategoriaProductoID)
                    ?: throw Exception("error al encontrar categoria productoo")
            if (miCategoriaProducto!!.entregadoPorMozo) {
                this.state = ItemDePedidoState.ENTREGA_MOZO
            } else {
                this.state = ItemDePedidoState.ESPERANDO_ACEPTACION
            }
        }
        return this
    }

    private fun buscarMiCategoria(categoriasDeProducto: MutableList<CategoriaProducto>, miCategoriaProductoID: String): CategoriaProducto? {
        var i = 0
        var encontrado: CategoriaProducto? = null
        while (i < categoriasDeProducto.size && encontrado == null){
            if(categoriasDeProducto[i].id == miCategoriaProductoID){
                encontrado = categoriasDeProducto[i]
            }
            i++
        }
        return encontrado
    }


}