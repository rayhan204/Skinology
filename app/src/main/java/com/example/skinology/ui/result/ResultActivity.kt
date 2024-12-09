package com.example.skinology.ui.result

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.skinology.R
import com.example.skinology.data.local.entity.HistoryEntity
import com.example.skinology.data.local.room.SkinologyDatabase
import com.example.skinology.databinding.ActivityResultBinding
import com.example.skinology.helper.ImageClassifierHelper
import com.example.skinology.ui.camera.CameraFragment
import com.example.skinology.ui.cameraX.CameraX
import com.example.skinology.ui.history.HistoryFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*class ResultActivity : AppCompatActivity() {
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
}*/

//ini codingan versi cintia
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var imageUri: Uri? = null
    private var classificationResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
        classificationResult = intent.getStringExtra(EXTRA_RESULT)

        // Debugging untuk memastikan data diterima
        Log.d(TAG, "Image URI: $imageUri")
        Log.d(TAG, "Classification Result: $classificationResult")


        // Tampilkan data di UI
        displayImage()
        displayResults()

        // Atur toolbar
        setupToolbar()

        binding.buttonCamera.setOnClickListener {
            navigateToCameraFragment()
        }
    }
    private fun navigateToCameraFragment() {
        val intent = Intent(this, CameraFragment::class.java).apply {
            putExtra("FRAGMENT", "CameraFragment")
        }
        startActivity(intent)
        finish() // Tutup ResultActivity
    }

    private fun displayImage() {
        if (imageUri != null) {
            binding.imageView.setImageURI(imageUri)
        } else {
            binding.imageView.setImageResource(R.drawable.item_history) // Ganti dengan gambar placeholder yang sesuai
        }
    }

    private fun displayResults() {
        if (!classificationResult.isNullOrEmpty()) {
            binding.descOnboarding.text = classificationResult
        } else {
            binding.descOnboarding.text = "Hasil tidak ditemukan. Silakan coba lagi."
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        private const val TAG = "ResultActivity"
    }
}



