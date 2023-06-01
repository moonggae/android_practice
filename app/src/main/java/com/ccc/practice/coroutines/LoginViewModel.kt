package com.ccc.practice.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    private val loginRepository: LoginRepository = LoginRepository(LoginResponseParser())

    private val TAG = "LoginViewModel"

    private val _username by lazy {
        MutableLiveData<String>()
    }

    val username : LiveData<String>
        get() = _username

    private val _token by lazy {
        MutableLiveData<String>()
    }

    private val _response by lazy {
        MutableLiveData<LoginResponse>()
    }

    val response : LiveData<LoginResponse>
        get() = _response

    val token: LiveData<String>
        get() = _token

    fun updateValue(username: String, token: String) {
        _username.value = username
        _token.value = token
    }

    fun login(username: String, token: String) {
        viewModelScope.launch {
            updateValue(username, token)
            val jsonBody = "{ \"username\": \"${this@LoginViewModel.username.value}\", \"token\": \"${this@LoginViewModel.token.value}\"}"

            val result = try {
                 loginRepository.makeLoginRequest(jsonBody)
            } catch (e: Exception) {
                Result.Error(java.lang.Exception(e))
            }
            
            when (result) {
                is Result.Success<LoginResponse> -> _response.value = result.data
                else -> Log.d(TAG, "LoginViewModel - login - error: ${result}")
            }
        }
    }
}