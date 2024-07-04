package com.devmasterteam.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devmasterteam.tasks.service.model.PriorityModel

@Dao
interface PriorityDAO {

    // PEGA A LISTA DE PRIORIDADES E SALVA NO BANCO DE DADOS
    @Insert
    fun save(list: List<PriorityModel>)

    // LISTAGEM - CONSULTA AS PRIORIDADES NO BANCO DE DADOS
    @Query("SELECT * FROM Priority")
    fun list(): List<PriorityModel>

    // PEGA A DESCRIÇÃO DA PRIORIDADE
    @Query("SELECT description FROM Priority WHERE id = :id")
    fun getDescription(id: Int): String

    // LIMPA A TABELA DE PRIORIDADES
    @Query("DELETE FROM Priority")
    fun clear()

}