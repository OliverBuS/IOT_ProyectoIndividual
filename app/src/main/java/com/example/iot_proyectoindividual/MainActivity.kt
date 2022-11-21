package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.iot_proyectoindividual.cliente.ClienteHomeActivity
import com.example.iot_proyectoindividual.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen =installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Thread.sleep(500)
        splashScreen.setKeepOnScreenCondition{
            false
        }

        binding.buttonSesion.setOnClickListener{
            iniciarSesion()

        }

        binding.textRecuperar.setOnClickListener{
            recuperar()
        }

        binding.textRegistrate.setOnClickListener{
            registrar()
        }

    }


    fun iniciarSesion(){
        val intent = Intent(this,ClienteHomeActivity::class.java)
        startActivity(intent)
    }



    fun registrar(){
        val intent = Intent(this, CrearCuentaActivity::class.java)
        startActivity(intent)
    }

    fun recuperar(){
        val intent = Intent(this, RecuperarCuentaActivity::class.java)
        startActivity(intent)
    }

}