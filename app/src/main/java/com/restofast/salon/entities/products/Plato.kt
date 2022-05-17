package com.restofast.salon.entities.products


import com.restofast.salon.entities.enums.CategoriaPlato
import com.restofast.salon.entities.enums.ProductType
import com.restofast.salon.entities.enums.UnitOfMeasurement

class Plato constructor(

     var id: String = "",
     var ownerID : String = "",
     var productType: ProductType = ProductType.PLATO,
     var sellPrice:  String= "",
     var buyPrice:  String= "",
     var name: String = "Plato",
     var stock: Int = 0,
     var unitOfMeasurement: UnitOfMeasurement = UnitOfMeasurement.UNIDADES,
     var sales: Int = 0,
      var imageUrl: String = "",

    var category : CategoriaPlato = CategoriaPlato.SIN_CATEGORIA,
    var ingredients: MutableList<Product> = ArrayList(),


    ) /*: Product(id, ownerID, productType, sellPrice, buyPrice, name, stock, unitOfMeasurement, sales, imageUrl)*/ {

}