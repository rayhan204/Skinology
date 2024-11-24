package com.example.skinology.ui.article2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skinology.R
import com.example.skinology.databinding.ActivityArticel2Binding
import com.example.skinology.databinding.ActivityDetailArticelBinding
import com.example.skinology.databinding.FragmentArticleBinding

class Articel2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityArticel2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticel2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        if (savedInstanceState == null) {
            val fragment = ButtonsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.buttonContainerFragment, fragment)
                .commit()
        }
    }

}