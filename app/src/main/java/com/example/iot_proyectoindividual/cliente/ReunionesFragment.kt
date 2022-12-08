package com.example.iot_proyectoindividual.cliente

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iot_proyectoindividual.adapter.ReunionesAdapter
import com.example.iot_proyectoindividual.databinding.FragmentReunionesBinding
import com.example.iot_proyectoindividual.entity.Amigo
import com.example.iot_proyectoindividual.entity.ReunionNotificacion
import com.example.iot_proyectoindividual.save.RelacionesUsuario
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class ReunionesFragment : Fragment() {


    private lateinit var binding : FragmentReunionesBinding
    private lateinit var reference: DatabaseReference

    private lateinit var recycle: RecyclerView
    private lateinit var invitaciones: ArrayList<ReunionNotificacion>

    object res{
        var should=false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReunionesBinding.inflate(layoutInflater)
        recycle = binding.recycleView
        reference = Firebase.database.reference


        reference.child("invitaciones/${User.uid}").addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                var listaInvitaciones = ArrayList<ReunionNotificacion>()
                for (i in snapshot.children){
                    if(i.exists()) {
                        var amigo = Amigo()
                        RelacionesUsuario.amigos[i.key]?.let { amigo = it }
                        val reuNot = i.getValue<String>()?.let { ReunionNotificacion(amigo, it) }
                        if (reuNot != null) {
                            listaInvitaciones.add(reuNot)
                        }
                    }

                }
                invitaciones = listaInvitaciones
                Log.d("Oliver","valor de res ${res.should}|")
                reloadLista()
                if(res.should){
                    res.should=false
                    Log.d("Oliver","aqui si pasa")
                    activity?.recreate()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return binding.root
    }


    private fun reloadLista(){
        recycle.layoutManager = LinearLayoutManager(context)
        recycle.adapter = context?.let { ReunionesAdapter(it,invitaciones) }
    }
}