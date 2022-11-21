package com.example.iot_proyectoindividual.Entity


data class Amigo(val nombre:String? =null,
                 val estado:String?=null,
                 val disponible:Boolean?=false,
                 val imagenNombre:String?=null)
{
    constructor() :this (null,null,false,null)
}