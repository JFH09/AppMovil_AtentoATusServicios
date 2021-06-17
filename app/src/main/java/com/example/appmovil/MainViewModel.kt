package com.example.appmovil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val repo = Repo()
    fun fetchUserData():LiveData<MutableList<Servicio>>{
        val mutableData = MutableLiveData<MutableList<Servicio>>()
        repo.getUserData().observeForever { serviceList ->
            mutableData.value = serviceList
        }

        return mutableData
    }
}