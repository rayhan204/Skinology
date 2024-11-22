package com.example.skinology.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skinology.R
import com.example.skinology.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        setupUI()
        return root

    }

    private fun setupUI() {
        binding.recentArticle.text = getString(R.string.recent_article)
        binding.progressBar.visibility = View.GONE
        binding.button1.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_camera)
        }
        binding.button2.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_article)
        }
        binding.button3.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_history)
        }
        binding.button4.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_setting)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}