package com.example.iot_proyectoindividual.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iot_proyectoindividual.databinding.FragmentAdminInfoBinding
import com.example.iot_proyectoindividual.save.User


class AdminInfoFragment : Fragment() {


    private lateinit var binding : FragmentAdminInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminInfoBinding.inflate(layoutInflater)

        binding.nombre.text= User.usuario.nombre
        binding.correo.text= User.usuario.correo
        return binding.root
    }

}