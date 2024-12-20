package com.example.skinology.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skinology.R
import com.example.skinology.ViewModelFactory
import com.example.skinology.adapter.ArticleAdapter
import com.example.skinology.data.Result
import com.example.skinology.databinding.FragmentHomeBinding
import com.example.skinology.ui.detailarticle.DetailArticleActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        articleAdapter = ArticleAdapter { article ->
            val intent = Intent(requireContext(), DetailArticleActivity::class.java)
            intent.putExtra("ARTICLE_ID", article.id)
            startActivity(intent)
        }

        with(binding.rvArticle) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }

        binding.button1.setOnClickListener {
            findNavController().navigate(R.id.navigation_camera)
        }
        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.navigation_article)
        }
        binding.button3.setOnClickListener {
            findNavController().navigate(R.id.navigation_history)
        }
        binding.button4.setOnClickListener {
            findNavController().navigate(R.id.navigation_setting)
        }
    }

    private fun observeViewModel() {
        homeViewModel.getAllSkinTypes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val articles = result.data
                    if (articles.isNotEmpty()) {
                        Log.d("HomeFragment", "Articles loaded: $articles")
                        articleAdapter.submitList(articles.toMutableList())
                    } else {
                        Log.d("HomeFragment", "Articles list is empty or null")
                        Toast.makeText(requireContext(), "No articles available", Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}