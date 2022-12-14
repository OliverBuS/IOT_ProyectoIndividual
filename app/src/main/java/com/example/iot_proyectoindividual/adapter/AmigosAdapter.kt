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
import com.example.iot_proyectoindividual.cliente.ReunionesFragment
import com.example.iot_proyectoindividual.entity.Amigo
import com.example.iot_proyectoindividual.save.Coordenadas
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = Calendar.getInstance().time
            val amigoDate = amigo.time?.let { df.parse(it) }
            val diffDays = currentDate.time - (amigoDate?.time ?: 0L)

            val horaActual = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

            if (diffDays / (1000 * 60) > 5 ||
                (horaActual < 7 || horaActual > 22) ||
                    !Coordenadas.inPucp)  {
                    amigo.disponible = -1
                }

                if (amigo.disponible == 0) {
                    viewHolder.teTipo.text = "Disponible"
                    viewHolder.teTipo.setTextColor(Color.parseColor("#5DB16F"))
                    viewHolder.checkIcon.visibility = View.GONE
                    viewHolder.exIcon.visibility = View.GONE
                    viewHolder.sendIcon.visibility = View.VISIBLE
                    viewHolder.sendIcon.setOnClickListener {

                        if (User.reunionId.isEmpty()) {
                            val intent = Intent(context, NuevaReunionActivity::class.java)
                            intent.putExtra("uid", amigo.uid)
                            intent.putExtra("disponible", amigo.disponible)
                            context.startActivity(intent)
                        } else {
                            val reference = Firebase.database.reference
                            val reuId = User.reunionId
                            val uid = amigo.uid


                            reference.child("invitados/$reuId/$uid").get().addOnSuccessListener {
                                if (!it.exists()) {
                                    reference.child("invitados/$reuId/$uid").setValue("pendiente")
                                        .addOnSuccessListener {
                                            reference.child("invitaciones/$uid/${User.uid}")
                                                .setValue(reuId).addOnSuccessListener {
                                                reference.child("invitados/$reuId/${User.uid}")
                                                    .setValue("asiste").addOnSuccessListener {
                                                    reference.child("activos/${User.uid}")
                                                        .setValue(reuId).addOnSuccessListener {
                                                        ReunionesFragment.res.should = true
                                                        Toast.makeText(
                                                            context,
                                                            "Amigo invitado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                        }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Tu amigo ya tiene una invitaci??n",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                    }

                } else if (amigo.disponible == -1) {
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

                        if (User.reunionId.isEmpty()) {
                            val intent = Intent(context, NuevaReunionActivity::class.java)
                            intent.putExtra("uid", amigo.uid)
                            intent.putExtra("disponible", amigo.disponible)
                            context.startActivity(intent)
                        } else {
                            val reference = Firebase.database.reference
                            val reuId = User.reunionId
                            val uid = amigo.uid


                            reference.child("invitados/$reuId/$uid").get().addOnSuccessListener {
                                if (!it.exists()) {
                                    reference.child("invitados/$reuId/$uid").setValue("pendiente")
                                        .addOnSuccessListener {
                                            reference.child("invitaciones/$uid/${User.uid}")
                                                .setValue(reuId).addOnSuccessListener {
                                                reference.child("invitados/$reuId/${User.uid}")
                                                    .setValue("asiste").addOnSuccessListener {
                                                    reference.child("activos/${User.uid}")
                                                        .setValue(reuId).addOnSuccessListener {
                                                        ReunionesFragment.res.should = true
                                                        Toast.makeText(
                                                            context,
                                                            "Amigo invitado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                        }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Tu amigo ya tiene una invitaci??n",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }


                        /*
                        val intent = Intent(context, NuevaReunionActivity::class.java)
                        intent.putExtra("uid", amigo.uid)
                        intent.putExtra("disponible", amigo.disponible)
                        context.startActivity(intent)
                         */
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