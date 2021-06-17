package com.example.appmovil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_crud_datos.*
import kotlinx.android.synthetic.main.activity_main.buttonIniciarSesion
import kotlinx.android.synthetic.main.activity_main.buttonRegistrarme

class CrudDatos : AppCompatActivity() {

    private val baseDeDatos = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_datos)


        comenzar();
    }

    private fun limpiarDatos(){
        editTextNombreServicio.setText("")
        editTextNombreServicio.setHint("Email")

        editTextNombreEncargadoPagar.setText("")
        editTextNombreEncargadoPagar.setHint("Nombre")

        editTextCantidadAPagar.setText("")
        editTextCantidadAPagar.setHint("Edad")

        ultimaFechaPago.setText("")
        ultimaFechaPago.setHint("Numero Celular")

        recordatorio.setText("")
        recordatorio.setHint("Profesión")
    }

    private fun comenzar(){
        title = "FORMULARIO"

        buttonRegistrarme.setOnClickListener {
            limpiarDatos()
        }

        buttonGuardar.setOnClickListener{
            var email = editTextNombreServicio.text.toString().trim()
            baseDeDatos.collection("datos").document(email).set(
                    hashMapOf(
                            "email" to editTextNombreServicio.text.toString(),
                            "nombre" to editTextNombreEncargadoPagar.text.toString(),
                            "edad" to editTextCantidadAPagar.text.toString(),
                            "telefonoCel" to ultimaFechaPago.text.toString(),
                            "profesion" to recordatorio.text.toString()
                    )

            )
            Toast.makeText(applicationContext, "Se Almaceno correctamente", Toast.LENGTH_SHORT)
        }

        buttonEliminar.setOnClickListener{
            var email = editTextNombreServicio.text.toString().trim()
            baseDeDatos.collection("datos").document(email).delete()
            Toast.makeText(applicationContext, "Se Elimino correctamente", Toast.LENGTH_SHORT)
            limpiarDatos()
        }

        buttonIniciarSesion.setOnClickListener {
            var email = editTextNombreServicio.text.toString().trim()
            baseDeDatos.collection("datos").document(email).get().addOnSuccessListener {

                editTextNombreEncargadoPagar.setText(it.get("nombre") as String?)
                editTextCantidadAPagar.setText(it.get("edad") as String?)
                ultimaFechaPago.setText(it.get("telefonoCel") as String?)
                recordatorio.setText(it.get("profesion") as String?)

            }


        }
    }
}