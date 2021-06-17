package com.example.appmovil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.content.Intent

class Repo {

    var usuario = "julanfzh@gmail.com"

    fun getUserData():LiveData<MutableList<Servicio>>{
        val mutableData = MutableLiveData<MutableList<Servicio>>()

        FirebaseFirestore.getInstance().collection(usuario).get().addOnSuccessListener { result ->
            val listData = mutableListOf<Servicio>()
            for(document in result){
                val imageUrl = document.getString("imagen")
                val titulo = document.getString("servicio")
                val valor = document.getString("cantidad")
                var servicio = Servicio(imageUrl!!, titulo!!, valor!!)
                listData.add(servicio)
            }

            mutableData.value = listData
        }

        return mutableData
    }
}