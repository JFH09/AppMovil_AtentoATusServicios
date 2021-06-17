package com.example.appmovil

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Provider

class MainActivity : AppCompatActivity() {


    private  val GOOGLE_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var analytics : FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase Completa")
        analytics.logEvent("InitScreen", bundle)
        comenzar()
        sesionActiva()

    }
    private fun sesionActiva(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val proveedor = prefs.getString("proveedor", null)

        if(email !=null && proveedor!=null){

            mostrarHome(email, typoProveedor.valueOf(proveedor ))
        }
    }

    private fun comenzar(){
        title = "AATS"

        buttonRegistrarme.setOnClickListener {
            if (textoEmail.text.isNotEmpty() && textoContra.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(textoEmail.text.toString(), textoContra.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            mostrarHome(it.result?.user?.email ?: "", typoProveedor.BASIC)
                        }else{
                            mostrarAlerta()
                        }
                    }
            }
        }

        buttonIniciarSesion.setOnClickListener {
            if (textoEmail.text.isNotEmpty() && textoContra.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(textoEmail.text.toString(), textoContra.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            mostrarHome(it.result?.user?.email ?: "", typoProveedor.BASIC)
                        }else{
                            mostrarAlerta()
                        }
                    }
            }
        }

        buttonGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleUsu = GoogleSignIn.getClient(this, googleConf)
            googleUsu.signOut()
            startActivityForResult(googleUsu.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    private fun mostrarAlerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error ")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog= builder.create()
        dialog.show()
    }

    private fun mostrarHome(email: String, proveedor: typoProveedor){
        val homeIntent = Intent(this, Home::class.java ).apply {
            putExtra("email", email)
            putExtra("proveedor", proveedor.name)
        }

        startActivity(homeIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val cuenta = task.getResult(ApiException::class.java)

                if (cuenta != null){
                    val credencial = GoogleAuthProvider.getCredential(cuenta.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener {
                        if(it.isSuccessful){
                            mostrarHome(cuenta.email ?: "", typoProveedor.GOOGLE)
                        }else{
                            mostrarAlerta()
                        }
                    }
                }
            }catch (e: ApiException){
                mostrarAlerta()
            }

        }

    }

}