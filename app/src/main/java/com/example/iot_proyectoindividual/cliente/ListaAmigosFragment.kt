package com.example.iot_proyectoindividual.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.FragmentListaAmigosBinding
import com.example.iot_proyectoindividual.dialog.CodigoDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ListaAmigosFragment : Fragment() {


    private lateinit var binding : FragmentListaAmigosBinding
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView : View
    private lateinit var codigo : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListaAmigosBinding.inflate(layoutInflater)

        materialAlertDialogBuilder = context?.let { MaterialAlertDialogBuilder(it) }!!

        binding.fac.setOnClickListener{
            customAlertDialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_add_friend, null, false)
            launchCustomAlertDialog()
        //val dialog = CodigoDialog()
            //activity?.supportFragmentManager?.let { it1 -> dialog.show(it1,"tag") }
        }

        return binding.root

    }

    private fun launchCustomAlertDialog() {
        codigo = customAlertDialogView.findViewById(R.id.codigo)

        // Building the Alert dialog using materialAlertDialogBuilder instance
        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Añadir amigo")
            .setMessage("Ingrese el código del alumno")
            .setPositiveButton("Añadir") { dialog, _ ->
                Toast.makeText(context,codigo.text.toString(),Toast.LENGTH_SHORT).show()

                /**
                 * Do as you wish with the data here --
                 * Download/Clone the repo from my Github to see the entire implementation
                 * using the link provided at the end of the article.
                 */

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


}