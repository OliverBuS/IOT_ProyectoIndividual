package com.example.iot_proyectoindividual.cliente

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
import com.example.iot_proyectoindividual.databinding.ActivityClienteHomeBinding
import com.example.iot_proyectoindividual.entity.Amigo
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.RelacionesUsuario
import com.example.iot_proyectoindividual.save.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ClienteHomeActivity : AppCompatActivity() {

    private var listaAmigosFragment = ListaAmigosFragment()
    private var horarioFragment = HorarioFragment()
    private var perfilFragment = PerfilFragment()
    private var reunionesFragment = ReunionesFragment()

    private lateinit var ref : DatabaseReference

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var binding: ActivityClienteHomeBinding
    object  NavValue { var navPage:Int =0; var ejecutado:Boolean = false }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        ref = Firebase.database.reference
        binding = ActivityClienteHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getLocation()


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


    private fun getLocation(){
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }

        task.addOnSuccessListener {
            if(it !=null){
                Toast.makeText(this,"${it.latitude} ${it.longitude}",Toast.LENGTH_SHORT).show()
            }
        }

        val amigo  = Amigo()
        amigo.disponible = User.usuario.horario?.disponible()
        amigo.estado = User.usuario.estado
        amigo.imagen = User.usuario.imagen
        amigo.nombre = User.usuario.nombre

        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date: String = df.format(Calendar.getInstance().time)
        amigo.time= date
        Firebase.database.reference.child("amigos/${User.uid}").setValue(amigo)

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
                        RelacionesUsuario.amigos = hashMapOf()
                        RelacionesUsuario.lista = hashMapOf()
                        NavValue.navPage=0
                        listaAmigosFragment.reset()
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



}