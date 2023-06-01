package com.ccc.practice.coroutines

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class LoginResponseParser {
    fun parse(inputStream: InputStream) : LoginResponse {
        val reader = BufferedReader(inputStream.reader())
        var content: String
        reader.use { reader ->
            content = reader.readText()
        }
        return LoginResponse(content)
    }
}

data class LoginResponse(val body: String) {

    val username: String
        get() {
            val jsonObject = JSONObject(body)
            return jsonObject.get("username") as String
        }

    val token: String
        get() {
            val jsonObject = JSONObject(body)
            return jsonObject.get("token") as String
        }
}

class LoginRepository(private val responseParser: LoginResponseParser) {
    private val loginUrl = "http://10.0.2.2:3000/login"

    companion object {
        private const val TAG = "LoginRepository"
    }

    suspend fun makeLoginRequest(jsonBody: String) : Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            val url = URL(loginUrl)
            (url.openConnection() as? HttpURLConnection)?.run {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                setRequestProperty("Accept", "application/json")
                doOutput = true
                val outputBytes = jsonBody.toByteArray(StandardCharsets.UTF_8)
                outputStream.write(outputBytes, 0, outputBytes.size)
                outputStream.flush()
                outputStream.close()
                return@withContext Result.Success(responseParser.parse(inputStream))
            }
            return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
        }
    }
}