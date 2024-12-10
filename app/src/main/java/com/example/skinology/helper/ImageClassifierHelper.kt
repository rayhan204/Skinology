package com.example.skinology.helper

import android.os.SystemClock

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

/*class ImageClassifierHelper(
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
}*/

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




