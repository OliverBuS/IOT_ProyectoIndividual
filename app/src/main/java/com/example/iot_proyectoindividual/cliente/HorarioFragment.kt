package com.example.iot_proyectoindividual.cliente

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.iot_proyectoindividual.databinding.FragmentHorarioBinding
import com.example.iot_proyectoindividual.save.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HorarioFragment : Fragment() {


    lateinit var binding: FragmentHorarioBinding
    lateinit var horariosBlocks: List<TextView>
    lateinit var horariosDias: List<TextView>

    object stateSavedValues {
        var estados: CharArray = CharArray(15)
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
            binding.hora11, binding.hora12, binding.hora13, binding.hora14, binding.hora15
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
                var x = 0
                when (it.id) {
                    horariosBlocks[0].id -> {
                        x = 0
                    }
                    horariosBlocks[1].id -> {
                        x = 1
                    }
                    horariosBlocks[2].id -> {
                        x = 2
                    }
                    horariosBlocks[3].id -> {
                        x = 3
                    }
                    horariosBlocks[4].id -> {
                        x = 4
                    }
                    horariosBlocks[5].id -> {
                        x = 5
                    }
                    horariosBlocks[6].id -> {
                        x = 6
                    }
                    horariosBlocks[7].id -> {
                        x = 7
                    }
                    horariosBlocks[8].id -> {
                        x = 8
                    }
                    horariosBlocks[9].id -> {
                        x = 9
                    }
                    horariosBlocks[10].id -> {
                        x = 10
                    }
                    horariosBlocks[11].id -> {
                        x = 11
                    }
                    horariosBlocks[12].id -> {
                        x = 12
                    }
                    horariosBlocks[13].id -> {
                        x = 13
                    }
                    horariosBlocks[14].id -> {
                        x = 14
                    }
                    horariosBlocks[15].id -> {
                        x = 15
                    }
                }
                if (stateSavedValues.estados[x] == '1') {
                    stateSavedValues.estados[x] = '0'
                    it.setBackgroundColor(Color.parseColor("#2b2831"))
                } else {
                    stateSavedValues.estados[x] = '1'
                    it.setBackgroundColor(Color.parseColor("#55171a"))
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
        for (i in 14 downTo 0 step 1) {
            if (stateSavedValues.estados[i] == '0') {
                horariosBlocks[i].setBackgroundColor(Color.parseColor("#2b2831"))
            } else {
                horariosBlocks[i].setBackgroundColor(Color.parseColor("#55171a"))
            }
        }

    }

    private fun reloadVista() {
        for (i in 14 downTo 0 step 1) {
            if (stateSavedValues.estados[i] == '0') {
                horariosBlocks[i].setBackgroundColor(Color.parseColor("#2b2831"))
            } else {
                horariosBlocks[i].setBackgroundColor(Color.parseColor("#55171a"))
            }
        }

    }

    private fun obtenerBinario(numero: Int): String {
        val len = 15
        return String.format(
            "%" + len + "s",
            Integer.toBinaryString(numero)
        ).replace(" ".toRegex(), "0")
    }

}