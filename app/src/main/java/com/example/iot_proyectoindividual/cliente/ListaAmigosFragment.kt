package com.example.iot_proyectoindividual.cliente

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.adapter.AmigosAdapter
import com.example.iot_proyectoindividual.databinding.FragmentListaAmigosBinding
import com.example.iot_proyectoindividual.dialog.CodigoDialog
import com.example.iot_proyectoindividual.entity.Amigo
import com.example.iot_proyectoindividual.entity.Relacion
import com.example.iot_proyectoindividual.save.RelacionesUsuario
import com.example.iot_proyectoindividual.save.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class ListaAmigosFragment : Fragment() {


    private lateinit var binding: FragmentListaAmigosBinding
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    private lateinit var codigo: EditText
    private lateinit var reference: DatabaseReference


    private lateinit var recycler: RecyclerView

    private lateinit var listener : ValueEventListener
    private lateinit var path :String

    object savedTemp {
        var created = false
        var path = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentListaAmigosBinding.inflate(layoutInflater)
        reference = Firebase.database.reference
        materialAlertDialogBuilder = context?.let { MaterialAlertDialogBuilder(it) }!!
        recycler = binding.recycleView
        //Añadir amigo
        binding.fac.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_add_friend, null, false)
            launchCustomAlertDialog()

        }

        savedTemp.path = "relaciones/${User.uid}"

        var listaListener: ValueEventListener? = null
           listener= reference.child("relaciones/${User.uid}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            var relacion = postSnapshot.getValue<String>()
                            if (relacion != null) {
                                RelacionesUsuario.lista.put(
                                    postSnapshot.key!!,
                                    relacion
                                )
                                //Log.d("hola",relacion)
                            }

                            if (listaListener != null) {
                                reference.child("amigos").removeEventListener(listaListener!!)
                            }

                            listaListener = reference.child("amigos").addValueEventListener(
                                object : ValueEventListener {
                                    override fun onDataChange(snapshot2: DataSnapshot) {
                                        val listaTemp = HashMap<String,Amigo>()
                                        for (children in snapshot2.children) {
                                            val amigo = children.getValue<Amigo>()
                                            if (amigo != null) {
                                                if(RelacionesUsuario.lista.containsKey(children.key)){

                                                    amigo.tipo = RelacionesUsuario.lista[children.key]
                                                    amigo.uid = children.key
                                                    amigo.uid?.let { listaTemp.put(it,amigo) }
                                                }
                                            }
                                        }
                                        RelacionesUsuario.amigos=listaTemp
                                        listarAmigos()
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
                                    }

                                }
                            )


                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })

        return binding.root
    }

    public fun reset() {
        //reference = Firebase.database.reference
        savedTemp.created=false
        //reference.child(savedTemp.path).removeEventListener(listener)
        super.onDestroy()
    }


    private lateinit var listenerAmigos: ValueEventListener


    private fun listarAmigos() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = context?.let { AmigosAdapter(it, ArrayList(RelacionesUsuario.amigos.values)) }

    }

    private fun launchCustomAlertDialog() {
        codigo = customAlertDialogView.findViewById(R.id.codigo)

        // Building the Alert dialog using materialAlertDialogBuilder instance
        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Añadir amigo")
            .setMessage("Ingrese el código del alumno")
            .setPositiveButton("Añadir") { dialog, _ ->
                val codeString = codigo.text.toString()

                if (codeString.length != 8) {
                    Toast.makeText(context, "Tiene que ser de 8 digitos", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    reference.child("codigos/${codeString}").get().addOnSuccessListener {
                        if (it.exists()) {
                            reference.child("relaciones/${it.value}/${User.uid}")
                                .setValue("solicitud")
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Se envio tu solicitud",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "No se pudo enviar tu solicitud",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(context, "No existe el usuario", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                        }
                    }
                }

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


}