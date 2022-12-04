package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.iot_proyectoindividual.databinding.ActivityRecuperarCuentaBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecuperarCuentaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecuperarCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityRecuperarCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonContinuar.setOnClickListener{
            val correo = binding.editTextCorreo.text.toString()
            if(correo.isEmpty() or correo.isEmailValid()){
                Toast.makeText(this,"Correo no valido",Toast.LENGTH_SHORT).show()
            } else{
                Firebase.auth.sendPasswordResetEmail(correo).addOnCompleteListener{
                    if(it.isSuccessful){
                        intent = Intent(this,RecuperarCuentaFinActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else{
                        Toast.makeText(this,"No se pudo enviar el correo",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

}