package com.example.appmovil.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmovil.MainAdapter
import com.example.appmovil.R
import com.example.appmovil.R.layout.fragment_home
import com.example.appmovil.Servicio
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  lateinit var adapter:MainAdapter

  // private  val  context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //adapter = MainAdapter(R.id.inicio)
        //recycleView.layoutManager= LinearLayoutManager(R.id.inicio)
        //recycleView.adapter = adapter

        val dummyList = mutableListOf<Servicio>()

        dummyList.add(Servicio("https://lh3.googleusercontent.com/proxy/NY5zVRkM0T9RjvMBMg6aMOfU0uMeXYkE7B5COjwA7vi6rPMeTDkyuISm2M3E-pak62j6mzrENdpegRHJ5yd3wpDlOYKcsmVK_ia8rWhX7-s342R031CCiS1qwhLAWcvUXUJK",
            "Sin imagen1", "34000"))
        adapter.setListData(dummyList)

        adapter.notifyDataSetChanged()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *

         * @return A new instance of fragment HomeFragment.
         */

    }
}