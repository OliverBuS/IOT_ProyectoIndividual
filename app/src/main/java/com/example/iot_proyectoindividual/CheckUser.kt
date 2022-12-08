package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.iot_proyectoindividual.admin.MainAdminActivity
import com.example.iot_proyectoindividual.background.LocationService
import com.example.iot_proyectoindividual.cliente.ClienteHomeActivity
import com.example.iot_proyectoindividual.entity.Ban
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CheckUser : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        Thread.sleep(500)
        splashScreen.setKeepOnScreenCondition {
            false
        }
        setContentView(R.layout.activity_check_user)

        databaseReference = Firebase.database.reference
        mAuth = Firebase.auth
        if (mAuth.currentUser == null) {
            goLogPage()
        } else {
            User.uid = mAuth.currentUser!!.uid
            databaseReference.child("usuarios/${mAuth.currentUser?.uid}").get()
                .addOnSuccessListener {
                    User.usuario = it.getValue<Usuario>()!!
                    goHome()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun goLogPage() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent);
    }


    fun goHome() {
        if (User.usuario?.rol.equals("admin")) {
            val intent = Intent(this, MainAdminActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            //Toast.makeText(this, "Por hacer", Toast.LENGTH_SHORT).show()
        } else {

            if (User.usuario.banfin != null) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val timeBan = dateFormat.parse(User.usuario.banfin)
                val timeNow = Calendar.getInstance().time
                val ref = Firebase.database.reference
                if (timeBan < timeNow) {
                    ref.child("usuarios/${User.uid}/ban").removeValue()
                    ref.child("usuarios/${User.uid}/banfin").removeValue()
                    ref.child("baneos/${User.usuario.ban}").removeValue()
                    User.usuario.ban = null
                    User.usuario.banfin = null
                    Toast.makeText(this, "Ha terminado su baneo", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ClienteHomeActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    ref.child("baneos/${User.usuario.ban}").get().addOnSuccessListener {
                        val ban = it.getValue<Ban>()
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        val dateForma2 = SimpleDateFormat("dd/MM/yyy-HH:mm:ss")
                        val fechaFin = dateForma2.format(dateFormat.parse(ban?.fin ?: ""))
                        if (ban != null) {
                            mAuth.signOut()
                            Intent(this, LocationService::class.java).apply {
                                action = LocationService.ACTION_STOP
                                startService(this)
                            }
                            MaterialAlertDialogBuilder(this).setTitle("Tu cuenta está baneada")
                                .setMessage("Razón: ${ban.razon}\nFin: ${fechaFin}\nSi cree que es un error contactese con ${ban.contacto}")
                                .setPositiveButton("aceptar") { _, _ ->
                                    intent = Intent(this, MainActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                }.show()


                        } else {
                            ref.child("usuarios/${User.uid}/ban").removeValue()
                            ref.child("usuarios/${User.uid}/banfin").removeValue()
                            User.usuario.ban = null
                            User.usuario.banfin = null
                            Toast.makeText(this, "Ha terminado su baneo", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ClienteHomeActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    }
                }
            } else {
                val intent = Intent(this, ClienteHomeActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }

}