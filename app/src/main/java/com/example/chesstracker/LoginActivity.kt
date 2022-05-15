package com.example.chesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.et_username).text.toString()
    }
}