package com.example.skinology.ui.cameraX

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.skinology.R
import com.example.skinology.databinding.ActivityArticel2Binding
import com.example.skinology.databinding.ActivityCameraXBinding
import com.example.skinology.ui.article2.ButtonsFragment

class CameraX : AppCompatActivity() {
    private lateinit var binding: ActivityCameraXBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnClose.setOnClickListener() {
            onBackPressed()
        }
    }
}