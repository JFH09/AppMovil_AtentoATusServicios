package com.example.appmovil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_mostrar_servicios.*

class MostrarServicios : AppCompatActivity() {


    public var usuario="";

    private lateinit var adapter: MainAdapter
    private val viewModel by lazy{ ViewModelProviders.of(this).get(MainViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_servicios)


        var bundle  = intent.extras;
        var email = bundle?.getString("email")
        usuario = email.toString();

        adapter = MainAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        observeData()

    }

    fun observeData(){
        shimmer_view_container.startShimmer()
        viewModel.fetchUserData().observe(this, Observer{
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility = View.GONE
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}