package com.example.chesstracker.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import com.example.chesstracker.LoginActivity
import com.example.chesstracker.MainActivity
import com.example.chesstracker.R


class SettingsFragment : Fragment()  {

    lateinit var lightMode: Button
    lateinit var darkMode: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lightMode = view.findViewById<Button>(R.id.lightMode)
        darkMode = view.findViewById<Button>(R.id.darkMode)


        lightMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }

        //lightMode.setOnClickListener()

        darkMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        }

        view.findViewById<Button>(R.id.btn_logout).setOnClickListener(){
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }



    /*override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }*/

}