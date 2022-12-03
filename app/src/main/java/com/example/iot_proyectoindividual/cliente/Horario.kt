package com.example.iot_proyectoindividual.cliente

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.FragmentHorarioBinding
import com.example.iot_proyectoindividual.save.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class Horario : Fragment() {


    lateinit var binding: FragmentHorarioBinding
    lateinit var horariosBlocks: List<TextView>
    lateinit var horariosDias: List<TextView>

    object stateSavedValues {
        var estados: CharArray = CharArray(14)
        var creado: Boolean = false
        var dia: String = "Lunes"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHorarioBinding.inflate(inflater, container, false)
        horariosBlocks = listOf(
            binding.hora1, binding.hora2, binding.hora3, binding.hora4, binding.hora5,
            binding.hora6, binding.hora7, binding.hora8, binding.hora9, binding.hora10,
            binding.hora11, binding.hora12, binding.hora13, binding.hora14
        )
        horariosDias = listOf(
            binding.dia1, binding.dia2, binding.dia3, binding.dia4, binding.dia5, binding.dia6
        )

        binding.diaTitulo.setText(stateSavedValues.dia)
        if (stateSavedValues.creado) {
            reloadVista()
        } else {
            setvista(User.usuario.horario?.lunes!!)
            stateSavedValues.creado = true
        }

        for (i in horariosDias) {
            i.setOnClickListener {

            }
        }

        for (i in horariosBlocks) {
            i.setOnClickListener {
                when (it.id) {
                    horariosBlocks[0].id -> {
                        if (stateSavedValues.estados[0] == '1') {
                            stateSavedValues.estados[0] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[0] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[1].id -> {
                        if (stateSavedValues.estados[1] == '1') {
                            stateSavedValues.estados[1] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[1] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[2].id -> {
                        if (stateSavedValues.estados[2] == '1') {
                            stateSavedValues.estados[2] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[2] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[3].id -> {
                        if (stateSavedValues.estados[3] == '1') {
                            stateSavedValues.estados[3] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[3] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[4].id -> {
                        if (stateSavedValues.estados[4] == '1') {
                            stateSavedValues.estados[4] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[4] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[5].id -> {
                        if (stateSavedValues.estados[5] == '1') {
                            stateSavedValues.estados[5] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[5] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[6].id -> {
                        if (stateSavedValues.estados[6] == '1') {
                            stateSavedValues.estados[6] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[6] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[7].id -> {
                        if (stateSavedValues.estados[7] == '1') {
                            stateSavedValues.estados[7] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[7] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[8].id -> {
                        if (stateSavedValues.estados[8] == '1') {
                            stateSavedValues.estados[8] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[8] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[9].id -> {
                        if (stateSavedValues.estados[9] == '1') {
                            stateSavedValues.estados[9] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[9] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[10].id -> {
                        if (stateSavedValues.estados[10] == '1') {
                            stateSavedValues.estados[10] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[10] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[11].id -> {
                        if (stateSavedValues.estados[11] == '1') {
                            stateSavedValues.estados[11] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[11] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[12].id -> {
                        if (stateSavedValues.estados[12] == '1') {
                            stateSavedValues.estados[12] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[12] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[13].id -> {
                        if (stateSavedValues.estados[13] == '1') {
                            stateSavedValues.estados[13] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[13] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                    horariosBlocks[14].id -> {
                        if (stateSavedValues.estados[14] == '1') {
                            stateSavedValues.estados[14] = '0'
                            it.setBackgroundColor(Color.parseColor("#2b2831"))
                        } else {
                            stateSavedValues.estados[14] = '1'
                            it.setBackgroundColor(Color.parseColor("#55171a"))
                        }
                    }
                }
            }
        }

        for (i in horariosDias) {
            i.setOnClickListener {
                stateSavedValues.dia = (it as TextView).text.toString()
                binding.diaTitulo.setText(stateSavedValues.dia)
                reloadDay()
            }
        }

        binding.buttonGuardar.setOnClickListener {
            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle("Guardar Cambios")
                    .setMessage("¿Está seguro que quiere actualizar su horario de clases?")
                    .setNegativeButton("Cancelar") { _, _ -> }
                    .setPositiveButton("Guardar") { _, _ ->
                        val ref = Firebase.database.reference.child("usuarios/${User.uid}/horario")
                        val est = String(stateSavedValues.estados).toInt(2)

                        ref.child(stateSavedValues.dia.lowercase()).setValue(est)
                            .addOnSuccessListener {
                                when (stateSavedValues.dia) {
                                    "Lunes" -> {
                                        User.usuario.horario?.lunes = est
                                    }
                                    "Martes" -> {
                                        User.usuario.horario?.martes = est

                                    }
                                    "Miercoles" -> {
                                        User.usuario.horario?.miercoles = est

                                    }
                                    "Jueves" -> {
                                        User.usuario.horario?.jueves = est

                                    }
                                    "Viernes" -> {
                                        User.usuario.horario?.viernes = est

                                    }
                                    "Sabado" -> {
                                        User.usuario.horario?.sabado = est

                                    }
                                }
                                Toast.makeText(context, "Horario actualizado", Toast.LENGTH_SHORT)
                                    .show()
                            }.addOnFailureListener {
                            Toast.makeText(context, "No se pudo actualizar", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }.show()
            }

        }

        return binding.root

    }

    private fun reloadDay() {
        when (stateSavedValues.dia) {
            "Lunes" -> {
                User.usuario.horario?.lunes?.let { setvista(it) }
            }
            "Martes" -> {
                User.usuario.horario?.martes?.let { setvista(it) }

            }
            "Miercoles" -> {
                User.usuario.horario?.miercoles?.let { setvista(it) }

            }
            "Jueves" -> {
                User.usuario.horario?.jueves?.let { setvista(it) }

            }
            "Viernes" -> {
                User.usuario.horario?.viernes?.let { setvista(it) }

            }
            "Sabado" -> {
                User.usuario.horario?.sabado?.let { setvista(it) }

            }
        }
    }


    private fun setvista(horasOcupadas: Int) {
        stateSavedValues.estados = obtenerBinario(horasOcupadas).toCharArray()
        for (i in 13 downTo 0 step 1) {
            if (stateSavedValues.estados[i] == '0') {
                horariosBlocks[i].setBackgroundColor(Color.parseColor("#2b2831"))
            } else {
                horariosBlocks[i].setBackgroundColor(Color.parseColor("#55171a"))
            }
        }

    }

    private fun reloadVista() {
        for (i in 13 downTo 0 step 1) {
            if (stateSavedValues.estados[i] == '0') {
                horariosBlocks[i].setBackgroundColor(Color.parseColor("#2b2831"))
            } else {
                horariosBlocks[i].setBackgroundColor(Color.parseColor("#55171a"))
            }
        }

    }

    private fun obtenerBinario(numero: Int): String {
        val len = 14
        return String.format(
            "%" + len + "s",
            Integer.toBinaryString(numero)
        ).replace(" ".toRegex(), "0")
    }

}