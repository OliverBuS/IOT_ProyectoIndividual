package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.iot_proyectoindividual.cliente.ClienteHomeActivity
import com.example.iot_proyectoindividual.databinding.ActivityMainBinding
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.reference
        mAuth = Firebase.auth


        binding.buttonSesion.setOnClickListener {
            iniciarSesion()

        }

        binding.textRecuperar.setOnClickListener {
            recuperar()
        }

        binding.textRegistrate.setOnClickListener {
            registrar()
        }

    }


    fun iniciarSesion() {
        val intent = Intent(this, ClienteHomeActivity::class.java)

        val codigo = binding.edCodigo.text.toString()
        val password = binding.edPassword.text.toString()

        if (codigo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Llene los campos", Toast.LENGTH_SHORT).show()
            return
        }

        database.child("correos/$codigo").get().addOnSuccessListener {
            if (it.exists()) {
                val correo = it.value.toString()
                mAuth.signInWithEmailAndPassword(correo, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful &&  (mAuth.currentUser?.isEmailVerified == true or true)) {
                            User.uid= mAuth.currentUser!!.uid
                            database.child("usuarios/${mAuth.currentUser?.uid}").get()
                                .addOnSuccessListener {
                                    if(it.exists()){
                                        User.usuario = it.getValue<Usuario>()!!
                                        goHome()
                                    } else{
                                        Toast.makeText(
                                            this,
                                            "No hay registros del usuario",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }.addOnFailureListener {
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Las credenciales son incorrectas",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "No existe el usuario indicado", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Hubo un error en el logeo", Toast.LENGTH_SHORT).show()
        }


    }

    fun goHome() {
        if (User.usuario?.rol.equals("admin")) {
            Toast.makeText(this, "Por hacer", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, ClienteHomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

    }

    fun registrar() {
        val intent = Intent(this, CrearCuentaActivity::class.java)
        startActivity(intent)
    }

    fun recuperar() {
        val intent = Intent(this, RecuperarCuentaActivity::class.java)
        startActivity(intent)
    }

}