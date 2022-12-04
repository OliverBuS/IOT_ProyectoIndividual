package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iot_proyectoindividual.databinding.ActivityCrearCuentaFinBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CrearCuentaFinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCrearCuentaFinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.auth.signOut()
        super.onCreate(savedInstanceState)
        binding= ActivityCrearCuentaFinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonContinuar.setOnClickListener{
            intent = Intent(this,MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}