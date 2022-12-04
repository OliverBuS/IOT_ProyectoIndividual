package com.example.iot_proyectoindividual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.iot_proyectoindividual.cliente.ClienteHomeActivity
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class CheckUser : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen =installSplashScreen()
        super.onCreate(savedInstanceState)
        Thread.sleep(500)
        splashScreen.setKeepOnScreenCondition{
            false
        }
        setContentView(R.layout.activity_check_user)

        databaseReference = Firebase.database.reference
        mAuth = Firebase.auth
        if(mAuth.currentUser==null){
            goLogPage()
        } else{
            User.uid= mAuth.currentUser!!.uid
            databaseReference.child("usuarios/${mAuth.currentUser?.uid}").get()
                .addOnSuccessListener {
                    User.usuario = it.getValue<Usuario>()!!
                    goHome()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun goLogPage(){
        val intent = Intent(this,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent);
    }


    fun goHome(){
        if(User.usuario?.rol.equals("admin")){
            Toast.makeText(this,"Por hacer", Toast.LENGTH_SHORT).show()
        } else{
            val intent = Intent(this, ClienteHomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

    }

}