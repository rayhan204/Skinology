package com.example.skinology.helper


import android.graphics.BitmapFactory
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ImageClassifierHelper(
    private val context: Context,
    private val classifierListener: ClassifierListener?,
    private val modelName: String = "converted_model_metadata.tflite",
    private val inputSize: Int = 224,
    private val numClasses: Int = 4
) {
    private lateinit var tflite: Interpreter

    // Peta label untuk kelas output
    private val labels = listOf("Berjerawat", "Berminyak", "Kering", "Normal")

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        try {
            tflite = Interpreter(loadModelFile())
        } catch (e: Exception) {
            classifierListener?.onError("Error initializing TensorFlow Lite interpreter: ${e.message}")
            Log.e(TAG, "Error initializing TensorFlow Lite interpreter", e)
        }
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val value = intValues[pixel++]
                byteBuffer.putFloat(((value shr 16) and 0xFF) * (1f / 255f))
                byteBuffer.putFloat(((value shr 8) and 0xFF) * (1f / 255f))
                byteBuffer.putFloat((value and 0xFF) * (1f / 255f))
            }
        }
        return byteBuffer
    }

    fun classifyStaticImage(imageUri: Uri) {
        try {
            val bitmap = context.contentResolver.openInputStream(imageUri)?.use {
                BitmapFactory.decodeStream(it)
            } ?: throw IllegalArgumentException("Unable to decode image from URI")

            val inputBuffer = convertBitmapToByteBuffer(bitmap)
            val output = Array(1) { FloatArray(numClasses) }

            val startTime = System.currentTimeMillis()
            tflite.run(inputBuffer, output)
            val inferenceTime = System.currentTimeMillis() - startTime

            val results = output[0].mapIndexed { index, confidence ->
                ClassificationResult(labels.getOrNull(index) ?: "Unknown", confidence)
            }.sortedByDescending { it.confidence }

            classifierListener?.onResults(results, inferenceTime)
        } catch (e: Exception) {
            classifierListener?.onError("Error during image classification: ${e.message}")
            Log.e(TAG, "Error during image classification", e)
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: List<ClassificationResult>, inferenceTime: Long)
    }

    data class ClassificationResult(
        val label: String,
        val confidence: Float
    )

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}




