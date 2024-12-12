package com.example.skinology.ui.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.skinology.databinding.FragmentArticleBinding
import com.example.skinology.ui.article2.Articel2Activity

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
            navigateToArticle2Activity("DRY")
        }

        binding.buttonOily.setOnClickListener {
            navigateToArticle2Activity("OILY")
        }

        binding.buttonNormal.setOnClickListener {
            navigateToArticle2Activity("NORMAL")
        }

        binding.buttonAcne.setOnClickListener {
            navigateToArticle2Activity("ACNE")
        }
    }


    private fun navigateToArticle2Activity(selectedButton: String) {
        Log.d("ArticleFragment", "Navigating with button: $selectedButton")
        val intent = Intent(requireContext(), Articel2Activity::class.java)
        intent.putExtra("SELECTED_BUTTON", selectedButton)
        startActivity(intent)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}