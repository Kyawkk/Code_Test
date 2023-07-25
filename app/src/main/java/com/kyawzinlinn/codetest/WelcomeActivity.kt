package com.kyawzinlinn.codetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import com.kyawzinlinn.codetest.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        setContentView(binding.root)

        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.apply {
            btnCreateNewAccount.setOnClickListener { startCreateAccountActivity() }
        }
    }

    private fun startCreateAccountActivity() {
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }
}