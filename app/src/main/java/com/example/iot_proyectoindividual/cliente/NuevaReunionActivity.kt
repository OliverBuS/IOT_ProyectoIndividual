package com.example.iot_proyectoindividual.cliente

import android.app.TimePickerDialog
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.ActivityNuevaReunionBinding
import com.example.iot_proyectoindividual.save.Coordenadas
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class NuevaReunionActivity : AppCompatActivity() , OnMapReadyCallback,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private lateinit var map : GoogleMap
    private lateinit var binding : ActivityNuevaReunionBinding
    private var hora = 0
    private var minutos=0



    private var coordinates = LatLng(Coordenadas.lat,Coordenadas.lon)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNuevaReunionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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
            //TODO
        }

        binding.buCrear.setOnClickListener {
            //TODO
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