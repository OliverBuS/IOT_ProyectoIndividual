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

class AmigosAdapter(var context: Context, var lista: ArrayList<Amigo>) :
    RecyclerView.Adapter<AmigosAdapter.ViewHolder>() {

    val listaAmigos: ArrayList<Amigo> = lista

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.recycle_amigos_lista, viewGroup, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val amigo = listaAmigos[i]
        viewHolder.teNombre.text = amigo.nombre
        viewHolder.teEstado.text = amigo.estado
        Glide.with(context).load(Firebase.storage.reference.child("perfil/${amigo.imagen}"))
            .into(viewHolder.profImage)

        if (amigo.tipo == "solicitud") {
            viewHolder.teTipo.text = "Solicitud de amistad"

            viewHolder.checkIcon.setOnClickListener {
                Firebase.database.reference.child("relaciones/${User.uid}/${amigo.uid}")
                    .setValue("amigo").addOnFailureListener {
                    Toast.makeText(context, "Hubo un problema", Toast.LENGTH_SHORT).show()
                }
                Firebase.database.reference.child("relaciones/${amigo.uid}/${User.uid}")
                    .setValue("amigo").addOnFailureListener {
                    Toast.makeText(context, "Hubo un problema", Toast.LENGTH_SHORT).show()
                }
            }
            viewHolder.exIcon.setOnClickListener {
                Firebase.database.reference.child("relaciones/${User.uid}/${amigo.uid}")
                    .removeValue().addOnFailureListener {
                    Toast.makeText(context, "Hubo un problema", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            if (amigo.disponible == 0) {
                viewHolder.teTipo.text = "Disponible"
                viewHolder.teTipo.setTextColor(Color.parseColor("#5DB16F"))
                viewHolder.checkIcon.visibility = View.GONE
                viewHolder.exIcon.visibility = View.GONE
                viewHolder.sendIcon.visibility = View.VISIBLE
                viewHolder.sendIcon.setOnClickListener {
                    val intent = Intent(context, NuevaReunionActivity::class.java)
                    intent.putExtra("uid", amigo.uid)
                    intent.putExtra("disponible", amigo.disponible)
                    context.startActivity(intent)
                }

            } else if (amigo.disponible==-1) {
                viewHolder.teTipo.text = "No disponible"
                viewHolder.teTipo.setTextColor(Color.parseColor("#971C1C"))
                viewHolder.checkIcon.visibility = View.GONE
                viewHolder.exIcon.visibility = View.GONE
            } else {
                viewHolder.teTipo.text = "Disponible ${amigo.disponible}:00"
                viewHolder.teTipo.setTextColor(Color.parseColor("#49BDB6"))
                viewHolder.checkIcon.visibility = View.GONE
                viewHolder.exIcon.visibility = View.GONE
                viewHolder.alarmIcon.visibility = View.VISIBLE
                viewHolder.alarmIcon.setOnClickListener {
                    context.startActivity(Intent(context, NuevaReunionActivity::class.java))
                }
            }
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
        var checkIcon: ImageView
        var exIcon: ImageView
        var sendIcon: ImageView
        var alarmIcon: ImageView

        init {
            profImage = itemView.findViewById(R.id.imgPerfil)
            teNombre = itemView.findViewById(R.id.teNombre)
            teEstado = itemView.findViewById(R.id.teEstado)
            teTipo = itemView.findViewById(R.id.teTipo)
            checkIcon = itemView.findViewById(R.id.aceptarSolicitud)
            exIcon = itemView.findViewById(R.id.rechazarSolicitud)
            sendIcon = itemView.findViewById(R.id.invitar)
            alarmIcon = itemView.findViewById(R.id.invitarDespues)
        }
    }
}