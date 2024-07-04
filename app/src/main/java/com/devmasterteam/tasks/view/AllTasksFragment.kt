package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.tasks.databinding.FragmentAllTasksBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.view.adapter.TaskAdapter
import com.devmasterteam.tasks.viewmodel.TaskListViewModel

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val adapter = TaskAdapter()

    private var taskFilter = 0

    // MÉTODO ONCREATEVIEW PARA INICIALIZAR O FRAGMENT
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        // IDENTIFICAR O ELEMENTO, LAYOUT PARA A RECYCLER VIEW, ADAPTER FAZ A COLA LAYOUT/DADOS
        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllTasks.adapter = adapter

        taskFilter = requireArguments().getInt(TaskConstants.BUNDLE.TASKFILTER, 0)

        // CRIA O LISTENER
        val listener = object : TaskListener {

            // CLICK NA TAREFA
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            // CLICK NO BOTÃO DELETAR A TASK
            override fun onDeleteClick(id: Int) {
                viewModel.delete(id)
            }

            // CLICK NO BOTÃO COMPLETAR A TASK
            override fun onCompleteClick(id: Int) {
                viewModel.status(id, true)
            }

            // CLICK NO BOTÃO DESFAZER A TASK
            override fun onUndoClick(id: Int) {
                viewModel.status(id, false)
            }
        }

        // ATRIBUI O LISTENER
        adapter.attachListener(listener)

        // CRIA OS OBSERVADORES
        observe()

        return binding.root
    }

    // MÉTODO ONRESUME PARA ATUALIZAR A LISTA DE TAREFAS
    override fun onResume() {
        super.onResume()
        viewModel.list(taskFilter)
    }

    // MÉTODO ONDESTROYVIEW PARA DESTRUIR O FRAGMENT
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // MÉTODO PARA OBSERVAR AS MUDANÇAS NA LISTA DE TAREFAS
    private fun observe() {
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.updateTasks(it)
        }
        viewModel.delete.observe(viewLifecycleOwner) {
            if (!it.status()) {
                Toast.makeText(context, it.message(), Toast.LENGTH_LONG).show()
            }
        }
        viewModel.status.observe(viewLifecycleOwner) {
            if (!it.status()) {
                Toast.makeText(context, it.message(), Toast.LENGTH_LONG).show()
            }
        }
    }
}