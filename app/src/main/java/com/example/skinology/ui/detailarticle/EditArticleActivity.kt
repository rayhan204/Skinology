package com.example.skinology.ui.detailarticle

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.skinology.MainActivity
import com.example.skinology.R
import com.example.skinology.ViewModelFactory
import com.example.skinology.data.Result
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.databinding.ActivityDetailArticelBinding
import com.example.skinology.databinding.ActivityEditArticleBinding
import com.example.skinology.ui.article2.Articel2Activity
import java.io.ByteArrayOutputStream

class EditArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditArticleBinding

    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var getImage = ""
    private var base64String = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val selectedImageUri = data?.data
            selectedImageUri?.let {

                base64String = uriToBase64(it).toString()
                binding.etImageAdd.setText("Image Terpilih!")
                binding.addPhoto.setImageURI(it)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleId = intent.getStringExtra("ARTICLE_ID")
        if (articleId != null) {
            observeViewModel(articleId)
        } else {
            Toast.makeText(this, "Article id tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        setupToolbar()
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
            val articleId = intent.getStringExtra("ARTICLE_ID")
            val newName = binding.etJudul.text.toString()
            val newDescription = binding.etDescription.text.toString()

            if (articleId != null) {
                viewModel.updateArticleDetails(
                    articleId = articleId,
                    newName = newName,
                    newPhoto = if (base64String.isNotEmpty()) {
                        base64String
                    } else {
                        getImage
                    },
                    newDescription = newDescription
                )
                Toast.makeText(this, "Artikel berhasil diubah", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.btnDelete.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Article")
            builder.setMessage("Do you want to delete article?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                val articleId = intent.getStringExtra("ARTICLE_ID")

                if (articleId != null) {
                    viewModel.deleteArticlesById(articleId)
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            builder.setNegativeButton("No"){dialog, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel(articleId: String) {
        viewModel.getArticleId(articleId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    updateUI(result.data)
                }

                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateUI(article: ArticleEntity) {

        binding.apply {
            etJudul.setText(article.name)
            etDescription.setText(article.description)
            etImageAdd.setText("Klik untuk edit image!")
            getImage = article.photo
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