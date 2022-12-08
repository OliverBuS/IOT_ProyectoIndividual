package com.example.iot_proyectoindividual.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.iot_proyectoindividual.MainActivity
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.background.LocationService
import com.example.iot_proyectoindividual.cliente.ClienteHomeActivity
import com.example.iot_proyectoindividual.cliente.HorarioFragment
import com.example.iot_proyectoindividual.cliente.ListaAmigosFragment
import com.example.iot_proyectoindividual.databinding.ActivityMainAdminBinding
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.RelacionesUsuario
import com.example.iot_proyectoindividual.save.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class MainAdminActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainAdminBinding

    private var admiFragment = AdminInfoFragment()
    private var usuariosFragment = ListaUsuariosFragment()
    private lateinit var ref: DatabaseReference

    object NavValueAdmin {
        var navPage: Int = 0;
        var ejecutado: Boolean = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)


        when (NavValueAdmin.navPage) {
            0 -> {
                binding.bottomNavigationView.selectedItemId = R.id.usuario
                actionBar?.title = "Usuarios"
                supportActionBar?.title = "Usuarios"
                replaceFragment(usuariosFragment)
                NavValueAdmin.navPage = 0
            }
            1 -> {
                binding.bottomNavigationView.selectedItemId = R.id.admin

                actionBar?.title = "Perfil"
                supportActionBar?.title = "Perfil"
                replaceFragment(admiFragment)
                NavValueAdmin.navPage = 1

            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.usuario -> {
                    actionBar?.title = "Usuarios"
                    supportActionBar?.title = "Usuarios"
                    replaceFragment(usuariosFragment)
                    NavValueAdmin.navPage = 0
                }
                R.id.admin -> {
                    actionBar?.title = "Perfil"
                    supportActionBar?.title = "Perfil"
                    replaceFragment(admiFragment)
                    NavValueAdmin.navPage = 1
                }
            }
            true
        }




        setContentView(binding.root)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.logout, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {

                MaterialAlertDialogBuilder(this).setTitle("Cerrar Sesión")
                    .setMessage("¿Está seguro que quiere cerrar sesión?")
                    .setNegativeButton("Cancelar") { _, _ -> }
                    .setPositiveButton("Seguro") { _, _ ->
                        Firebase.auth.signOut()
                        User.usuario = Usuario()
                        User.uid = ""
                        RelacionesUsuario.amigos = hashMapOf()
                        RelacionesUsuario.lista = hashMapOf()
                        NavValueAdmin.navPage = 0
                        User.reunion = null
                        User.reunionId = ""
                        NavValueAdmin.ejecutado=false

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


}