package com.example.iot_proyectoindividual.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.iot_proyectoindividual.databinding.ActivityUnbanUserBinding
import com.example.iot_proyectoindividual.entity.Ban
import com.example.iot_proyectoindividual.entity.Usuario
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UnbanUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUnbanUserBinding
    private lateinit var usuario: Usuario
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnbanUserBinding.inflate(layoutInflater)
        reference = Firebase.database.reference
        usuario = intent.getSerializableExtra("usuario") as Usuario

        binding.nombreUsuario.text = usuario.nombre
        Glide.with(this).load(Firebase.storage.reference.child("perfil/${usuario.imagen}")).into(binding.imgPerfil)
        binding.estadoUsuario.text = usuario.estado

        binding.buCancelar.setOnClickListener {
            finish()
        }

        reference.child("baneos/${usuario.ban}").get().addOnSuccessListener {
            if(it.exists()){
                val ban = it.getValue<Ban>()
                if (ban != null) {
                    binding.fechaFin.text = ban.fin
                    binding.edRazon.text = ban.razon
                }
            }
        }


        binding.buCompletar.setOnClickListener {
            desBanear()
        }

        setContentView(binding.root)
    }

    private fun desBanear(){

        reference.child("baneos/${usuario.ban}").removeValue().addOnSuccessListener {
            reference.child("usuarios/${usuario.key}/ban").removeValue().addOnSuccessListener {
                reference.child("usuarios/${usuario.key}/banfin").removeValue().addOnSuccessListener {
                    Toast.makeText(this,"Se ha quitado el ban al usuario",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

}