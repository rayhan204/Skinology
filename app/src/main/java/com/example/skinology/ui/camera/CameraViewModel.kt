package com.example.skinology.ui.camera

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class CameraViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val IMAGE_URI_KEY = "IMAGE_URI_KEY"
    }
    var currentImageUri: Uri?
        get() = savedStateHandle[IMAGE_URI_KEY]
        set(value) {
            savedStateHandle[IMAGE_URI_KEY] = value
        }
}