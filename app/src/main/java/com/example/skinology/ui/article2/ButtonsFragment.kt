package com.example.skinology.ui.article2

import android.content.Context
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
        val buttons = listOf(
            "Dry" to binding.buttonDry,
            "Oily" to binding.buttonOily,
            "Normal" to binding.buttonNormal,
            "Acne" to binding.buttonAcne
        )

        buttons.forEach { (category, button) ->
            button.setOnClickListener {
                activateButton(button, buttons.map { it.second })
                buttonSelectionListener?.onButtonSelected(category) // Kirim kategori ke listener
            }
        }

        selectedButton?.let { category ->
            val defaultButton = buttons.firstOrNull { it.first.equals(category, ignoreCase = true) }?.second
            defaultButton?.performClick()
        }
    }


    private fun activateButton(activeButton: MaterialButton, allButtons: List<MaterialButton>) {
        allButtons.forEach { button ->
            if (button == activeButton) {
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                button.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.primary)
            } else {
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                button.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.secondary)
            }
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