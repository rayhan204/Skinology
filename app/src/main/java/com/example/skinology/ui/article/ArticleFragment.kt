package com.example.skinology.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skinology.databinding.FragmentArticleBinding
import com.example.skinology.databinding.FragmentCameraBinding
import com.example.skinology.ui.article2.Articel2Activity
import com.example.skinology.ui.cameraX.CameraX

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUI()
        return root
    }

    private fun setupUI() {
        binding.progressBar.visibility = View.GONE
        binding.buttonDry.setOnClickListener {
            val intent = Intent(requireContext(), Articel2Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}