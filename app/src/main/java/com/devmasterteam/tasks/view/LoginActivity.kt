package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // VARIÁVEIS DE CLASSE
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // ATRIBUE O LAYOUT
        setContentView(binding.root)

        // ESCONDE A ACTION BAR, BARRA DO TOPO
        supportActionBar?.hide()

        // ATRIBUE OS EVENTOS DE CLICK
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        // VERIFICA SE O USUARIO ESTA LOGADO
        viewModel.verifyAuthentication()

        // OBSERVADORES
        observe()
    }

    // MÉTODO DE CLICK
    override fun onClick(v: View) {

        // FAZER LOGIN
        if (v.id == R.id.button_login) {
            handleLogin()

            // REGISTRAR USUÁRIO
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loggedUser.observe(this) {
            if (it) {
                biometricAuthentication()
            }
        }
    }

    // MÉTODO DE BIOMETRIA
    private fun biometricAuthentication() {
        val executor = ContextCompat.getMainExecutor(this)
        val bio =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            })
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Title")
            .setSubtitle("Subtitle")
            .setDescription("Description")
            .setNegativeButtonText("Cancel")
            .build()

        bio.authenticate(info)
    }

    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.doLogin(email, password)
    }
}