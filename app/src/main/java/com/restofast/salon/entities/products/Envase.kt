package com.restofast.salon.entities.products

import com.restofast.salon.entities.enums.ProductType
import com.restofast.salon.entities.enums.TipoEnvase
import com.restofast.salon.entities.enums.UnitOfMeasurement

class Envase constructor(

     var id: String = "",
     var ownerID : String = "",
     var productType : ProductType = ProductType.ENVASE_DESCARTABLE,
     var sellPrice:  String= "",
     var buyPrice:  String= "",
     var name: String = "Envase",
     var stock: Int = 0,
     var unitOfMeasurement: UnitOfMeasurement = UnitOfMeasurement.UNIDADES,
     var sales: Int = 0,
      var imageUrl: String = "",

    var tipoEnvase : TipoEnvase = TipoEnvase.DESCARTABLE_COMIDA



) /*: Product(id,ownerID, productType, sellPrice, buyPrice, name, stock, unitOfMeasurement, sales, imageUrl)*/ {
}