package com.restofast.salon.entities.owners

import android.os.Parcelable
import com.restofast.salon.entities.enums.TipoCuenta
import com.restofast.salon.entities.enums.TipoFormaDePago
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
open class Cuenta constructor(

    open var id: String = "",
    open var nombre: String = "",
    open var tipoCuenta: TipoCuenta = TipoCuenta.OTRO,
    open var nombreMoneda: String = "",
    open var simboloMoneda: String = "",
    open var disponible: String = "",
    open var sePuedeEliminar: Boolean = true,

    open var tipoFormasDePagoAceptadas: MutableList<TipoFormaDePago> = ArrayList()

    ) : Parcelable {

     fun descontar(egreso: Egreso) {
        val importeADescontar = BigDecimal(egreso.importe)
        var importeActual = BigDecimal(disponible)
        importeActual = importeActual.subtract(importeADescontar)
        disponible = importeActual.toPlainString()
    }

    fun acreditar(ingreso: Ingreso) {
        val importeAAcreditar = BigDecimal(ingreso.importe)
        var importeActual = BigDecimal(disponible)
        importeActual = importeActual.add(importeAAcreditar)
        disponible = importeActual.toPlainString()
    }
}