package com.example.iot_proyectoindividual.entity

data class Reunion(
    val descripcion: String,
    val hora: String,
    val latitud:Double,
    val longitud:Double,
    val invitados:List<Invitado>
)
