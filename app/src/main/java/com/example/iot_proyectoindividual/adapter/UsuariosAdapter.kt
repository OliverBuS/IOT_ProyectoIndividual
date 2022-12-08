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
import com.example.iot_proyectoindividual.admin.BanUserActivity
import com.example.iot_proyectoindividual.admin.UnbanUserActivity
import com.example.iot_proyectoindividual.cliente.NuevaReunionActivity
import com.example.iot_proyectoindividual.cliente.ReunionesFragment
import com.example.iot_proyectoindividual.entity.Amigo
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UsuariosAdapter(var context: Context, var lista: ArrayList<Usuario>) :
    RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {

    val listaUsuarios: ArrayList<Usuario> = lista

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.recycle_usuarios_lista, viewGroup, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val usuario = listaUsuarios[i]
        viewHolder.teNombre.text = usuario.nombre
        viewHolder.teEstado.text = usuario.estado
        Glide.with(context).load(Firebase.storage.reference.child("perfil/${usuario.imagen}"))
            .into(viewHolder.profImage)

        var estaVacio: Boolean
        estaVacio = usuario.banfin?.isEmpty() ?: true


        if (!estaVacio) {
            val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val timeBanFin = dateFormat.parse(usuario.banfin)
            val timeNow = Calendar.getInstance().time

            if (timeBanFin < timeNow) {
                viewHolder.banUser.setOnClickListener {
                    val intent = Intent(context, BanUserActivity::class.java)
                    intent.putExtra("usuario", usuario)
                    context.startActivity(intent)
                }
                viewHolder.unbanUser.visibility = View.GONE
            } else {
                viewHolder.unbanUser.setOnClickListener {
                    val intent = Intent(context, UnbanUserActivity::class.java)
                    intent.putExtra("usuario", usuario)
                    context.startActivity(intent)
                }
                viewHolder.banUser.visibility = View.GONE
            }
        } else{
            viewHolder.banUser.setOnClickListener {
                val intent = Intent(context, BanUserActivity::class.java)
                intent.putExtra("usuario", usuario)
                context.startActivity(intent)
            }
            viewHolder.unbanUser.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profImage: ImageView
        var teNombre: TextView
        var teEstado: TextView
        var banUser: ImageView
        var unbanUser: ImageView

        init {
            profImage = itemView.findViewById(R.id.imgPerfil)
            teNombre = itemView.findViewById(R.id.teNombre)
            teEstado = itemView.findViewById(R.id.teEstado)
            banUser = itemView.findViewById(R.id.banearUsuario)
            unbanUser = itemView.findViewById(R.id.desbanearUsuario)
        }
    }
}