package com.example.iot_proyectoindividual.funcs

class MyFunctions {
    companion object {
        fun obtenerBinario(numero: Int): String {
            val len = 15
            return String.format(
                "%" + len + "s",
                Integer.toBinaryString(numero)
            ).replace(" ".toRegex(), "0")
        }
    }
}