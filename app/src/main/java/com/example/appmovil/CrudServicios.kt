package com.example.appmovil

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_crud_datos.buttonRegistrarme
import kotlinx.android.synthetic.main.activity_crud_datos.editTextCantidadAPagar
import kotlinx.android.synthetic.main.activity_crud_datos.editTextNombreServicio
import kotlinx.android.synthetic.main.activity_crud_datos.editTextNombreEncargadoPagar
import kotlinx.android.synthetic.main.activity_crud_datos.ultimaFechaPago
import kotlinx.android.synthetic.main.activity_crud_datos.recordatorio
import kotlinx.android.synthetic.main.activity_crud_servicios.*
import java.util.*
import kotlin.collections.HashMap

class CrudServicios : AppCompatActivity() {


    private val File = 1
    private var usuario = "";
    private var fechaRecordatorio= ""
    private var fechaUltima = ""
    private var horaRecordatorio="";
    private val baseDeDatos = FirebaseFirestore.getInstance()

   // val myRef = baseDeDatos.getReference("user")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_servicios)
       ultimaFechaPago.setFocusable(false);
       ultimaFechaPago.setEnabled(false);
       ultimaFechaPago.setCursorVisible(false);
       recordatorio.setFocusable(false);
       recordatorio.setEnabled(false);
       recordatorio.setCursorVisible(false);
       var bundle  = intent.extras;
       var email = bundle?.getString("email")
       usuario = email.toString();


       buttonUltimaFecha.setOnClickListener {

           capturarUltimaFecha();


       }

        buttonRecordatorio.setOnClickListener {
            capturarFecha()

        }

       button_ImagenServicio.setOnClickListener {
           fileUpload(email ?: "")
       }

        comenzar();
    }

    fun capturarUltimaFecha(){
        var fecha1 = ""
        var hora1 = ""
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                this@CrudServicios,
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    fechaUltima= day.toString() + " / " + month.toString() + " / " + year.toString()
                    ultimaFechaPago.setText(fechaUltima)
                    ultimaFechaPago.setHint(fechaUltima)
                    Toast.makeText(applicationContext, "d=>"+year+month+day, Toast.LENGTH_SHORT).show()


                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()


        }
        fechaUltima = fecha1
//        ultimaFechaPago.setKeyListener(null);

    }

    fun capturarFecha(){
        var fecha1 = ""
        var hora1 = ""
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                this@CrudServicios,
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    fecha1 = day.toString() + " / " + month.toString() + " / " + year.toString()
                    fechaRecordatorio= day.toString() + " / " + month.toString() + " / " + year.toString()

                    Toast.makeText(applicationContext, "d=>"+fecha1, Toast.LENGTH_SHORT).show()

                    TimePickerDialog(
                        this@CrudServicios,
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            hora1 = hour.toString() + ":" + minute.toString()
                            horaRecordatorio= hour.toString() + " : " + minute.toString()
                            recordatorio.setText(fecha1 + " - "+hora1)
                            recordatorio.setHint(fecha1 + " - "+hora1)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()

                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()

        }
        fechaRecordatorio = fecha1
        horaRecordatorio = hora1

    }
    fun fileUpload(email: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, File,)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == File) {
            Toast.makeText(applicationContext, "Subiendo Informacion, Espera un momento", Toast.LENGTH_SHORT).show()
            if (resultCode == RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference =
                    FirebaseStorage.getInstance().getReference().child(usuario)
                val file_name: StorageReference = Folder.child("file" + FileUri!!.lastPathSegment)
                file_name.putFile(FileUri).addOnSuccessListener { taskSnapshot ->
                    file_name.getDownloadUrl().addOnSuccessListener { uri ->
                        var nombreServicio = editTextNombreServicio.text.toString().trim()
                        val hashMap =
                            HashMap<String, String>()
                        hashMap["link"] = java.lang.String.valueOf(uri)
                        baseDeDatos.collection(usuario).document(usuario+"servicio"+nombreServicio).set(hashMapOf(
                            "servicio" to editTextNombreServicio.text.toString(),
                            "encargadoPago" to editTextNombreEncargadoPagar.text.toString(),
                            "cantidad" to editTextCantidadAPagar.text.toString(),
                            "ultimaFechaPago" to fechaUltima,
                            "FechaRecordatorio" to fechaRecordatorio,
                             "HoraRecordatorio" to horaRecordatorio,
                            "imagen" to java.lang.String.valueOf(uri)
                        ))
                        limpiarDatos()
                        onBackPressed()
                        Toast.makeText(applicationContext, "Se Almaceno correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun limpiarDatos(){
        editTextNombreServicio.setText("")
        editTextNombreServicio.setHint("Nombre Servicio")

        editTextNombreEncargadoPagar.setText("")
        editTextNombreEncargadoPagar.setHint("Nombre Encargado Pagar")

        editTextCantidadAPagar.setText("")
        editTextCantidadAPagar.setHint("Cantidad a pagar")

        ultimaFechaPago.setText("")
        ultimaFechaPago.setHint("Ultima Fecha De Pago")

        recordatorio.setText("")
        recordatorio.setHint("Recordatorio")

    }

    private fun comenzar(){
        title = "FORMULARIO"

        buttonRegistrarme.setOnClickListener {
            limpiarDatos()
        }

       /* buttonGuardar.setOnClickListener{
            var email = editTextEmail.text.toString().trim()
            baseDeDatos.collection(usuario).document(usuario+"servicio"+email).set(
                hashMapOf(
                    "email" to editTextEmail.text.toString(),
                    "nombre" to nombreTextView.text.toString(),
                    "edad" to edadTextView.text.toString(),
                    "telefonoCel" to numeroCelular.text.toString(),
                    "profesion" to profesionTextView.text.toString()
                )

            )
            Toast.makeText(applicationContext, "Se Almaceno correctamente", Toast.LENGTH_SHORT)
        }

        buttonEliminar.setOnClickListener{
            var email = editTextEmail.text.toString().trim()
            baseDeDatos.collection(usuario).document(usuario+"servicio"+email).delete()
            Toast.makeText(applicationContext, "Se Elimino correctamente", Toast.LENGTH_SHORT)
            limpiarDatos()
        }

        buttonIniciarSesion.setOnClickListener {
            var email = editTextEmail.text.toString().trim()
            baseDeDatos.collection(usuario).document(usuario+"servicio"+email).get().addOnSuccessListener {

                nombreTextView.setText(it.get("nombre") as String?)
                edadTextView.setText(it.get("edad") as String?)
                numeroCelular.setText(it.get("telefonoCel") as String?)
                profesionTextView.setText(it.get("profesion") as String?)

            }


        }*/
    }
}