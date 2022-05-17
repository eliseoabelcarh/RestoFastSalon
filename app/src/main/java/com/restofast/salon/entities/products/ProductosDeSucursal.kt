package com.restofast.salon.entities.products


import com.restofast.salon.entities.enums.CategoriaPlato
import com.restofast.salon.entities.enums.ProductType
import com.restofast.salon.entities.enums.UnitOfMeasurement

object ProductosDeSucursal {

    //quitar luego q no sirve
    var idSegunRestaurantYSucursal = "CHIFA_FUNNY_CHEN-COMAS1"

    val listaProductos: MutableList<Product> = ArrayList()


    val producto1 = Product(
        "ididi",
        "Pollo Chijaukay",
        "5jXZPnwkeW19zCLQOrhb",
        "CHIFA_FUNNY_CHEN",
        "COMAS1",
        "14.00",
        ""
    )
    val producto2 = Product(
        "ididi",
        "Pollo Kamlu Wantan",
        "5jXZPnwkeW19zCLQOrhb",
        "CHIFA_FUNNY_CHEN",
        "COMAS1",
        "16.00",
        ""
    )
    val producto3 = Product(
        "ididi",
        "Gaseosa 1 Litro",
        "qljrexawL5ixTAOUjM6a",
        "CHIFA_FUNNY_CHEN",
        "COMAS1",
        "3.00",
        ""
    )
    val producto4 = Product(
        "ididi",
        "Envase Descartable",
        "kE1iDZ1SVQtfByJHimbg",
        "CHIFA_FUNNY_CHEN",
        "COMAS1",
        "3.00",
        ""
    )

    init {
        listaProductos.add(producto1)
        listaProductos.add(producto2)
        listaProductos.add(producto3)
        listaProductos.add(producto4)
    }



    var plato1 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "CHAUFA ESPECIAL FUNNY CHEN",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CLASICOS
    )
    var plato2 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "10.00",
        "",
        "CHAUFA C/ POLLO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CLASICOS
    )
    var plato3 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "12.00",
        "5.00",
        "CHAUFA C/ CHANCHO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CLASICOS
    )

    var plato4 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "12.00",
        "",
        "CHAUFA C/ TALLARIN SALTADO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CLASICOS
    )
    var plato5 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "11.00",
        "",
        "CHAUFA C/ TORTILLA DE VERDURAS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CLASICOS
    )
    var plato6 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "11.00",
        "",
        "AEROPUERTO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CLASICOS
    )
    var plato7 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "10.00",
        "",
        "TALLARIN SALTADO C/ POLLO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CLASICOS
    )
    var plato8 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "12.00",
        "",
        "TALLARIN SALTADO C/ CHANCHO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CLASICOS
    )

    var plato9 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO AL AJO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )

    var plato10 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO C/ SALSA BLANCA Y ESPARRAGOS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )


    var plato11 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "CHICHARRON DE POLLO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )


    var plato12 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO EN SALSA CURRY",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )


    var plato13 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO C/ TAUSI",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato14 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO C/ MENSI",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato15 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO C/ PIÑA",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato16 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO TI PA KAY",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato17 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO LIMON KAY",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato18 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO CHI JAU KAY",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato19 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "POLLO C/ VERDURAS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato20 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "TORTILLA C/ POLLO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato21 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "ALITAS FRITAS C/ ZUMO DE LIMON",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato22 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "ALITAS FRITAS C/ TAUSI",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )
    var plato23 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "13.00",
        "",
        "ALITAS FRITAS C/ MENSI",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO1
    )


    var plato24 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "KAMLU WANTAN",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO2
    )
    var plato25 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "POLLO C/ FRUTAS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO2
    )
    var plato26 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "POLLO 5 SABORES",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO2
    )
    var plato27 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "POLLO C/ DURAZNO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO2
    )
    var plato28 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "POLLO EN TROZOS C/ VERDURAS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO2
    )
    var plato29 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "POLLO EN TROZOS C/ TAUSI",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO2
    )
    var plato30 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "POLLO EN TROZOS C/ MENSI",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.POLLO2
    )


    var plato31 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "CHANCHO ASADO AL AJO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CHANCHO
    )
    var plato32 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "CHANCHO ASADO C/ VERDURAS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CHANCHO
    )
    var plato33 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "CHANCHO ASADO C/ TAUSI",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CHANCHO
    )
    var plato34 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "CHANCHO ASADO C/ MENSI",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CHANCHO
    )
    var plato35 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "CHANCHO ASADO C/ PIÑA",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CHANCHO
    )
    var plato36 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "CHANCHO ASADO C/ DURAZNO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CHANCHO
    )
    var plato37 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "CHANCHO ASADO C/ FRUTAS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.CHANCHO
    )


    var plato38 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "7.50",
        "",
        "SOPA DE PATITA",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.SOPAS
    )
    var plato39 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "10.50",
        "",
        "SOPA WANTAN ESPECIAL",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.SOPAS
    )
    var plato40 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "8.50",
        "",
        "SOPA C/ POLLO Y KION",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.SOPAS
    )
    var plato41 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "8.50",
        "",
        "CALDO DE GALLINA CON PRESA",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.SOPAS
    )
    var plato42 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "6.00",
        "",
        "CALDO DE GALLINA SIN PRESA",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.SOPAS
    )


    var plato43 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "10.50",
        "",
        "CALDO DE GALLINA CON PRESA y 2 HUEVOS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.SOPAS
    )
    var plato44 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "8.50",
        "",
        "CALDO DE GALLINA SIN PRESA y 2 HUEVOS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.SOPAS
    )


    var plato45 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "9.00",
        "",
        "1/2 DOC DUMPLINGS AL VAPOR C/ SALSA ESPECIAL",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.EXTRAS
    )
    var plato46 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "17.00",
        "",
        "1 DOC DUMPLINGS AL VAPOR C/ SALSA ESPECIAL",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.EXTRAS
    )
    var plato47 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "8.00",
        "",
        "1/2 DOC WANTAN FRITO C/ SALSA TAMARINDO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.EXTRAS
    )
    var plato48 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "15.00",
        "",
        "1 DOC WANTAN FRITO C/ SALSA TAMARINDO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.EXTRAS
    )
    var plato49 = Plato(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.PLATO,
        "4.00",
        "",
        "PORCION DE ARROZ BLANCO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, "",
        CategoriaPlato.EXTRAS
    )


    var bebida1 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "3.50",
        "",
        "GASEOSA INKA COLA 600 ML",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )
    var bebida2 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "6.00",
        "",
        "GASEOSA INKA COLA 1 LITRO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )
    var bebida3 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "8.00",
        "",
        "GASEOSA INKA COLA 1.5 LITROS ",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )
    var bebida4 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "3.50",
        "",
        "GASEOSA COCA COLA 600 ML",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )
    var bebida5 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "6.00",
        "",
        "GASEOSA COCA COLA 1 LITRO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )
    var bebida6 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "8.00",
        "",
        "GASEOSA COCA COLA 1.5 LITROS",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )
    var bebida7 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "2.50",
        "",
        "GASEOSA FANTA 500 ML",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )
    var bebida8 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "2.50",
        "",
        "GASEOSA KOLA INGLESA 500 ML",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )


    var bebida9 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "3.00",
        "",
        "TE JAZMIN",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )

    var bebida10 = Bebida(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.BEBIDA,
        "2.50",
        "",
        "INFUSION VARIADO",
        0,
        UnitOfMeasurement.UNIDADES,
        0, ""
    )
    var envase1 = Envase(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.ENVASE_DESCARTABLE,
        "0.50",
        "",
        "ENVASE 0.50",
        0,
        UnitOfMeasurement.UNIDADES,
        0,
        "",
    )
    var envase2 = Envase(
        "idi",
        idSegunRestaurantYSucursal,
        ProductType.ENVASE_DESCARTABLE,
        "1.00",
        "",
        "ENVASE 1.00",
        0,
        UnitOfMeasurement.UNIDADES,
        0,
        "",
    )

    init {
        /* listaProductos.add(plato1)
         listaProductos.add(plato2)
         listaProductos.add(plato3)
         listaProductos.add(plato4)
         listaProductos.add(plato5)
         listaProductos.add(plato6)
         listaProductos.add(plato7)
         listaProductos.add(plato8)
         listaProductos.add(plato9)
         listaProductos.add(plato10)
         listaProductos.add(plato11)
         listaProductos.add(plato12)
         listaProductos.add(plato13)
         listaProductos.add(plato14)
         listaProductos.add(plato15)
         listaProductos.add(plato16)
         listaProductos.add(plato17)
         listaProductos.add(plato18)
         listaProductos.add(plato19)
         listaProductos.add(plato20)
         listaProductos.add(plato21)
         listaProductos.add(plato22)
         listaProductos.add(plato23)
         listaProductos.add(plato24)
         listaProductos.add(plato25)
         listaProductos.add(plato26)
         listaProductos.add(plato27)
         listaProductos.add(plato28)
         listaProductos.add(plato29)
         listaProductos.add(plato30)
         listaProductos.add(plato31)
         listaProductos.add(plato32)
         listaProductos.add(plato33)
         listaProductos.add(plato34)
         listaProductos.add(plato35)
         listaProductos.add(plato36)

         listaProductos.add(plato37)
         listaProductos.add(plato38)
         listaProductos.add(plato39)
         listaProductos.add(plato40)
         listaProductos.add(plato41)
         listaProductos.add(plato42)

         listaProductos.add(plato43)
         listaProductos.add(plato44)
         listaProductos.add(plato45)
         listaProductos.add(plato46)
         listaProductos.add(plato47)
         listaProductos.add(plato48)
         listaProductos.add(plato49)





         listaProductos.add(bebida1)
         listaProductos.add(bebida2)
         listaProductos.add(bebida3)
         listaProductos.add(bebida4)
         listaProductos.add(bebida5)
         listaProductos.add(bebida6)
         listaProductos.add(bebida7)
         listaProductos.add(bebida8)
         listaProductos.add(bebida9)
         listaProductos.add(bebida10)


         listaProductos.add(envase1)
         listaProductos.add(envase2)*/


    }


}