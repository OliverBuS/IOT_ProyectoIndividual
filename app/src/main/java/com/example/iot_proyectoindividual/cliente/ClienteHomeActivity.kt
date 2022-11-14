package com.example.iot_proyectoindividual.cliente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.ActivityClienteHomeBinding

class ClienteHomeActivity : AppCompatActivity() {

    private val listaAmigosFragment = ListaAmigos()
    private val horarioFragment = Horario()
    private val perfilFragment = Perfil()
    private val reunionesFragment = Reuniones()


    private lateinit var binding: ActivityClienteHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_home)
        replaceFragment(listaAmigosFragment)

        binding = ActivityClienteHomeBinding.inflate(layoutInflater)


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
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout , fragment)
            transaction.commit()
        }
    }

}