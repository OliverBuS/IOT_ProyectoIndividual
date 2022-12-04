package com.example.iot_proyectoindividual.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.iot_proyectoindividual.R

class CodigoDialog : AppCompatDialogFragment(){

    private lateinit var codigo: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_add_friend,null)

        builder.setView(view).setTitle("Añadir amigo")
            .setNegativeButton("Cancelar",{ dialog, which ->

            }).setPositiveButton("Añadir",{dialog,which->

            })

        if (view != null) {
            codigo = view.findViewById(R.id.codigo)
        }
        return builder.create()
    }
}