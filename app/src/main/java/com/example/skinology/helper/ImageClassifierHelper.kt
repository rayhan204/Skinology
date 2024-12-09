package com.example.skinology.helper

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import com.example.skinology.R
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.lang.IllegalStateException

/*class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResult: Int = 3,
    val modelName: String = "converted_model_metadata.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResult)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionBuilder.build()
            )
        }catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        val bitmap = toBitmap(imageUri) ?: return

        val argbBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val image = TensorImage.fromBitmap(argbBitmap)
        val outputs = imageClassifier?.classify(image)
        var inferenceTime = SystemClock.uptimeMillis()
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        outputs?.forEach { classification ->
            classification.categories.forEach { category ->
                Log.d(
                    "Classification Result",
                    "Label: ${category.label}, Confidence: ${category.score}"
                )
            }
        }
        classifierListener?.onResult(outputs, inferenceTime)

    }



    private fun toBitmap(imageUri: Uri): Bitmap? {
        return try {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.isMutableRequired = true
                decoder.setTargetColorSpace(android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error converting URI to Bitmap: ${e.message}")
            null
        }
    }


    interface ClassifierListener {
        fun onError(error: String)

        fun onResult (
            result: List<Classifications>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}*/

//ini codingan versi cintia:
import android.graphics.BitmapFactory
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
        val bitmap = context.contentResolver.openInputStream(imageUri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: return

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR)) // Sesuaikan dengan input size model
            .add(NormalizeOp(0f, 255f)) // Normalisasi 0-1 jika model membutuhkan ini
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

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




