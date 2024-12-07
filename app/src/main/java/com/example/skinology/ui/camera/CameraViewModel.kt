package com.example.skinology.ui.camera

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.skinology.data.SkinologyRepository

class CameraViewModel(private val repository: SkinologyRepository) : ViewModel() {

   var currentImageUri: Uri? = null
}