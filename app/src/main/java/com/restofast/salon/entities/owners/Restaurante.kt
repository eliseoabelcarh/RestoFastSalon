package com.restofast.salon.entities.owners

open class Restaurante constructor(

    open var id: String = "",
    open var nombre: String = "",
    open var owners: MutableList<String> = ArrayList(),
    open var listaSucursales : MutableList<SucursalRest> =  ArrayList()

) {
    fun updateDisponibleParaEgreso(egreso: Egreso)  {
        val sucursalID = egreso.sucursalID
        val sucursalRest : SucursalRest? = getSucursalByID(sucursalID)
        if(sucursalRest != null ){
            sucursalRest.updateDisponibleParaEgreso(egreso)
        }else{
            throw Exception ("updateDisponibleParaEgreso: sucursal no encontrada")
        }
    }
    fun updateDisponibleParaIngreso(ingreso: Ingreso) {
        val sucursalID = ingreso.sucursalID
        val sucursalRest : SucursalRest? = getSucursalByID(sucursalID)
        if(sucursalRest != null ){
            sucursalRest.updateDisponibleParaIngreso(ingreso)
        }else{
            throw Exception ("updateDisponibleParaIngreso: sucursal no encontrada")
        }
    }

    fun getSucursalByID(sucursalID: String): SucursalRest? {
        var i = 0
        var encontrado : SucursalRest? = null
        while(i < listaSucursales.size && encontrado == null){
            if(listaSucursales[i].id == sucursalID){
                encontrado = listaSucursales[i]
            }
            i++
        }
        return encontrado
    }



}