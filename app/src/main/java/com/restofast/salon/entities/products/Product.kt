package com.restofast.salon.entities.products

open class Product constructor(
    open var id: String ="",
    open var name: String = "",
    open var categoriaProductoID: String = "",
    open var restauranteID: String = "",
    open var sucursalID : String =  "",
    open var price:  String= "",
    open var imageUrl: String = "",

    ) {
}