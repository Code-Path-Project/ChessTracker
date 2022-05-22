package com.example.chesstracker.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chesstracker.LoginActivity
import com.example.chesstracker.MainActivity
import com.example.chesstracker.R

class SettingsFragment : Fragment() {

    lateinit var darkmode_switch: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        darkmode_switch = view.findViewById<Switch>(R.id.dark_mode_switch)

        darkmode_switch.setOnCheckedChangeListener({ _ , isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            //Toast.makeText(this@MainActivity, message,
              //  Toast.LENGTH_SHORT).show()
        })

        view.findViewById<Button>(R.id.btn_logout).setOnClickListener(){
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /*override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }*/

}