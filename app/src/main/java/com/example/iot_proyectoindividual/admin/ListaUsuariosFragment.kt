package com.example.iot_proyectoindividual.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.adapter.UsuariosAdapter
import com.example.iot_proyectoindividual.databinding.FragmentListaUsuariosBinding
import com.example.iot_proyectoindividual.entity.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class ListaUsuariosFragment : Fragment() {


    private lateinit var binding : FragmentListaUsuariosBinding
    private lateinit var reference: DatabaseReference
    private lateinit var recycle : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentListaUsuariosBinding.inflate(layoutInflater)
        reference = Firebase.database.reference
        recycle = binding.recycleView


        reference.child("usuarios").orderByChild("rol").equalTo("usuario").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuariosLista = ArrayList<Usuario>()
                for(children in snapshot.children){
                    if(children.exists()){
                        val usuarioTemp = children.getValue<Usuario>()
                        if(usuarioTemp!=null){
                            usuarioTemp.key = children.key
                            usuariosLista.add(usuarioTemp)
                        }
                    }
                }
                recycle.layoutManager = LinearLayoutManager(context)
                recycle.adapter = context?.let { UsuariosAdapter(it,usuariosLista) }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


        return binding.root
    }


}