package com.example.iot_proyectoindividual.entity

import com.example.iot_proyectoindividual.funcs.MyFunctions
import java.util.Calendar

data class Horario(
    var lunes: Int?=null,
    var martes: Int?=null,
    var miercoles: Int?=null,
    var jueves: Int?=null,
    var viernes: Int?=null,
    var sabado: Int?=null,
){
    fun disponible() : Int {

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        var binHoras = MyFunctions.obtenerBinario(0)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        if(hour<7 || hour>22 || Calendar.SUNDAY==day){
            return -1
        }
        when(day){
            Calendar.MONDAY->{
                binHoras = MyFunctions.obtenerBinario(lunes!!)
            }
            Calendar.TUESDAY->{
                binHoras = MyFunctions.obtenerBinario(martes!!)
            }
            Calendar.WEDNESDAY->{
                binHoras = MyFunctions.obtenerBinario(miercoles!!)
            }
            Calendar.THURSDAY->{
                binHoras = MyFunctions.obtenerBinario(jueves!!)
            }
            Calendar.FRIDAY->{
                binHoras = MyFunctions.obtenerBinario(viernes!!)
            }
            Calendar.SATURDAY->{
                binHoras = MyFunctions.obtenerBinario(sabado!!)
            }
        }

        var horaLibre=hour+1
        if(binHoras[hour-7]=='0'){
            return 0
        }

        for(i in (hour-6)..13){
            if(binHoras[i]=='0'){
                return horaLibre
            }
            horaLibre++
        }

        return -1
    }

    fun disponible(hora : Int) : Int {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        var binHoras = MyFunctions.obtenerBinario(0)

        val hour = hora

        if(hour<7 || hour>22 || Calendar.SUNDAY==day){
            return -1
        }
        when(day){
            Calendar.MONDAY->{
                binHoras = MyFunctions.obtenerBinario(lunes!!)
            }
            Calendar.TUESDAY->{
                binHoras = MyFunctions.obtenerBinario(martes!!)
            }
            Calendar.WEDNESDAY->{
                binHoras = MyFunctions.obtenerBinario(miercoles!!)
            }
            Calendar.THURSDAY->{
                binHoras = MyFunctions.obtenerBinario(jueves!!)
            }
            Calendar.FRIDAY->{
                binHoras = MyFunctions.obtenerBinario(viernes!!)
            }
            Calendar.SATURDAY->{
                binHoras = MyFunctions.obtenerBinario(sabado!!)
            }
        }

        var horaLibre=hour+1
        if(binHoras[hour-7]=='0'){
            return 0
        }

        for(i in (hour-6)..13){
            if(binHoras[i]=='0'){
                return horaLibre
            }
            horaLibre++
        }

        return -1
    }
}