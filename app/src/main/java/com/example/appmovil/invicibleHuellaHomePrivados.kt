package com.example.appmovil

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appmovil.fragments.CamaraFragment
import com.example.appmovil.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.buttonCerrarSesion
import kotlinx.android.synthetic.main.activity_home.button_navegation
import kotlinx.android.synthetic.main.activity_home.textoObtenerEmail
import kotlinx.android.synthetic.main.activity_invicible_huella_home_privados.*

class invicibleHuellaHomePrivados : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invicible_huella_home_privados)

        var bundle = intent.extras
        var email = bundle?.getString("email")
        var proveedor = bundle?.getString("proveedor")

        val homeFragment = HomeFragment()
        val camaraFragment = CamaraFragment()

        // pasarDeFragments(homeFragment);
        pantallaCrearServicioPrivado.setOnClickListener {
            pantallaCRUDServicioPrivado(email ?: "")
        }
        button_navegation.setOnNavigationItemSelectedListener {
            when ( it.itemId){
                R.id.inicio -> pantallaHOME(email ?: "")
                R.id.camara -> pantallaCamara(email ?: "")
            }
            true
        }



        comenzar(email ?: "", proveedor ?: "")
        //Guardar datos...percistencia
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        //val prefs :SharedPreferences.Editor! = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        prefs.putString("email", email)
        prefs.putString("proveedor", proveedor)
        prefs.apply()
    }

    private fun comenzar(email: String, proveedor: String){
        title = "Inicio"
        textoObtenerEmail.text = email
        //textoObtenerProveedor.text = proveedor

        buttonCerrarSesion.setOnClickListener {

            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()
            onBackPressed()//Se devuelve a la pantalla anterior....
        }
    }
    private fun pantallaCRUDServicioPrivado(email: String){
        var intentCRUDServiciosPrivado = Intent(this, CrudServicioPrivado::class.java).apply {
            putExtra("email", email)

        }

        startActivity(intentCRUDServiciosPrivado)
    }

    private fun pantallaCamara(email: String){
        var intentCamara= Intent(this, Camara::class.java).apply {
            putExtra("email", email)

        }

        startActivity(intentCamara)
    }
    private fun pantallaHOME(email: String){
        var intentHome = Intent(this, Home::class.java).apply {
            putExtra("email", email)

        }

        startActivity(intentHome)
    }
}