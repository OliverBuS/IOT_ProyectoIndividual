package com.example.iot_proyectoindividual.cliente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iot_proyectoindividual.adapter.InvitadosAdapter
import com.example.iot_proyectoindividual.databinding.ActivityVerInvitadosBinding
import com.example.iot_proyectoindividual.entity.Amigo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class VerInvitadosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerInvitadosBinding
    private lateinit var reference: DatabaseReference

    private lateinit var recycle: RecyclerView

    private lateinit var listener: ValueEventListener
    private var listenerHijo: ValueEventListener?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerInvitadosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reference = Firebase.database.reference

        recycle = binding.recycleView
        val id = intent.getStringExtra("idR")!!

        val invRelacion = hashMapOf<String, String>()
        listener =
            reference.child("invitados/$id").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        if (i.exists()) {
                            i.key?.let { it1 ->
                                i.getValue<String>()
                                    ?.let { it2 -> invRelacion.put(it1, it2) }
                            }
                            //Toast.makeText(binding.root.context, i.getValue<String>(), Toast.LENGTH_SHORT).show()
                        }
                    }


                    if (listenerHijo != null) {
                        reference.child("amigos").removeEventListener(listenerHijo!!)
                    }

                    listenerHijo = reference.child("amigos")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot2: DataSnapshot) {
                                val listaInvitados = ArrayList<Amigo>()
                                for (i in snapshot2.children) {
                                    if (i.exists() && invRelacion.containsKey(i.key)) {
                                        val amigo = i.getValue<Amigo>()
                                        if (amigo != null) {
                                            amigo.tipo = invRelacion.get(i.key)
                                            listaInvitados.add(amigo)
                                        }
                                    }
                                }
                                recycle.layoutManager = LinearLayoutManager(binding.root.context)
                                recycle.adapter =
                                    InvitadosAdapter(binding.root.context, listaInvitados)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })


                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    override fun onDestroy() {
        //reference.child("invitados/$id").removeEventListener(listener)
        super.onDestroy()
    }
}
