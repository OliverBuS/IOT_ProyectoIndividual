package com.example.iot_proyectoindividual.entity



data class Usuario(
    var key:String?=null,
    var ban:String?=null,
    var codigo:String?=null,
    var correo:String?=null,
    var estado:String?=null,
    var horario:Horario?=null,
    var imagen:String?=null,
    var nombre:String?=null,
    var rol:String?=null,
    var banfin:String?=null,

    ) :java.io.Serializable
