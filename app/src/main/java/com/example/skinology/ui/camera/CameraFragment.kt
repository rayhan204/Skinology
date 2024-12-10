package com.example.skinology.ui.camera

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skinology.databinding.FragmentCameraBinding
import com.example.skinology.helper.ImageClassifierHelper
import com.example.skinology.ui.cameraX.CameraX
import com.example.skinology.ui.result.ResultActivity
import com.yalantis.ucrop.UCrop
import com.example.skinology.ViewModelFactory
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

//ini codingan versi cintia

class CameraFragment : Fragment(), ImageClassifierHelper.ClassifierListener {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var classificationResult: String? = null
    private val cameraViewModel: CameraViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            cameraViewModel.currentImageUri = it
            startCrop(it)
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private val cropActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                val croppedUri = UCrop.getOutput(data!!)
                if (croppedUri != null) {
                    cameraViewModel.currentImageUri = croppedUri
                    binding.previewImageView.setImageURI(croppedUri)
                } else {
                    showToast("Crop failed: No URI found")
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = UCrop.getError(result.data!!)
                showToast("Crop error: ${cropError?.message}")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageClassifierHelper = ImageClassifierHelper(requireContext(), this)

        cameraViewModel.currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
        binding.buttonImg.setOnClickListener { startGallery() }
        binding.buttonAnalyze.setOnClickListener { analyzeImage() }
        binding.buttonCamera.setOnClickListener { startCameraX() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(450, 450)
            .getIntent(requireActivity())
            .let { cropActivityResultLauncher.launch(it) }
    }

    private fun analyzeImage() {
        cameraViewModel.currentImageUri?.let { uri ->
            lifecycleScope.launch {
                imageClassifierHelper.classifyStaticImage(uri)
            }
        } ?: showToast("No image selected")
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

    private fun moveToResult() {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_IMAGE_URI, cameraViewModel.currentImageUri.toString())
            putExtra(ResultActivity.EXTRA_RESULT, classificationResult)
        }
        startActivity(intent)
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
        results?.firstOrNull()?.categories?.let { categories ->
            val topCategory = categories.maxByOrNull { it.score }

            if (topCategory != null) {
                classificationResult = "${topCategory.label}: ${(topCategory.score * 100).toInt()}%"
                Log.d("Classification", "Top Label: ${topCategory.label}, Score: ${(topCategory.score * 100).toInt()}%")
            } else {
                classificationResult = "No result"
            }
        } ?: run {
            classificationResult = "No result"
        }

        moveToResult()
    }




    override fun onError(error: String) {
        showToast("Error: $error")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

