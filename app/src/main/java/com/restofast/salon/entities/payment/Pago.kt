package com.restofast.salon.entities.payment

import android.os.Parcelable
import com.restofast.salon.entities.enums.TipoFormaDePago
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.math.BigDecimal
import java.math.RoundingMode

@Parcelize
class Pago constructor(

    var orderID: String = "",
    var empleadoIDPagoCreado: String = "",
    var totalSinDsctos: String = "",
    var totalConDsctos: String = "",
    var formasDePago: MutableList<@RawValue FormaDePago> = ArrayList(),

    var nroComprobante: String = ""


) : Parcelable {

    fun setListaDeFormasDePagoYActualizarValores(lista: MutableList<FormaDePago>) {
        formasDePago = lista
        totalSinDsctos = sumarTodoSinDsctos()
        totalConDsctos = sumarTodoConDsctos()
    }

    fun sumarTodoSinDsctos(): String {
        var suma = BigDecimal("0.00")
        for (item in formasDePago) {
            suma = suma.add(BigDecimal(item.importeCobrado))
            suma = suma.setScale(2,RoundingMode.HALF_DOWN)
        }
        return suma.toPlainString()
    }

    fun sumarTodoConDsctos(): String {
        var suma = BigDecimal("0.00")
        for (item in formasDePago) {
            if (item.tipo == TipoFormaDePago.EFECTIVO) {
                suma = suma.add(BigDecimal(item.importeCobrado))
            } else if (item.tipo == TipoFormaDePago.VENDEMAS) {
                suma = suma.add(BigDecimal(item.realCobradoQuitandoComisiones))
                suma = suma.setScale(2,RoundingMode.HALF_DOWN)
            } else {
                throw Exception("Error SumarTodoConDsctos: No existe formaPago para calcular")
            }
        }
        return suma.toPlainString()
    }
}