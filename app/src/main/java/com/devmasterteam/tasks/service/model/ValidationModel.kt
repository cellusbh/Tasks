package com.devmasterteam.tasks.service.model

// ESSA CLASSE É UTILIZADA PARA VALIDAÇÃO DE DADOS
class ValidationModel(message: String = "") {

    // ATRIBUTOS DA CLASSE DE VALIDAÇÃO
    private var status: Boolean = true
    private var validationMessage: String = ""

    // VALIDAÇÃO DE DADOS
    init {
        if (message.isNotEmpty()) {
            validationMessage = message
            status = false
        }
    }

    // RETORNA O STATUS DA VALIDAÇÃO
    fun status() = status
    fun message() = validationMessage

}