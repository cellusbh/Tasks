package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.PersonModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PersonService {

    // LOGIN -> USANDO RETROFIT, INFORMANDO EMAIL E SENHA RETORNAM UM PERSON MODEL
    @POST("Authentication/Login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ):
            Call<PersonModel>

    // CREATE -> USANDO RETROFIT, INFORMANDO NOME, EMAIL E SENHA RETORNAM UM PERSON MODEL
    @POST("Authentication/Create")
    @FormUrlEncoded
    fun create(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ):
            Call<PersonModel>

}