package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {

    // TODAS AS TAREFAS
    @GET("Task")
    fun list(): Call<List<TaskModel>>

    // PROXIMO 7 DIAS
    @GET("Task/Next7Days")
    fun listNext(): Call<List<TaskModel>>

    // EXPIRADAS
    @GET("Task/Overdue")
    fun listOverdue(): Call<List<TaskModel>>

    // TAREFA ESPECIFICA
    @GET("Task/{id}")
    fun load(@Path(value = "id", encoded = true) id: Int): Call<TaskModel>

    // INSERIR UMA TAREFA INFORMANDO OS DADOS ABAIXO
    @POST("Task")
    @FormUrlEncoded // FORMA DE ENVIO DOS DADOS EM CORPO
    fun create(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Call<Boolean>

    // ATUALIZAR UMA TAREFA INFORMANDO OS DADOS ABAIXO
    @PUT("Task")
    @FormUrlEncoded
    fun uptade(
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Call<Boolean>

    // ATUALIZAR O COMPLETE
    @PUT("Task/Complete")
    @FormUrlEncoded
    fun complete(@Field("Id") id: Int): Call<Boolean>

    // DESFAZER O COMPLETE
    @PUT("Task/Undo")
    @FormUrlEncoded
    fun undo(@Field("Id") id: Int): Call<Boolean>

    // REMOÇÃO DE UMA TAREFA
    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun delete(@Field("Id") id: Int): Call<Boolean>

}