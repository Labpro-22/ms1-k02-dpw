package com.dpw.nimons360.features.families

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dpw.nimons360.R
import com.dpw.nimons360.databinding.FragmentCreateFamilyBinding

class CreateFamilyFragment : Fragment() {

    private var _binding: FragmentCreateFamilyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateFamilyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createFamilyButton.setOnClickListener {
            val familyName = binding.familyNameInput.text?.toString().orEmpty().trim()
            if (familyName.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    R.string.error_family_name_required,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            Toast.makeText(
                requireContext(),
                getString(R.string.message_create_family_todo, familyName),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

