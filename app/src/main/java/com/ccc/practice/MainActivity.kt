package com.ccc.practice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ccc.practice.coroutines.CoroutineActivity
import com.ccc.practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.coroutineButton.setOnClickListener {
            startActivity(Intent(this, CoroutineActivity::class.java))
        }
    }
}