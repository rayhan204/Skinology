package com.example.skinology.ui.result

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.skinology.R
import com.example.skinology.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val results = intent.getStringExtra("RESULT")
        val imageUri = intent.getStringExtra("IMAGE_URI")

        imageUri?.let {
            binding.imageView.setImageURI(Uri.parse(it))
        }

        results?.let { displayResults(it) }
    }

    private fun displayResults(results: String) {
        binding.descOnboarding.text = results
    }
}