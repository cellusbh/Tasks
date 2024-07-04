package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository(val context: Context) {

    // RETORNA A RESPOSTA EM JSON
    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }

    // EXECUTA A CHAMADA DO RETROFIT E TRATA O RETORNO
    fun <T> executeCall(call: Call<T>, listener: APIListener<T>) {
        call.enqueue(object : Callback<T> {

            // TRATA RESPOSTAS DE SUCESSO
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string()))
                }
            }

            // TRATA ERROS DE CONEXÃO
            override fun onFailure(call: Call<T>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    // VERIFICA SE A CONEXÃO ESTÁ DISPONÍVEL
    fun isConnectionAvailable(): Boolean {
        var result = false

        // PEGA O GERENCIADOR DE CONEXAO, PEGA A REDE ATIVA E VERIFICA AS FUNCIONALIDADES DA REDE
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // VERSOES MAIS NOVAS DO ANDROID USAM ESSE CODIGO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNet = cm.activeNetwork ?: return false
            val networkCapabilities = cm.getNetworkCapabilities(activeNet) ?: return false
            result = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {

            // VERSOES MAIS ANTIGAS DO ANDROID USAM ESSE CODIGO
            if (cm.activeNetworkInfo != null) {
                result = when (cm.activeNetworkInfo!!.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return result
    }
}