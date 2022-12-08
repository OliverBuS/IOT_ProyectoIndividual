package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.iot_proyectoindividual.databinding.ActivityCrearCuentaBinding
import com.example.iot_proyectoindividual.entity.Horario
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.requests.Alumno
import com.example.iot_proyectoindividual.requests.Request
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CrearCuentaActivity : AppCompatActivity() {


    private var request  = Request(false, emptyArray())
    private lateinit var binding : ActivityCrearCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonContinuar.setOnClickListener{
            registrarUsuario()
        }



    }


    private fun fetchCode(code :String): Thread
    {
        return Thread{
            val url = URL("https://function-estudiantes.azurewebsites.net/api/codigo/$code")
            val connection =  url.openConnection() as HttpURLConnection
            if(connection.responseCode == 200){
                val inputStream = connection.inputStream
                val inputStreamReader = InputStreamReader(inputStream,"UTF-8")
                request = Gson().fromJson(inputStreamReader, Request::class.java)
                inputStreamReader.close()
            } else{
                request = Request(false, emptyArray())
            }
            checkCode(request)
        }
    }
    
    private fun checkCode(request: Request)
    {
        runOnUiThread{
            kotlin.run {
                if(request.success){
                } else{
                    Toast.makeText(this,"No existe es código del alumno",Toast.LENGTH_SHORT).show()
                }
            }
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

            val check =fetchCode(codigo)
            check.start()
            check.join()


            if(request.success && request.data[0].email != correo){
                Toast.makeText(this,"El correo no pertenece al alumno",Toast.LENGTH_SHORT).show()
                return
            } else if(!request.success){
                return
            }
            Toast.makeText(this,"Se verifico el correo y código de alumno",Toast.LENGTH_SHORT).show()

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
                                    database.child("codigos/${codigo}").setValue(uid).addOnSuccessListener {
                                        intent = Intent(this,CrearCuentaFinActivity::class.java)
                                        intent.flags=(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                    }.addOnFailureListener {
                                        Toast.makeText(this, "Hubo un problema terminando el registro",Toast.LENGTH_SHORT).show()
                                    }
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