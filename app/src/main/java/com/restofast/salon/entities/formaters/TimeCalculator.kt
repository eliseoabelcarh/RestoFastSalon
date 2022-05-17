package com.restofast.salon.entities.formaters

import java.util.*

object TimeCalculator {


    fun calcularDiferenciaDeMinutos(fechaHoraInicio: Date, fechaHoraCierre: Date): String {

        lateinit var fechaMayor : Date
        lateinit var fechaMenor : Date
        if(fechaHoraInicio < fechaHoraCierre){
            fechaMayor = fechaHoraCierre
            fechaMenor = fechaHoraInicio
        }else{
            fechaMayor = fechaHoraInicio
            fechaMenor = fechaHoraCierre
        }

        var diferencia: Long = fechaMayor.time - fechaMenor.time

        val segsMilli: Long = 1000
        val minsMilli = segsMilli * 60
        val horasMilli = minsMilli * 60
        val diasMilli = horasMilli * 24

        val diasTranscurridos = diferencia / diasMilli
        diferencia = diferencia % diasMilli

        val horasTranscurridos = diferencia / horasMilli
        diferencia = diferencia % horasMilli

        val minutosTranscurridos = diferencia / minsMilli
        diferencia = diferencia % minsMilli

        val segsTranscurridos = diferencia / segsMilli

        if(diasTranscurridos > 0 ){
            return diasTranscurridos.toString() + "D " +
                    horasTranscurridos.toString() + "H " +
                    minutosTranscurridos.toString() + "mins"

        }else{
            if(horasTranscurridos > 0){
                return horasTranscurridos.toString() + "H " +
                        minutosTranscurridos.toString() + "mins"
            }else{
                return  minutosTranscurridos.toString() + "mins"
            }
        }

    }



}