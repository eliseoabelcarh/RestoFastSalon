package com.restofast.salon.entities.products

import com.restofast.salon.entities.enums.Marca
import com.restofast.salon.entities.enums.ProductType
import com.restofast.salon.entities.enums.TipoEnvase
import com.restofast.salon.entities.enums.UnitOfMeasurement

class Bebida constructor(

    /*override*/ var id: String = "",
   /* override*/ var ownerID : String = "",
    /*override */var productType: ProductType = ProductType.BEBIDA,
    /*override*/ var sellPrice:  String= "",
   /* override*/ var buyPrice:  String= "",
   /* override */var name: String = "Bebida",
  /*  override*/ var stock: Int = 0,
   /* override*/ var unitOfMeasurement: UnitOfMeasurement = UnitOfMeasurement.UNIDADES,
    /*override*/ var sales: Int = 0,
    /*override*/  var imageUrl: String = "",

    var marca: Marca = Marca.SIN_MARCA,
    var mililitros: Int = 0,
    var envase: TipoEnvase = TipoEnvase.BOTELLA_PLASTICO,
    var proveedor: String = ""


) /*: Product(id,ownerID, productType, sellPrice, buyPrice, name, stock, unitOfMeasurement, sales, imageUrl)*/ {
}