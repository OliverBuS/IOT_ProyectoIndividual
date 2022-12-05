package com.example.iot_proyectoindividual.cliente

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.ActivityNuevaReunionBinding
import com.example.iot_proyectoindividual.entity.Reunion
import com.example.iot_proyectoindividual.entity.ReunionEmpty
import com.example.iot_proyectoindividual.save.Coordenadas
import com.example.iot_proyectoindividual.save.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class NuevaReunionActivity : AppCompatActivity() , OnMapReadyCallback,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private lateinit var map : GoogleMap
    private lateinit var binding : ActivityNuevaReunionBinding
    private lateinit var reference: DatabaseReference
    private var hora = 12
    private var minutos=0

    private var disponible=0
    private var uid : String? = null

    private var coordinates = LatLng(Coordenadas.lat,Coordenadas.lon)

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        reference = Firebase.database.reference
        supportActionBar?.title = "Crear Reunión"
        binding = ActivityNuevaReunionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        disponible = intent.getIntExtra("disponible",0)
        uid = intent.getStringExtra("uid")


        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Seleccione la hora")
            .setPositiveButtonText("OK")
            .setNegativeButtonText("Cancelar")
            .build()

        picker.addOnPositiveButtonClickListener{
            hora = picker.hour
            minutos = picker.minute
            binding.horaInput.text = String.format(Locale.getDefault(),"%02d:%02d",hora,minutos,true)
        }



        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.horaInput.setOnClickListener{
            picker.show(supportFragmentManager,"tag")
        }

        binding.buCancelar.setOnClickListener {
            finish()
        }

        binding.buCrear.setOnClickListener {
            crearReunion()
        }


    }


    private fun crearReunion(){
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinutes = calendar.get(Calendar.MINUTE)
        if( (hora<currentHour && minutos<currentMinutes)){
            Toast.makeText(this,"La hora no es correcta",Toast.LENGTH_SHORT).show()
            return
        }else if( hora< disponible){
            Toast.makeText(this,"Tu amigo no está disponible",Toast.LENGTH_SHORT).show()
            return
        } else if( hora-currentHour>3){
            Toast.makeText(this,"No se permite crear excediendo las 3 horas",Toast.LENGTH_SHORT).show()
        }
        val descText = binding.edDesc.text.toString().trim()

        if(descText.isEmpty()){
            binding.edDesc.error="Describa la reunión"
            return
        }

        val reunion = Reunion(descText,binding.horaInput.text.toString(),coordinates.latitude,coordinates.longitude)
        val refReunion = reference.child("reuniones")
        val reuId = refReunion.push().key
        if (reuId != null) {
            refReunion.child(reuId).setValue(reunion).addOnSuccessListener {
               reference.child("invitados/$reuId/$uid").setValue("pendiente").addOnSuccessListener {
                   reference.child("invitaciones/$uid/${User.uid}").setValue(reuId).addOnSuccessListener {
                       reference.child("invitados/$reuId/${User.uid}").setValue("asiste").addOnSuccessListener {
                           reference.child("activos/${User.uid}").setValue(reuId).addOnSuccessListener {
                               Toast.makeText(this,"Reunion Creada con exito",Toast.LENGTH_SHORT).show()
                               User.reunion= ReunionEmpty()
                               User.reunion!!.hora = reunion.hora
                               User.reunion!!.descripcion = reunion.descripcion
                               User.reunion!!.latitud = reunion.latitud
                               User.reunion!!.longitud = reunion.longitud
                               User.reunionId=reuId
                               finish()
                           }
                       }
                   }
               }
            }
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        map = p0
        this.map.setOnMapClickListener(this)
        this.map.setOnMapLongClickListener(this)

        map.addMarker(MarkerOptions().position(coordinates).title("PUCP"))
        map.moveCamera(CameraUpdateFactory.newLatLng(coordinates))

    }

    override fun onMapClick(p0: LatLng) {
        coordinates=p0
        map.clear()

        val lugar = LatLng(p0.latitude,p0.longitude)
        map.addMarker(MarkerOptions().position(lugar).title(""))
        map.moveCamera(CameraUpdateFactory.newLatLng(lugar))
    }

    override fun onMapLongClick(p0: LatLng) {
        coordinates=p0
        val lugar = LatLng(p0.latitude,p0.longitude)
        map.addMarker(MarkerOptions().position(lugar).title(""))
        map.moveCamera(CameraUpdateFactory.newLatLng(lugar))
    }
}