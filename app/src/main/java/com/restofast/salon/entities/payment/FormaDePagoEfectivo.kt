package com.restofast.salon.entities.payment

import android.os.Parcelable
import com.restofast.salon.entities.enums.TipoFormaDePago
import kotlinx.parcelize.Parcelize

@Parcelize
class FormaDePagoEfectivo constructor(

    override var id: String = "",
    override var clientePagaCon: String = "",
    override var vuelto: String = "",
    override var importeCobrado: String = "",
    override var tipo: TipoFormaDePago = TipoFormaDePago.EFECTIVO,

    override var comision: String = "",
    override var realCobradoQuitandoComisiones: String = "",
    override var importeDescontadoPorServicio: String = "",


    ) : Parcelable, FormaDePago(
    id,
    clientePagaCon,
    vuelto,
    importeCobrado,
    tipo,
    comision,
    realCobradoQuitandoComisiones,
    importeDescontadoPorServicio
) {
}