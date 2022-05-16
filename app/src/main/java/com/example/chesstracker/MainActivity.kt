package com.example.chesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // navigation bar
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item ->
            when(item.itemId) {
                R.id.action_main -> {
                    Toast.makeText(this, "main page", Toast.LENGTH_SHORT).show()
                }

                R.id.action_friends -> {
                    Toast.makeText(this, "friend page", Toast.LENGTH_SHORT).show()

                }

                R.id.action_settings -> {
                    Toast.makeText(this, "settings page", Toast.LENGTH_SHORT).show()

                }
            }

            true
        }

    }
}