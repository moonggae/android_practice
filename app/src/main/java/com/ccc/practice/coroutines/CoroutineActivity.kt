package com.ccc.practice.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ccc.practice.R
import com.ccc.practice.databinding.ActivityCoroutineBinding
import kotlinx.coroutines.launch

class CoroutineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutineBinding
    private lateinit var loginViewModel: LoginViewModel

    companion object {
        private const val TAG = "CoroutineActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]


        binding.loginButton.setOnClickListener {
            val username = binding.usernameEdittext.text.toString()
            val token = binding.tokenEdittext.text.toString()
            loginViewModel.login(username, token)
        }

        loginViewModel.response.observe(this) { response ->
            Toast.makeText(
                this,
                "username: ${response.username}, token: ${response.token}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}