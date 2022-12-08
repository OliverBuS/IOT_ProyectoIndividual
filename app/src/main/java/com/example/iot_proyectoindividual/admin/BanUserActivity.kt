package com.example.iot_proyectoindividual.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.iot_proyectoindividual.databinding.ActivityBanUserBinding
import com.example.iot_proyectoindividual.entity.Ban
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar

class BanUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBanUserBinding
    private lateinit var usuario: Usuario
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBanUserBinding.inflate(layoutInflater)
        val opciones = arrayOf("1 semana", "1 mes", "1 minuto")
        val adapter = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,opciones)
        binding.tiempoSpinner.adapter = adapter

        reference = Firebase.database.reference

        usuario = intent.getSerializableExtra("usuario") as Usuario

        binding.nombreUsuario.text = usuario.nombre
        Glide.with(this).load(Firebase.storage.reference.child("perfil/${usuario.imagen}")).into(binding.imgPerfil)
        binding.estadoUsuario.text = usuario.estado
        binding.buCancelar.setOnClickListener {
            finish()
        }

        binding.buCompletar.setOnClickListener {
            ban()
        }

        setContentView(binding.root)
    }

    private fun ban(){

        val descp = binding.edRazon.text
        val contacto = User.usuario.correo
        if(descp.isEmpty()){
            binding.edRazon.error="Llene el campo"
            return
        }
        val tiempoOption = binding.tiempoSpinner.selectedItemPosition

        var fechaFin : String
        val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val timeNow = Calendar.getInstance()

        if(tiempoOption==0){
            timeNow.add(Calendar.DAY_OF_MONTH,7)
        } else if(tiempoOption==1){
            timeNow.add(Calendar.DAY_OF_MONTH,30)
        } else{
            timeNow.add(Calendar.MINUTE,1)
        }
        fechaFin = dateFormat.format(timeNow.time)

        val ban = Ban()
        ban.contacto = User.usuario.correo
        ban.razon = descp.toString()
        ban.fin = fechaFin

        val key = reference.child("baneos").push().key
        reference.child("baneos/$key").setValue(ban).addOnSuccessListener {
            reference.child("usuarios/${usuario.key}/ban").setValue(key).addOnSuccessListener {
                reference.child("usuarios/${usuario.key}/banfin").setValue(fechaFin).addOnSuccessListener{
                    Toast.makeText(this,"Baneado con exito el usuario",Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this,"Hubo un error",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,"Hubo un error",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Hubo un error",Toast.LENGTH_SHORT).show()
        }


    }

}