package com.example.appmovil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appmovil.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_camara.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.button_navegation

class Camara : AppCompatActivity() {

   // lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)

        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        //binding

        var bundle = intent.extras
        var email = bundle?.getString("email")
       // var proveedor = bundle?.getString("proveedor")


        btnabrir.setOnClickListener {
          val act = Intent(this, Camara_Interfaz_Uso::class.java)
          startActivity(act)
        }

        button_navegation.setOnNavigationItemSelectedListener {
            when ( it.itemId){
                R.id.inicio -> pantallaHOME(email ?: "")
                R.id.camara -> pantallaCamara(email ?: "")
            }
            true
        }

    }

    private fun pantallaHOME(email: String){
        var intentHome = Intent(this, Home::class.java).apply {
            putExtra("email", email)

        }

        startActivity(intentHome)
    }

    private fun pantallaCamara(email: String){
        var intentCamara= Intent(this, Camara::class.java).apply {
            putExtra("email", email)

        }

        startActivity(intentCamara)
    }
}