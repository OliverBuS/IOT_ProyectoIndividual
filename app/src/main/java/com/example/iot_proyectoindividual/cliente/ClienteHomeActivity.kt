package com.example.iot_proyectoindividual.cliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.iot_proyectoindividual.MainActivity
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.ActivityClienteHomeBinding
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ClienteHomeActivity : AppCompatActivity() {

    private val listaAmigosFragment = ListaAmigosFragment()
    private val horarioFragment = HorarioFragment()
    private val perfilFragment = PerfilFragment()
    private val reunionesFragment = ReunionesFragment()

    private lateinit var ref : DatabaseReference


    private lateinit var binding: ActivityClienteHomeBinding
    object  NavValue { var navPage:Int =0; var ejecutado:Boolean = false }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ref = Firebase.database.reference
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



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.logout,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logOut->{

                MaterialAlertDialogBuilder(this).setTitle("Cerrar Sesión")
                    .setMessage("¿Está seguro que quiere cerrar sesión?")
                    .setNegativeButton("Cancelar"){_,_-> }
                    .setPositiveButton("Seguro"){_,_->
                        Firebase.auth.signOut()
                        User.usuario = Usuario()
                        User.uid=""
                        intent = Intent(this, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                    .show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout , fragment)
        transaction.commit()
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        NavValue.navPage=0

        super.onActionModeFinished(mode)
    }

}