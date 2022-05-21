package com.example.chesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.github.farshidroohi.LineChart

class DetailUserActivity : AppCompatActivity() {

    lateinit var tvUsername: TextView
    lateinit var lineGraph: LineChart
    lateinit var rv: RecyclerView
    lateinit var btnAddFriend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        tvUsername = findViewById(R.id.tv_name)
        lineGraph = findViewById(R.id.lineChart)
        rv = findViewById(R.id.rv)
        btnAddFriend = findViewById(R.id.btnAddFriend)

        btnAddFriend.setOnClickListener {
            Toast.makeText(this, "add friend button pressed", Toast.LENGTH_SHORT).show()
        }

    }
}