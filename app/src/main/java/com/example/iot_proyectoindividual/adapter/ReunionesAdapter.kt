package com.example.iot_proyectoindividual.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.cliente.VerReunionActivity
import com.example.iot_proyectoindividual.entity.ReunionEmpty
import com.example.iot_proyectoindividual.entity.ReunionNotificacion
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ReunionesAdapter(var context:Context, var lista:ArrayList<ReunionNotificacion>) : RecyclerView.Adapter<ReunionesAdapter.ViewHolder>(){

    val listaNotificacion : ArrayList<ReunionNotificacion> = lista

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val  v = LayoutInflater.from(context).inflate(R.layout.recycle_reuniones,viewGroup,false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val notification = listaNotificacion[i]
        viewHolder.teNombre.text = notification.amigo.nombre
        Glide.with(context).load(Firebase.storage.reference.child("perfil/${notification.amigo.imagen}")).into(viewHolder.profImage)

        viewHolder.verIcon.setOnClickListener{
            val intent = Intent(context,VerReunionActivity::class.java)

            Firebase.database.reference.child("reuniones/${notification.idReunion}").get().addOnSuccessListener {
                val reunion = it.getValue<ReunionEmpty>()!!
                intent.putExtra("idA",notification.amigo.uid)
                intent.putExtra("idR",notification.idReunion)
                intent.putExtra("hora",reunion.hora)
                intent.putExtra("desc",reunion.descripcion)
                intent.putExtra("lat",reunion.latitud)
                intent.putExtra("lon",reunion.longitud)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return listaNotificacion.size
    }


    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var profImage : ImageView
        var teNombre : TextView
        var teTipo : TextView
        var verIcon : ImageView

        init{
            profImage = itemView.findViewById(R.id.imgPerfil)
            teNombre = itemView.findViewById(R.id.teNombre)
            teTipo = itemView.findViewById(R.id.teTipo)
            verIcon = itemView.findViewById(R.id.verReunion)
        }
    }
}