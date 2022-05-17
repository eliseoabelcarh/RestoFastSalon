package com.restofast.salon.entities.owners

import android.os.Parcelable
import com.restofast.salon.entities.enums.TipoMovimiento
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class Movimiento constructor(

    open var id: String = "",
    open var nroOperacion: String = "",
    open var tipoMovimiento: TipoMovimiento = TipoMovimiento.SIN_TIPO,
    open var userID : String = "",
    open var userNombre: String = "",
    open var restauranteID : String = "",
    open var restauranteNombre: String = "",
    open var sucursalID : String = "",
    open var sucursalNombre: String = "",
    open var concepto: String = "",
    open var importe: String = "",
    open var active: Boolean = false,
    open var creacion : Date? = null,
    open var cerrado: Date? = null,
    open var editado : Boolean = false,
    open var ultimaEdicion: Date? = null,
    //open var cuentaUtilizada: Cuenta? =  null,

    ) : Parcelable {


    /*override fun toString(): String {
        return "ID:$id - TIPOMOVIMIENTO:$tipoMovimiento - USER: $userNombre - RESTAURANTE: $restauranteNombre - SUCURSAL: $sucursalNombre" +
                " CONCEPTO: $concepto - IMPORTE: $importe - ACTIVE: $active - CREACION: $creacion -  CERRADO:$cerrado - EDITADO: $editado" +
                " ULTIMAEDICION: $ultimaEdicion"
    }*/

    override fun toString(): String {
        return "TIPOMOVIMIENTO:$tipoMovimiento - USER: $userNombre - RESTAURANTE: $restauranteNombre - SUCURSAL: $sucursalNombre" +
                " CONCEPTO: $concepto - IMPORTE: $importe - ACTIVE: $active"
    }
}