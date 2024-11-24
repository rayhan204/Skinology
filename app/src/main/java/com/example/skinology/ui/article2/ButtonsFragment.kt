package com.example.skinology.ui.article2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.skinology.R
import com.example.skinology.databinding.FragmentButtonsBinding

class ButtonsFragment : Fragment() {
    private var _binding: FragmentButtonsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentButtonsBinding.inflate(inflater, container, false)

        // Logika untuk tombol Dry Skin
        binding.buttonDry.setOnClickListener {
            binding.buttonDry.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))  // Mengubah teks menjadi putih
            binding.buttonDry.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary) // Menghapus backgroundTint

            // Mengubah tombol lainnya ke default (jika ada)
            binding.buttonOily.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            binding.buttonOily.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondary)

            binding.buttonNormal.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            binding.buttonNormal.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondary)
        }

        // Logika untuk tombol Oily Skin
        binding.buttonOily.setOnClickListener {
            binding.buttonOily.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))  // Mengubah teks menjadi putih
            binding.buttonOily.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary)  // Menghapus backgroundTint

            // Mengubah tombol lainnya ke default
            binding.buttonDry.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            binding.buttonDry.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondary)

            binding.buttonNormal.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            binding.buttonNormal.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondary)
        }

        // Logika untuk tombol Normal Skin
        binding.buttonNormal.setOnClickListener {
            binding.buttonNormal.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))  // Mengubah teks menjadi putih
            binding.buttonNormal.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary)  // Menghapus backgroundTint

            // Mengubah tombol lainnya ke default
            binding.buttonDry.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            binding.buttonDry.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondary)

            binding.buttonOily.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            binding.buttonOily.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondary)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

