package com.example.iot_proyectoindividual.background

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.entity.Amigo
import com.example.iot_proyectoindividual.save.Coordenadas
import com.example.iot_proyectoindividual.save.User
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


class LocationService:Service() {

    private val serviceScope = CoroutineScope(SupervisorJob()+ Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient =  DefaultLocalClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START -> start()
            ACTION_STOP ->stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this,"location")
            .setContentTitle("Obteniendo localizacion")
            .setContentText("location : null")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setOngoing(true)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10_000L) //600_000L
            .catch { e->
                e.printStackTrace()
                Coordenadas.inPucp=false
                val updatedNotification = notification.setContentText(
                    "No se puede verificar la localización"
                )
                notificationManager.notify(1,updatedNotification.build())
            }
            .onEach { location ->
                val lat = location.latitude
                val lon = location.longitude
                var message = "No"

                val amigo = Amigo()
                amigo.disponible = -1
                amigo.estado = User.usuario.estado
                amigo.imagen = User.usuario.imagen
                amigo.nombre = User.usuario.nombre

                if(-12.064598 > lat && lat > -12.074049
                    &&
                    -77.076378>lon  && lon> -77.083585)
                {
                    Coordenadas.inPucp=true
                    message = "Si"
                    amigo.disponible = User.usuario.horario?.disponible()
                } else{
                    Coordenadas.inPucp=false
                }

                val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val date: String = df.format(Calendar.getInstance().time)
                amigo.time = date
                Firebase.database.reference.child("amigos/${User.uid}").setValue(amigo)

                val updatedNotification = notification.setContentText(
                    "En la pucp : $message"
                )

                Firebase.database.reference.child("invitaciones/${User.uid}").get().addOnSuccessListener {
                    if(it.exists()){
                        val updatedNotification2 = notification.setContentText("Tienes invitaciones para reunirte").setContentTitle("Tienes una invitacion")
                        notificationManager.notify(1,updatedNotification2.build())
                    } else{
                        notificationManager.notify(1,updatedNotification.build())
                    }
                }


            }
            .launchIn(serviceScope)

        startForeground(1,notification.build())
    }

    private fun stop(){
        super.onDestroy()
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


    companion object{
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"


    }
}