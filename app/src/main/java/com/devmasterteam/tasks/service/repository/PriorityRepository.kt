package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class PriorityRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDAO()

    // COMPANION OBJECT PARA SALVAR A DESCRIÇÃO DA PRIORIDADE EM CACHE
    companion object {
        private val cache = mutableMapOf<Int, String>()
        fun getDescription(id: Int): String {
            return cache[id] ?: ""
        }

        fun setDescription(id: Int, str: String) {
            cache[id] = str
        }
    }

    // PEGA A DESCRIÇÃO DA PRIORIDADE ATRAVES DO ID
    fun getDescription(id: Int): String {
        val cached = PriorityRepository.getDescription(id)
        return if (cached == "") {
            val description = database.getDescription(id)
            PriorityRepository.setDescription(id, description)
            description
        } else {
            cached
        }
    }

    // LISTA SENDO SALVA USANDO O RETROFIT
    fun list(listener: APIListener<List<PriorityModel>>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.list(), listener)
    }

    // LISTA SENDO SALVA USANDO O ROOM
    fun list(): List<PriorityModel> {
        return database.list()
    }

    // LISTA SENDO SALVA USANDO O ROOM
    fun save(list: List<PriorityModel>) {
        database.clear()
        database.save(list)
    }
}
