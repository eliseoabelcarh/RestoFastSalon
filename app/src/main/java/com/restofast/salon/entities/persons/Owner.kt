package com.restofast.salon.entities.persons



import com.restofast.salon.entities.enums.RoleType
import com.restofast.salon.sharedData.domain.entities.UserModel

open class Owner constructor(

    override var uid: String = "",
    override var name: String = "",
    override var lastname: String = "",
    override var dni: String = "",
    override var email: String= "",
    override var password: String= "",

    open var listaDeRoleTypes: MutableList<RoleType> = ArrayList(),

    open var maxCantSucursales: Int = 0,
    open var codigoAutorizacion : String =  "",

    open var isActive: Boolean = false


) : UserModel(uid, name, lastname,dni, email, password) {


    fun tieneAlgunPermisoSegunRoleTypes(listaDePermisos: MutableList<RoleType>):Boolean{
        var i = 0
        var tienePermiso = false
        while (i < listaDePermisos.size && !tienePermiso){
            if(tieneRoleType(listaDePermisos[i])){
                tienePermiso = true
            }
            i++
        }
        return tienePermiso
    }


    private fun tieneRoleType(role: RoleType): Boolean {
        var i = 0
        var encontrado = false

        while(i < listaDeRoleTypes.size && !encontrado){
            if(listaDeRoleTypes[i] == role){
                encontrado= true
            }
            i++
        }
        return encontrado
    }

  /*  fun obtenerSucursalRestaurantID(): String{
        return "${restauranteID}-${sucursal}"
    }

    fun yaTieneAsignadoRestauranteYSucursal(): Boolean {
        return restauranteID != RestauranteID.SIN_ASIGNAR && sucursal != Sucursal.SIN_ASIGNAR
    }*/
}