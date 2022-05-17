package com.restofast.salon.entities.formaters

import java.util.*

object TimeFormater {

    fun formatearTimestampAString(date: Date?): String {
        if(date == null){
            throw Exception("Error en formatearTimestampAString valor nulo")
        }
        val hora = date.hours
        val minutos = date.minutes
        val horaF = mostrarHoraFormatoAMPM(hora)
        val minF = mostrarMinutosFormateado(minutos)
        val meridiano = mostrarAMPM(hora)
        return "$horaF:$minF $meridiano"

    }

    private fun mostrarAMPM(hora: Int): String {
        return if(hora >= 12){
            "pm"
        }else{
            "am"
        }
    }

    private fun mostrarMinutosFormateado(minutos: Int): String {
        if(minutos == 0){
            return "00"
        }
        else if(minutos < 10){
            return "0$minutos"
        }
        else{
            return "$minutos"
        }
    }

    private fun mostrarHoraFormatoAMPM(hora: Int): String {
        return if(hora >= 12){
            (hora - 12).toString()
        }else{
            hora.toString()
        }
    }

}