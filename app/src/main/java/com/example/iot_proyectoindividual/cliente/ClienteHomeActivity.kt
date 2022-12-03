package com.example.iot_proyectoindividual.cliente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.ActivityClienteHomeBinding
import com.example.iot_proyectoindividual.save.User

class ClienteHomeActivity : AppCompatActivity() {

    private val listaAmigosFragment = ListaAmigos()
    private val horarioFragment = Horario()
    private val perfilFragment = Perfil()
    private val reunionesFragment = Reuniones()


    private lateinit var binding: ActivityClienteHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClienteHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(listaAmigosFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.amigos -> replaceFragment(listaAmigosFragment)
                R.id.perfil -> replaceFragment(perfilFragment)
                R.id.horario -> replaceFragment(horarioFragment)
                R.id.reuniones -> replaceFragment(reunionesFragment)


            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout , fragment)
        transaction.commit()
    }

}