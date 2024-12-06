package com.example.skinology.ui.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.skinology.databinding.FragmentCameraBinding
import com.example.skinology.helper.ImageClassifierHelper
import com.example.skinology.ui.cameraX.CameraX
import com.example.skinology.ui.result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val cameraViewModel: CameraViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUI()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast("Error: $error")
                }

                override fun onResult(result: List<Classifications>?, inferenceTime: Long) {
                    result?.let { classifications ->
                        if (classifications.isNotEmpty() && classifications[0].categories.isNotEmpty()) {
                            val label = classifications[0].categories[0].label
                            val score = NumberFormat.getPercentInstance()
                                .format(classifications[0].categories[0].score).trim()

                            val displayResult = "$label\n$score"
                            moveToResult(displayResult)
                        } else {
                            showToast("Hasil prediksi tidak ditemukan")
                        }
                    }
                }
            }
        )

        binding.buttonImg.setOnClickListener { startGallery() }
        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.buttonAnalyze.setOnClickListener { analyze() }

        showImage()
    }

    private val cropActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                val croppedUri = UCrop.getOutput(data!!)
                if (croppedUri != null) {
                    cameraViewModel.currentImageUri = croppedUri
                    showImage()
                } else {
                    showToast("Crop failed: No URI found")
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = UCrop.getError(result.data!!)
                showToast("Crop error: ${cropError?.message}")
            }
        }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(450, 450)
            .getIntent(requireActivity())
            .let { cropActivityResultLauncher.launch(it) }
    }

    private fun setupUI() {
        binding.progressBar.visibility = View.GONE
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            cameraViewModel.currentImageUri = uri
            startCrop(uri)
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraX::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraX.CAMERAX_RESULT) {
            val imageUri = it.data?.getStringExtra(CameraX.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (imageUri != null) {
                startCrop(imageUri)
            }
        }
    }

    private fun analyze() {
        cameraViewModel.currentImageUri?.let {
            imageClassifierHelper.classifyStaticImage(it)
        } ?: run {
            showToast("Silahkan pilih gambar terlebih dahulu")
        }
    }

    private fun moveToResult(displayResult: String) {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra("IMAGE_URI", cameraViewModel.currentImageUri.toString())
            putExtra("RESULT", displayResult)
        }
        startActivity(intent)
    }

    private fun showImage() {
        cameraViewModel.currentImageUri?.let { uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.previewImageView.setImageURI(null)
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
