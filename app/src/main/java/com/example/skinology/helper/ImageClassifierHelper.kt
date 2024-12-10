package com.example.skinology.helper

import android.content.Context
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
//ini codingan versi cintia:
import android.graphics.BitmapFactory
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.IOException

class ImageClassifierHelper(
    private val context: Context,
    private val classifierListener: ClassifierListener?,
    private var threshold: Float = 0.1f,
    private var maxResults: Int = 3,
    private val modelName: String = "converted_model_metadata.tflite"
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold) // Menggunakan nilai threshold dari properti kelas
            .setMaxResults(maxResults)
            .setBaseOptions(BaseOptions.builder().setNumThreads(4).build())
            .build()

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(context, modelName, options)
        } catch (e: IOException) {
            classifierListener?.onError("Failed to load model: ${e.message}")
            Log.e(TAG, "Error loading model", e)
        }
    }


    fun classifyStaticImage(imageUri: Uri) {
        if(imageClassifier == null) {
            setupImageClassifier()
        }

        val bitmap = context.contentResolver.openInputStream(imageUri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: return

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .add(NormalizeOp(0f, 255f)) // Normalisasi ke rentang [0, 255] jika model membutuhkan ini
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
        Log.d("ImageProcessor", "Tensor Image Dimensions: ${tensorImage.width}x${tensorImage.height}")
        Log.d("ImageProcessor", "Tensor Image Pixel Values: ${tensorImage.buffer}")

        val startTime = SystemClock.uptimeMillis()
        val results = imageClassifier?.classify(tensorImage)
        val inferenceTime = SystemClock.uptimeMillis() - startTime

        classifierListener?.onResults(results, inferenceTime)
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: List<Classifications>?, inferenceTime: Long)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}




