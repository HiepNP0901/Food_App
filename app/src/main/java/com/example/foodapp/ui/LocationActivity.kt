package com.example.foodapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {
    private val binding: ActivityLocationBinding by lazy {
        ActivityLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val locationList = listOf("United", "States", "Canada", "Australia", "Japan",
            "Germany", "France", "Italy", "Brazil", "India", "China", "Viet Nam")
        val adapter = ArrayAdapter(this, (17367043), locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            autoCompleteTextView.error = null
        }

        binding.locationDoneButton.setOnClickListener{
            if(autoCompleteTextView.text.toString() == "Choose location"){
                autoCompleteTextView.error="Please choose a location"
                autoCompleteTextView.requestFocus()
            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}