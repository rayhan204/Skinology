package com.example.skinology.ui.result

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.skinology.R
import com.example.skinology.ViewModelFactory
import com.example.skinology.data.local.entity.HistoryEntity
import com.example.skinology.databinding.ActivityResultBinding
import com.example.skinology.ui.camera.CameraFragment
import java.text.SimpleDateFormat
import java.util.Locale


//ini codingan versi cintia
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var imageUri: Uri? = null
    private var classificationResult: String? = null
    private val resultViewModel: ResultViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

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

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = dateFormatter.format(System.currentTimeMillis())

        binding.btnSave.setOnClickListener{
            val historyEntity = HistoryEntity(
                id = System.currentTimeMillis().toString(),
                prediction = classificationResult ?: "No Result",
                date = formattedDate,
                image = imageUri.toString()
            )
            resultViewModel.insertHistory(historyEntity)
            Toast.makeText(this, "history disimpan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun navigateToCameraFragment() {
        val intent = Intent(this, CameraFragment::class.java).apply {
            putExtra("FRAGMENT", "CameraFragment")
        }
        startActivity(intent)
    }

    private fun displayImage() {
        if (imageUri != null) {
            binding.imageView.setImageURI(imageUri)
        } else {
            binding.imageView.setImageResource(R.drawable.item_history) // Ganti dengan gambar placeholder yang sesuai
        }
    }

    @SuppressLint("SetTextI18n")
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



