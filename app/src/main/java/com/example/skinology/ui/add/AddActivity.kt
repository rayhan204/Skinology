package com.example.skinology.ui.add

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skinology.MainActivity
import com.example.skinology.R
import com.example.skinology.ViewModelFactory
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.databinding.ActivityAddBinding
import com.example.skinology.databinding.ActivityEditArticleBinding
import com.example.skinology.ui.article2.Articel2Activity
import com.example.skinology.ui.detailarticle.DetailArticleActivity
import com.example.skinology.ui.detailarticle.DetailViewModel
import kotlinx.coroutines.MainScope
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    private val viewModel: AddViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var base64String = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val selectedImageUri = data?.data
            selectedImageUri?.let {

                base64String = uriToBase64(it).toString()
                binding.etImageAdd.setText("Image Terpilih")
                binding.addPhoto.setImageURI(it)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        setImage()

    }

    private fun setImage() {
        binding.etImageAdd.setOnClickListener {
            val pickImageIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageIntent.type = "image/*"
            startActivityForResult(pickImageIntent, 1)
        }
    }

    private fun setButton() {
        binding.btnSubmit.setOnClickListener {
            val selectedButton = intent.getStringExtra("SELECTED_BUTTON")

            val newName = binding.etJudul.text.toString()
            val newDescription = binding.etDescription.text.toString()

            val article = selectedButton?.let { it1 ->
                ArticleEntity(
                    id = UUID.randomUUID().toString(),
                    name = newName,
                    description = newDescription,
                    photo = base64String,
                    category = it1
                )
            }
            Log.d("photo", "setButton: $base64String")

            viewModel.addArticles(listOf(article) as List<ArticleEntity>)
            Toast.makeText(this, "Article added successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun uriToBase64(uri: Uri): String? {
        val contentResolver = this.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()

        val resizedBitmap = resizeBitmap(bitmap, 800, 800)
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream)


        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val aspectRatio = width.toFloat() / height.toFloat()

        val newWidth = if (aspectRatio > 1) maxWidth else (maxHeight * aspectRatio).toInt()
        val newHeight = if (aspectRatio > 1) (maxWidth / aspectRatio).toInt() else maxHeight

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}

