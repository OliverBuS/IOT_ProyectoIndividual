package com.example.iot_proyectoindividual.cliente

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.ActivityVerReunionBinding
import com.example.iot_proyectoindividual.entity.ReunionEmpty
import com.example.iot_proyectoindividual.save.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VerReunionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityVerReunionBinding
    private lateinit var map : GoogleMap

    private lateinit var hora : String
    private lateinit var desc : String
    private  var lat = 0.0//-12.0
    private  var lon = 0.0//-77.0
    private lateinit var idReunion : String
    private lateinit var idAmigo : String


    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerReunionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detalles"
        reference = Firebase.database.reference

        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        idReunion = intent.getStringExtra("idR")!!
        idAmigo = intent.getStringExtra("idA")!!
        hora = intent.getStringExtra("hora")!!
        desc = intent.getStringExtra("desc")!!
        lat = intent.getDoubleExtra("lat",0.0)
        lon = intent.getDoubleExtra("lon",0.0)


        binding.horaInput.text = hora
        binding.edDesc.text = desc

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.buAceptar.setOnClickListener {
            reference.child("invitados/$idReunion/${User.uid}").setValue("asiste").addOnSuccessListener {
                reference.child("invitaciones/${User.uid}").removeValue().addOnSuccessListener {
                    reference.child("activos/${User.uid}").setValue(idReunion).addOnSuccessListener {
                        Toast.makeText(this,"Se acepto la invitación",Toast.LENGTH_SHORT).show()
                        User.reunion= ReunionEmpty()
                        User.reunion!!.hora = hora
                        User.reunion!!.descripcion = desc
                        User.reunion!!.latitud = lat
                        User.reunion!!.longitud = lon
                        User.reunionId=idReunion
                        finish()
                    }
                }
            }
        }

        binding.buRechazar.setOnClickListener {
            reference.child("invitados/$idReunion/${User.uid}").setValue("cancela").addOnSuccessListener {
                reference.child("invitaciones/${User.uid}").removeValue().addOnSuccessListener {
                    Toast.makeText(this,"Se rechazo la invitación",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        binding.buInvitados.setOnClickListener {
            val intent = Intent(this,VerInvitadosActivity::class.java)
            intent.putExtra("idR",idReunion)
            startActivity(intent)
        }

    }



    override fun onMapReady(p0: GoogleMap) {
        map = p0
        val lugar = LatLng(lat,lon)
        map.addMarker(MarkerOptions().position(lugar).title(""))
        map.moveCamera(CameraUpdateFactory.newLatLng(lugar))
    }

}