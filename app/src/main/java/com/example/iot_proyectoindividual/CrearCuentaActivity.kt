package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.iot_proyectoindividual.databinding.ActivityCrearCuentaBinding
import com.example.iot_proyectoindividual.entity.Horario
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCrearCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonContinuar.setOnClickListener{
            registrarUsuario()
        }
    }

    private lateinit var database: DatabaseReference

    fun registrarUsuario(){
        val nombre = binding.editTextNombre.text.toString().trim()
        val codigo = binding.editTextCodigo.text.toString().trim()
        val correo = binding.editTextCorreo.text.toString().trim()
        val pass1 = binding.editTextPassword.text.toString().trim()
        val pass2  = binding.editTextPassword2.text.toString().trim()

        database = Firebase.database.reference

        var error = false

        if(nombre.isEmpty()){
            binding.editTextNombre.error = "Llene el campo"
            error=true
        }
        if(codigo.isEmpty() || codigo.length!=8){
            binding.editTextCodigo.error="Llene el campo correctamente"
            error=true
        }
        if(correo.isEmpty()){
            binding.editTextCorreo.error= "Llene el campo"
            error=true
        }
        if(pass1.isEmpty()){
            binding.editTextPassword.error= "Llene el campo"
            error=true
        } else if(pass1 != pass2){
            binding.editTextPassword2.error="Las contraseñas no concuerdan"
            error=true
        }

        if(!error){

            database.child("correos/${codigo}").get().addOnSuccessListener {
                if(it.exists()){
                    Toast.makeText(this, "El alumno con el código ya está registrado",Toast.LENGTH_SHORT).show()
                } else{
                    Firebase.auth.createUserWithEmailAndPassword(correo,pass1).addOnCompleteListener{
                        if(it.isSuccessful){
                            Firebase.auth.currentUser?.sendEmailVerification()
                            val uid = it.result.user?.uid
                            database.child("correos/${codigo}").setValue(correo).addOnSuccessListener {
                                val usuario = Usuario()
                                usuario.ban=""
                                usuario.codigo = codigo
                                usuario.correo = correo
                                usuario.estado = "¡Hola!"
                                val horario = Horario()
                                horario.lunes=0
                                horario.martes=0
                                horario.miercoles=0
                                horario.jueves=0
                                horario.viernes=0
                                horario.sabado=0
                                usuario.horario=horario
                                usuario.nombre=nombre
                                usuario.rol="usuario"
                                usuario.imagen="default/img.jpg"
                                database.child("usuarios/${uid}").setValue(usuario).addOnSuccessListener {
                                    intent = Intent(this,CrearCuentaFinActivity::class.java)
                                    intent.flags=(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                }.addOnFailureListener{
                                    Toast.makeText(this, "Hubo un problema terminando el registro",Toast.LENGTH_SHORT).show()
                                }

                            }.addOnFailureListener {
                                Toast.makeText(this, "No se pudo completar el registro",Toast.LENGTH_SHORT).show()
                            }
                        } else{
                            Toast.makeText(this, "No se pudo completar el registro",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


        }
    }


}