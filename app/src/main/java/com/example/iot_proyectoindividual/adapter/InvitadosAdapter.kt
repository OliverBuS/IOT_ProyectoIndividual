package com.example.iot_proyectoindividual.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.cliente.NuevaReunionActivity
import com.example.iot_proyectoindividual.entity.Amigo
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class InvitadosAdapter(var context: Context, var lista: ArrayList<Amigo>) :
    RecyclerView.Adapter<InvitadosAdapter.ViewHolder>() {

    val listaAmigos: ArrayList<Amigo> = lista

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.recycle_invitados, viewGroup, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val amigo = listaAmigos[i]
        viewHolder.teNombre.text = amigo.nombre
        viewHolder.teEstado.text = amigo.estado
        Glide.with(context).load(Firebase.storage.reference.child("perfil/${amigo.imagen}"))
            .into(viewHolder.profImage)

        if (amigo.tipo == "asiste") {
            viewHolder.teTipo.text = "Asistencia Confirmada"
            viewHolder.teTipo.setTextColor(Color.parseColor("#5DB16F"))

        } else if(amigo.tipo=="pendiente"){
            viewHolder.teTipo.text = "Pendiente"
            viewHolder.teTipo.setTextColor(Color.parseColor("#49BDB6"))

        } else{
            viewHolder.teTipo.text = "No asiste"
            viewHolder.teTipo.setTextColor(Color.parseColor("#971C1C"))
        }

    }

    override fun getItemCount(): Int {
        return listaAmigos.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profImage: ImageView
        var teNombre: TextView
        var teEstado: TextView
        var teTipo: TextView

        init {
            profImage = itemView.findViewById(R.id.imgPerfil)
            teNombre = itemView.findViewById(R.id.teNombre)
            teEstado = itemView.findViewById(R.id.teEstado)
            teTipo = itemView.findViewById(R.id.teTipo)

        }
    }
}