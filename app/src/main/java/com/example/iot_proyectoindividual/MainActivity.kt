package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.iot_proyectoindividual.admin.MainAdminActivity
import com.example.iot_proyectoindividual.cliente.ClienteHomeActivity
import com.example.iot_proyectoindividual.databinding.ActivityMainBinding
import com.example.iot_proyectoindividual.entity.Ban
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar

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
                        //mAuth.currentUser?.isEmailVerified == true
                        if (it.isSuccessful && mAuth.currentUser?.isEmailVerified == true) {
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
            val intent = Intent(this, MainAdminActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
           //Toast.makeText(this, "Por hacer", Toast.LENGTH_SHORT).show()
        } else {

            if(User.usuario.banfin!=null){
                val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val timeBan = dateFormat.parse(User.usuario.banfin)
                val timeNow = Calendar.getInstance().time
                val ref = Firebase.database.reference
                if(timeBan < timeNow){
                    ref.child("usuarios/${User.uid}/ban").removeValue()
                    ref.child("usuarios/${User.uid}/banfin").removeValue()
                    ref.child("baneos/${User.usuario.ban}").removeValue()
                    User.usuario.ban=null
                    User.usuario.banfin=null
                    Toast.makeText(this,"Ha terminado su baneo",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ClienteHomeActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else{
                    ref.child("baneos/${User.usuario.ban}").get().addOnSuccessListener {
                        val ban = it.getValue<Ban>()
                        val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        val dateForma2 =  SimpleDateFormat("dd/MM/yyy-HH:mm:ss")
                        val fechaFin = dateForma2.format(dateFormat.parse(ban?.fin ?: "" ))
                        if(ban!=null){
                            mAuth.signOut()
                            MaterialAlertDialogBuilder(this).setTitle("Tu cuenta está baneada")
                                .setMessage("Razón: ${ban.razon}\nFin: ${fechaFin}\nSi cree que es un error contactese con ${ban.contacto}")
                                .setPositiveButton("aceptar"){_,_->
                                }.show()
                        }
                    }
                }
            } else{
                val intent = Intent(this, ClienteHomeActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
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