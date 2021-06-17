package com.example.appmovil

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.appmovil.databinding.ActivityMainBinding
import com.example.appmovil.fragments.CamaraFragment
import com.example.appmovil.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_camara.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.button_navegation


enum class typoProveedor(){
    BASIC,
    GOOGLE
}


class Home : AppCompatActivity() {

    private var cancellationSignal: CancellationSignal? = null
    private var usuario= ""
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get()=
            @RequiresApi(Build.VERSION_CODES.P)
            object: BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notificarUsuario("Error al autenticar: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notificarUsuario("Autenticación Satisfactoria")
                    var intentPrivado= Intent(this@Home, invicibleHuellaHomePrivados::class.java).apply {
                        putExtra("email", usuario)

                    }
                    startActivity(intentPrivado)
                }
            }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var bundle = intent.extras
        var email = bundle?.getString("email")
        var proveedor = bundle?.getString("proveedor")
        usuario = email.toString()
        val homeFragment = HomeFragment()
        val camaraFragment = CamaraFragment()



       // pasarDeFragments(homeFragment);
        pantallaCrearServicio.setOnClickListener {
            pantallaCRUD(email ?: "")
        }
        button_navegation.setOnNavigationItemSelectedListener {
            when ( it.itemId){
                R.id.inicio -> pantallaHOME(email ?: "")
                R.id.camara -> pantallaCamara(email ?: "")
                R.id.calendario -> pantallaCalendario(email ?: "")
            }
            true
        }

        pantallaNavegarServicios.setOnClickListener {
            pantallaMostrarServicios(email ?: "");
        }

        comenzar(email ?: "", proveedor ?: "")
        //Guardar datos...percistencia
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        //val prefs :SharedPreferences.Editor! = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        prefs.putString("email", email)
        prefs.putString("proveedor", proveedor)
        prefs.apply()

        val btn_autenticacion=findViewById<Button>(R.id.serviciosPrivados)
        revisarSoporteHuella()

        btn_autenticacion.setOnClickListener {
            val avisoBiometrico_prompt = BiometricPrompt.Builder(this)
                    .setTitle("Ingreso Con Huella Dactilar ")
                    //.setSubtitle("Autenticacion Sugerida")
                    .setDescription("Coloca tu huella en el lector para continuar")
                    .setNegativeButton("Cancelar", this.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                        notificarUsuario("Autenticación cancelada")
                    }).build()

            avisoBiometrico_prompt.authenticate(obtenerSeñalCancelacion(),mainExecutor, authenticationCallback )
        }




    }


    /*private fun pasarDeFragments(fragment: Fragment){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.contenedor_vistas, fragment)
                commit()
            }
    }*/
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

    private fun notificarUsuario(mensaje: String){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun obtenerSeñalCancelacion(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notificarUsuario("Autenticacion cancelada por el usuario")

        }

        return cancellationSignal as CancellationSignal
    }


    private fun revisarSoporteHuella(): Boolean {
        val keyguardManager : KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure){
            notificarUsuario("No tiene habilitado el uso de huella")
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            notificarUsuario("Permisos de Autenticación con huella esta deshabilitada")
            return false
        }
        return if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }else true
    }

    private fun pantallaCRUD(email: String){
        var intentCRUDServicios = Intent(this, CrudServicios::class.java).apply {
            putExtra("email", email)

        }

        startActivity(intentCRUDServicios)
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

    private fun pantallaCalendario(email: String){
        var intentCalendario = Intent(this, Calendario::class.java).apply {
            putExtra("email", email)

        }

        startActivity(intentCalendario)
    }

    private fun pantallaMostrarServicios(email: String){
        var intentMostrarServicios = Intent(this, MostrarServicios::class.java).apply {
            putExtra("email", email)

        }

        startActivity(intentMostrarServicios)
    }
}