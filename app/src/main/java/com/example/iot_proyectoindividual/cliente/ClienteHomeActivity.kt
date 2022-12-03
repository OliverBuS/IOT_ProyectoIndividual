package com.example .iot_proyectoindividual.cliente

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
    object  NavValue {
        var navPage:Int =0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClienteHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when(NavValue.navPage){
            0->{
                actionBar?.title = "Amigos"
                supportActionBar?.title = "Amigos"
                replaceFragment(listaAmigosFragment)
            }
            1->{
                actionBar?.title = "Perfil"
                supportActionBar?.title = "Perfil"
                replaceFragment(perfilFragment)

            }
            2->{
                actionBar?.title = "Horario"
                supportActionBar?.title = "Horario"
                replaceFragment(horarioFragment)

            }
            3->{
                actionBar?.title = "Reuniones"
                supportActionBar?.title = "Reuniones"
                replaceFragment(reunionesFragment)
            }
        }



        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.amigos -> {
                    actionBar?.title = "Amigos"
                    supportActionBar?.title = "Amigos"
                    replaceFragment(listaAmigosFragment)
                    NavValue.navPage=0
                }
                R.id.perfil -> {
                    actionBar?.title = "Perfil"
                    supportActionBar?.title = "Perfil"
                    replaceFragment(perfilFragment)
                    NavValue.navPage=1
                }
                R.id.horario -> {
                    actionBar?.title = "Horario"
                    supportActionBar?.title = "Horario"
                    replaceFragment(horarioFragment)
                    NavValue.navPage=2
                }
                R.id.reuniones -> {
                    actionBar?.title = "Reuniones"
                    supportActionBar?.title = "Reuniones"
                    replaceFragment(reunionesFragment)
                    NavValue.navPage=3
                }

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