package com.example.chesstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.chesstracker.Fragments.FriendsFragment
import com.example.chesstracker.Fragments.MainFragment
import com.example.chesstracker.Fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // navigation bar
        val fragmentManager: FragmentManager = supportFragmentManager
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item ->

            var fragmentToShow: Fragment? = null
            when(item.itemId) {
                R.id.action_main -> {
                    fragmentToShow = MainFragment()
                }

                R.id.action_friends -> {
                    fragmentToShow = FriendsFragment()
                }

                R.id.action_settings -> {
                    fragmentToShow = SettingsFragment()
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            true
        }
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_main

    }
}