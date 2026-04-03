package com.dpw.nimons360.features.families

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.dpw.nimons360.R
import com.dpw.nimons360.databinding.FragmentFamiliesBinding

class FamiliesFragment : Fragment() {

    private var _binding: FragmentFamiliesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamiliesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val isMineSelected = checkedIds.contains(R.id.chipMine)
            binding.familiesFilterStatus.text = getString(
                if (isMineSelected) R.string.filter_status_mine else R.string.filter_status_all
            )
        }

        binding.searchInput.doAfterTextChanged { text ->
            val query = text?.toString().orEmpty().trim()
            binding.familiesPlaceholder.text = if (query.isBlank()) {
                getString(R.string.placeholder_families)
            } else {
                getString(R.string.placeholder_families_query, query)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
