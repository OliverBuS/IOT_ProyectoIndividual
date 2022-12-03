package com.example.iot_proyectoindividual.entity


data class Amigo(val nombre:String? =null,
                 val estado:String?=null,
                 val disponible:Boolean?=null,
                 val imagenNombre:String?=null)
{
    constructor() :this (null,null,null,null)
}