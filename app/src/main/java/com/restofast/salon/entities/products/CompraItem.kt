package com.restofast.salon.entities.products

import android.os.Parcelable
import com.restofast.salon.entities.enums.UnitOfMeasurement

import kotlinx.parcelize.Parcelize

@Parcelize
open class CompraItem constructor(

    open var id: String = "",
    open var compraID : String = "",
    open var cant: Int = 0,
    open var unitOfMeasurement: UnitOfMeasurement = UnitOfMeasurement.UNIDADES,
    open var productID: String = "",
    open var productName: String = "",
    open var subTotal: String = "",


    ) : Parcelable {

}