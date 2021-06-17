package com.example.appmovil

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appmovil.fragments.HomeFragment
import kotlinx.android.synthetic.main.item_row.view.*

class MainAdapter(private val context: Context): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var dataList = mutableListOf<Servicio>()

    fun setListData(data:MutableList<Servicio>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        var servicio = dataList[position]
        holder.bindView(servicio)
    }

    override fun getItemCount(): Int {

        return if(dataList.size>0){
            dataList.size
        }else{
            0
        }
    }

    inner class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(servicio: Servicio){
            Glide.with(context).load(servicio.imagenUrl).into(itemView.circleImagenView)
            itemView.titulo_Servicio.text = servicio.titulo
            itemView.valorServicio.text = servicio.valor
        }
    }

}