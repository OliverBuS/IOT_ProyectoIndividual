package com.example.iot_proyectoindividual.cliente

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.iot_proyectoindividual.R
import com.example.iot_proyectoindividual.config.ImageProcess
import com.example.iot_proyectoindividual.entity.Usuario
import com.example.iot_proyectoindividual.save.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


class PerfilFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    private lateinit var imPerfil: ImageView
    private lateinit var storageReference: StorageReference
    private lateinit var varContext: Context
    private val imgLink = "perfil/${User.uid}/img.jpg"
    lateinit var currentPhotoPath: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storageReference = Firebase.storage.reference.child(imgLink)
        varContext = view.context


        imPerfil = view.findViewById(R.id.imagenPerfil)
        val edNombre = view.findViewById<EditText>(R.id.edNombre)
        val edEstado = view.findViewById<EditText>(R.id.edEstado)
        val guardarNombre = view.findViewById<ImageView>(R.id.guardarNombre)
        val guardarEstado = view.findViewById<ImageView>(R.id.guardarEstado)
        val editarImagen = view.findViewById<ImageView>(R.id.editarImagen)


        val selectFoto =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    val imageUri = intent?.data
                    imPerfil.setImageURI(imageUri)

                    var bitmapImage = ImageProcess.handleSamplingAndRotationBitmap(context,imageUri)
                    val witdh = bitmapImage.width
                    val heigh = bitmapImage.height

                    if(witdh>heigh) {
                        bitmapImage = Bitmap.createBitmap(bitmapImage,(witdh/2)-(heigh/2) ,0, heigh, heigh )
                    }else{
                        bitmapImage = Bitmap.createBitmap(bitmapImage,0 ,(heigh/2)-(witdh/2), witdh, witdh)
                    }

                    val baos = ByteArrayOutputStream()
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val dataimage = baos.toByteArray()


                    if (imageUri != null) {
                        storageReference.putBytes(dataimage).addOnSuccessListener {
                            if(User.usuario.imagen?.contains(User.uid) == false) {
                                Firebase.database.reference.child("usuarios/${User.uid}/imagen")
                                    .setValue("${User.uid}/img.jpg")
                                User.usuario.imagen = "${User.uid}/img.jpg"
                            }
                            Toast.makeText(context, "Se actualizó tu foto", Toast.LENGTH_SHORT).show()
                        }.addOnCanceledListener {
                            Toast.makeText(context, "Hubo un problema subiendo tu foto", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }




        Glide.with(view.context).load(Firebase.storage.reference.child("perfil/${User.usuario.imagen!!}"))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true).into(imPerfil)
        edNombre.setText(User.usuario.nombre ?: "...")
        edEstado.setText(User.usuario.estado ?: "...")

        guardarNombre.setOnClickListener {
            Firebase.database.reference.child("usuarios/${User.uid}/nombre")
                .setValue(edNombre.text.toString()).addOnFailureListener {
                    Toast.makeText(
                        view.context,
                        "Hubo un problema actualizando",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
        }
        guardarEstado.setOnClickListener {
            Firebase.database.reference.child("usuarios/${User.uid}/estado")
                .setValue(edEstado.text.toString()).addOnFailureListener {
                    Toast.makeText(
                        view.context,
                        "Hubo un problema actualizando",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        editarImagen.setOnClickListener {

            MaterialAlertDialogBuilder(view.context)
                .setTitle("Subir Foto")
                .setMessage("¿Cómo va a cambiar su foto?")
                .setNegativeButton("Galeria") { d, w ->
                    selectFoto.launch(
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        )
                    )
                }.setPositiveButton("Cámara") { d, w ->

                    val fileName = "img"
                    val storageDirectory =
                        context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    try {
                        val imageFile = File.createTempFile(fileName, ".jpg", storageDirectory)
                        currentPhotoPath = imageFile.absolutePath
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val imageUri = context?.let { it1 ->
                            FileProvider.getUriForFile(
                                it1,
                                "com.example.iot_proyectoindividual.fileprovider",
                                imageFile
                            )
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(intent, 1)
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(
                            view.context,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    //tomarFoto.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                }
                .setNeutralButton("Cancelar") { d, w ->

                }
                .show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //var bitImage = BitmapFactory.decodeFile(currentPhotoPath)
            val uriImage = Uri.fromFile(File(currentPhotoPath))
            var bitmapImage = ImageProcess.handleSamplingAndRotationBitmap(context,uriImage)

            val witdh = bitmapImage.width
            val heigh = bitmapImage.height

            if(witdh>heigh) {
                bitmapImage = Bitmap.createBitmap(bitmapImage,(witdh/2)-(heigh/2) ,0, heigh, heigh )
            }else{
                bitmapImage = Bitmap.createBitmap(bitmapImage,0 ,(heigh/2)-(witdh/2), witdh, witdh)
            }

            MediaStore.Images.Media.insertImage(varContext.contentResolver,bitmapImage  , "Profile_PUCPMeet" , "Imagen de perfil");
            imPerfil.setImageBitmap(bitmapImage)


            val baos = ByteArrayOutputStream()
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataimage = baos.toByteArray()

            storageReference.putBytes(dataimage).addOnSuccessListener {
                if(User.usuario.imagen?.contains(User.uid) == false) {
                    Firebase.database.reference.child("usuarios/${User.uid}/imagen")
                        .setValue("${User.uid}/img.jpg")
                    User.usuario.imagen = "${User.uid}/img.jpg"
                }
                Toast.makeText(context, "Se actualizó tu foto", Toast.LENGTH_SHORT).show()

            }.addOnCanceledListener {
                Toast.makeText(context, "Hubo un problema subiendo tu foto", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }





}