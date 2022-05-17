package com.restofast.salon.entities.payment


import android.os.Parcelable
import com.restofast.salon.entities.enums.TipoFormaDePago
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.RoundingMode

@Parcelize
open class FormaDePago(

    open var id: String = "",
    open var clientePagaCon: String = "",
    open var vuelto: String = "",
    open var importeCobrado: String = "",
    open var tipo: TipoFormaDePago = TipoFormaDePago.SIN_ASIGNAR,

    open var comision: String = "",
    open var realCobradoQuitandoComisiones: String = "",
    open var importeDescontadoPorServicio: String = "",

    ) : Parcelable {

    override fun toString(): String {
        return "cliente paga con: $clientePagaCon .... vuelto: $vuelto .... importeCobrado: $importeCobrado ....." +
                "comision: $comision .... realcobradoquitandocomisiones: $realCobradoQuitandoComisiones ..... importeDescontadoPorServicio: $importeDescontadoPorServicio"
    }

    fun actualizarValores() {
        if (comision.isNotBlank()) {
            var dscto = BigDecimal(importeCobrado).multiply(BigDecimal(comision))
            dscto = dscto.setScale(2, RoundingMode.HALF_DOWN)
            importeDescontadoPorServicio = dscto.toPlainString()
            var realCobrado = BigDecimal(importeCobrado).subtract(dscto)
            realCobrado = realCobrado.setScale(2, RoundingMode.HALF_DOWN)
            realCobradoQuitandoComisiones = realCobrado.toPlainString()
        } else {
            realCobradoQuitandoComisiones = importeCobrado
        }
    }


}