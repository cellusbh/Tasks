package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var taskFilter = 0

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status


    // LISTA TODAS AS TAREFAS
    fun list(filter: Int) {
        taskFilter = filter

        // CRIA O LISTENER
        val listener = object : APIListener<List<TaskModel>> {

            // TRATA O RETORNO DA LISTA DE TAREFAS
            override fun onSuccess(result: List<TaskModel>) {
                result.forEach {
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId)
                }
                _tasks.value = result
            }

            override fun onFailure(message: String) {}
        }

        // VERIFICA O FILTRO DE TAREFAS DE ACORDO COM O FILTRO (0, 1, 2)
        when (filter) {
            TaskConstants.FILTER.ALL -> taskRepository.list(listener)
            TaskConstants.FILTER.NEXT -> taskRepository.listNext(listener)
            else -> taskRepository.listOverdue(listener)
        }
    }

    // DELETA UMA TAREFA
    fun delete(id: Int) {
        taskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }
        })
    }

    // ATUALIZA O STATUS DA TAREFA
    fun status(id: Int, complete: Boolean) {
        val listener = object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }
        }

        if (complete) {
            taskRepository.complete(id, listener)
        } else {
            taskRepository.undo(id, listener)
        }
    }
}