package com.example.iot_proyectoindividual.cliente

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.iot_proyectoindividual.MainActivity
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.background.LocationService
import com.example.iot_proyectoindividual.databinding.ActivityClienteHomeBinding
import com.example.iot_proyectoindividual.entity.Amigo
import com.example.iot_proyectoindividual.entity.Ban
import com.example.iot_proyectoindividual.entity.ReunionEmpty
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.Coordenadas
import com.example.iot_proyectoindividual.save.Coordenadas.lat
import com.example.iot_proyectoindividual.save.RelacionesUsuario
import com.example.iot_proyectoindividual.save.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ClienteHomeActivity : AppCompatActivity() {

    private var listaAmigosFragment = ListaAmigosFragment()
    private var horarioFragment = HorarioFragment()
    private var perfilFragment = PerfilFragment()
    private var reunionesFragment = ReunionesFragment()
    private var activoFragment = ReunionActivaFragment()

    private lateinit var ref: DatabaseReference

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var binding: ActivityClienteHomeBinding
    private lateinit var context:Context

    object NavValue {
        var navPage: Int = 0;
        var ejecutado: Boolean = false
        var active: Boolean = true
        var ban: Ban? = null
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClienteHomeBinding.inflate(layoutInflater)


        ref = Firebase.database.reference
        context=this
        ref.child("activos/${User.uid}").get().addOnSuccessListener {
            if (it.exists()) {
                User.reunionId = it.getValue<String>()!!
                ref.child("reuniones/${it.getValue<String>()}").get()
                    .addOnSuccessListener { x ->
                        User.reunion = x.getValue<ReunionEmpty>()
                    }
            } else {
                User.reunion = null
                User.reunionId = ""
            }
        }

        ref.child("usuarios/${User.uid}/ban").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val banID = snapshot.getValue<String>()
                if(banID!=null){
                    ref.child("baneos/$banID").get().addOnSuccessListener {
                        val ban = it.getValue<Ban>()
                        if(ban!=null){
                            NavValue.ban = ban
                            closeBan(ban)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        setContentView(binding.root)

        if(!NavValue.ejecutado){
            NavValue.ejecutado=true
            Intent(this,LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
            }
        }


        when (NavValue.navPage) {
            0 -> {
                binding.bottomNavigationView.selectedItemId = R.id.amigos
                actionBar?.title = "Amigos"
                supportActionBar?.title = "Amigos"
                replaceFragment(listaAmigosFragment)
            }
            1 -> {
                binding.bottomNavigationView.selectedItemId = R.id.perfil

                actionBar?.title = "Perfil"
                supportActionBar?.title = "Perfil"
                replaceFragment(perfilFragment)

            }
            2 -> {
                binding.bottomNavigationView.selectedItemId = R.id.horario

                actionBar?.title = "Horario"
                supportActionBar?.title = "Horario"
                replaceFragment(horarioFragment)

            }
            3 -> {
                binding.bottomNavigationView.selectedItemId = R.id.reuniones
                actionBar?.title = "Reuniones"
                supportActionBar?.title = "Reuniones"
                if (User.reunion == null) {
                    replaceFragment(reunionesFragment)
                } else {
                    replaceFragment(activoFragment)
                }
            }
        }


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.amigos -> {
                    actionBar?.title = "Amigos"
                    supportActionBar?.title = "Amigos"
                    replaceFragment(listaAmigosFragment)
                    NavValue.navPage = 0
                }
                R.id.perfil -> {
                    actionBar?.title = "Perfil"
                    supportActionBar?.title = "Perfil"
                    replaceFragment(perfilFragment)
                    NavValue.navPage = 1
                }
                R.id.horario -> {
                    actionBar?.title = "Horario"
                    supportActionBar?.title = "Horario"
                    NavValue.navPage = 2
                    replaceFragment(horarioFragment)

                }
                R.id.reuniones -> {
                    actionBar?.title = "Reuniones"
                    supportActionBar?.title = "Reuniones"
                    NavValue.navPage = 3
                    if (User.reunion == null) {
                        replaceFragment(reunionesFragment)
                    } else {
                        replaceFragment(activoFragment)
                    }
                }

            }
            true
        }


    }

    override fun onStop() {
        super.onStop()
        NavValue.active=false
    }

    override fun onStart() {
        super.onStart()
        NavValue.active=true
        if(NavValue.ban!=null){
            closeBan(NavValue.ban!!)
        }
    }


    private fun closeBan(ban : Ban){
        if(!NavValue.active){
            return
        }
        val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dateForma2 =  SimpleDateFormat("dd/MM/yyy-HH:mm:ss")
        val fechaFin = dateForma2.format(dateFormat.parse(ban?.fin ?: "" ))

        MaterialAlertDialogBuilder(this).setTitle("Tu cuenta está baneada")
            .setMessage("Razón: ${ban.razon}\nFin: ${fechaFin}\nSi cree que es un error contactese con ${ban.contacto}")
            .setPositiveButton("aceptar"){_,_->
                Firebase.auth.signOut()
                User.usuario = Usuario()
                User.uid = ""
                RelacionesUsuario.amigos = hashMapOf()
                RelacionesUsuario.lista = hashMapOf()
                NavValue.navPage = 0
                listaAmigosFragment.reset()
                User.reunion = null
                User.reunionId = ""
                NavValue.ejecutado=false
                NavValue.ban = null
                Intent(this,LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                    startService(this)
                }
                intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }.setCancelable(false).show()

    }


    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
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
                        NavValue.navPage = 0
                        listaAmigosFragment.reset()
                        User.reunion = null
                        User.reunionId = ""
                        NavValue.ejecutado=false
                        NavValue.ban = null

                        Intent(this,LocationService::class.java).apply {
                            action = LocationService.ACTION_STOP
                            startService(this)
                        }

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

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }


}