package com.restofast.salon.entities.enums

import android.graphics.Color

enum class ItemDePedidoState (val nombre: String) {
    FALTA_ACEPTAR("Falta Aceptar"),
    ESPERANDO_ACEPTACION("Esperando aceptación"),
    ACEPTADO("Aceptado"),
    PREPARANDO("Preparandose"),
    SERVIDO("Servido"),
    ENTREGA_MOZO("Entrega mozo"),
    ANULADO("Anulado"),


    ;


    companion object {


        fun getColorState(state: ItemDePedidoState): Int {
            return when(state){
                FALTA_ACEPTAR-> Color.argb(255, 255, 136, 49)
                ESPERANDO_ACEPTACION-> Color.argb(255, 0, 150, 255)
                ACEPTADO-> Color.argb(255, 0, 214, 29)
                PREPARANDO-> Color.argb(255, 0, 214, 29)
                SERVIDO-> Color.RED
                ENTREGA_MOZO-> Color.rgb(60, 60, 60)
                ANULADO-> Color.LTGRAY
            }
        }



    }




}