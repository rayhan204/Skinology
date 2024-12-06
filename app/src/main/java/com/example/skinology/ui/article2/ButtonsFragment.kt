package com.example.skinology.ui.article2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.skinology.R
import com.example.skinology.databinding.FragmentButtonsBinding
import com.google.android.material.button.MaterialButton

class ButtonsFragment : Fragment() {
    private var _binding: FragmentButtonsBinding? = null
    private val binding get() = _binding!!
    private var selectedButton: String? = null
    private var buttonSelectionListener: ButtonSelectionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedButton = it.getString("SELECTED_BUTTON")
            Log.d("ButtonsFragment", "Selected button: $selectedButton") // Debug log
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ButtonSelectionListener) {
            buttonSelectionListener = context
        } else {
            throw RuntimeException("$context must implement ButtonSelectionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentButtonsBinding.inflate(inflater, container, false)

        setupButtonBehavior()
        return binding.root
    }

    private fun setupButtonBehavior() {
        binding.buttonDry.setOnClickListener {
            activateButton(binding.buttonDry, binding.buttonOily, binding.buttonNormal)
            buttonSelectionListener?.onButtonSelected("DRY")
        }
        binding.buttonOily.setOnClickListener {
            activateButton(binding.buttonOily, binding.buttonDry, binding.buttonNormal)
            buttonSelectionListener?.onButtonSelected("OILY")
        }
        binding.buttonNormal.setOnClickListener {
            activateButton(binding.buttonNormal, binding.buttonDry, binding.buttonOily)
            buttonSelectionListener?.onButtonSelected("NORMAL")
        }

        when (selectedButton) {
            "DRY" -> binding.buttonDry.performClick()
            "OILY" -> binding.buttonOily.performClick()
            "NORMAL" -> binding.buttonNormal.performClick()
            else -> { "Tidak ada yang dipilih" }
        }
    }
    private fun activateButton(active: View, inactive1: View, inactive2: View) {
        (active as? MaterialButton)?.apply {
            setTextColor(ContextCompat.getColor(context ?: return, android.R.color.white))
            backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary)
        }
        (inactive1 as? MaterialButton)?.apply {
            setTextColor(ContextCompat.getColor(context ?: return, android.R.color.black))
            backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondary)
        }
        (inactive2 as? MaterialButton)?.apply {
            setTextColor(ContextCompat.getColor(context ?: return, android.R.color.black))
            backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.secondary)
        }
    }

    override fun onDetach() {
        super.onDetach()
        buttonSelectionListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(selectedButton: String?) = ButtonsFragment().apply {
            arguments = Bundle().apply {
                putString("SELECTED_BUTTON", selectedButton)
            }
        }
    }

    interface ButtonSelectionListener {
        fun onButtonSelected(buttonType: String)
    }
}

