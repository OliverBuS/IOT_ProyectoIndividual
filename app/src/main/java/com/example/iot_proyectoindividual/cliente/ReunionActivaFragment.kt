package com.example.iot_proyectoindividual.cliente

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.databinding.FragmentReunionActivaBinding
import com.example.iot_proyectoindividual.entity.Reunion
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


class ReunionActivaFragment : Fragment(), OnMapReadyCallback {


    private lateinit var binding : FragmentReunionActivaBinding
    private lateinit var map : GoogleMap
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReunionActivaBinding.inflate(layoutInflater)
        reference = Firebase.database.reference

        binding.horaInput.text = User.reunion?.hora ?: ""
        binding.edDesc.text = User.reunion?.descripcion ?: ""


        val mapFragment = childFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.buVerInvitados.setOnClickListener {
            val intent = Intent(binding.root.context,VerInvitadosActivity::class.java)
            intent.putExtra("idR",User.reunionId)
            startActivity(intent)
        }
        binding.buSalir.setOnClickListener {
            reference.child("invitados/${User.reunionId}/${User.uid}").removeValue().addOnSuccessListener {
                reference.child("activos/${User.uid}").removeValue().addOnSuccessListener {
                    User.reunionId =""
                    User.reunion = null
                    activity?.recreate()
                }
            }
        }


        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        val lat : Double = User.reunion?.latitud!!
        val lon : Double = User.reunion?.longitud!!
        val lugar = LatLng(lat,lon)
        map.addMarker(MarkerOptions().position(lugar).title(""))
        map.moveCamera(CameraUpdateFactory.newLatLng(lugar))
    }


}