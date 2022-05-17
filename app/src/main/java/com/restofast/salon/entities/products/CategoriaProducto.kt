package com.restofast.salon.entities.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class CategoriaProducto constructor(
    open var id: String ="",
    open var name: String = "",
    open var entregadoPorMozo: Boolean = false,

) : Parcelable {
}