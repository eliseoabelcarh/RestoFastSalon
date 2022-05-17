package com.restofast.salon.entities.owners

import com.restofast.salon.entities.enums.CategoriaEgreso
import com.restofast.salon.entities.enums.TipoMovimiento
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class Egreso constructor(

    override var id: String = "",
    override var nroOperacion: String = "",
    override var tipoMovimiento: TipoMovimiento = TipoMovimiento.EGRESO,
    override var userID: String = "",
    override var userNombre: String = "",
    override var restauranteID: String = "",
    override var restauranteNombre: String = "",
    override var sucursalID: String = "",
    override var sucursalNombre: String = "",
    override var concepto: String = "",
    override var importe: String = "",
    override var active: Boolean = false,
    override var creacion: Date? = null,
    override var cerrado: Date? = null,
    override var editado: Boolean = false,
    override var ultimaEdicion: Date? = null,
    open var cuentaUtilizada: Cuenta? = null,


    open var categoria: CategoriaEgreso = CategoriaEgreso.SIN_CATEGORIA,

    ) : Movimiento(
    id,
    nroOperacion,
    tipoMovimiento,
    userID,
    userNombre,
    restauranteID,
    restauranteNombre,
    sucursalID,
    sucursalNombre,
    concepto,
    importe,
    active,
    creacion,
    cerrado,
    editado,
    ultimaEdicion,
  //  cuentaUtilizada
) {

    /*override fun toString(): String {
        return "ID:$id - TIPOMOVIMIENTO:$tipoMovimiento - USERID: $userID - RESTAURANTEID: $restauranteID - SUCURSALID: $sucursalID" +
                " CONCEPTO: $concepto - IMPORTE: $importe - ACTIVE: $active - CREACION: $creacion -  CERRADO:$cerrado - EDITADO: $editado" +
                " CATEGORIAAAAAA: $categoria"
    }*/

    fun cerrarEgreso() {
        active = false
        cerrado = Date()
    }

}