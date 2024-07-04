package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.PriorityModel
import retrofit2.Call
import retrofit2.http.GET

interface PriorityService {

    // LIST -> USANDO RETROFIT, RETORNA UMA LISTA DE PRIORITY MODEL
    @GET("Priority")
    fun list(): Call<List<PriorityModel>>


}

