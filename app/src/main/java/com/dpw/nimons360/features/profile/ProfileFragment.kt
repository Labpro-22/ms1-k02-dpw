package com.dpw.nimons360.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dpw.nimons360.R
import com.dpw.nimons360.core.utils.SessionManager
import com.dpw.nimons360.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext().applicationContext)
        renderProfile(sessionManager)

        binding.editNameButton.setOnClickListener {
            Toast.makeText(requireContext(), R.string.message_edit_name_todo, Toast.LENGTH_SHORT).show()
        }

        binding.signOutButton.setOnClickListener {
            sessionManager.clearSession()
            findNavController().navigate(
                R.id.loginFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.main_nav_graph, true).build()
            )
        }
    }

    private fun renderProfile(sessionManager: SessionManager) {
        val fullName = sessionManager.getFullName().ifBlank {
            getString(R.string.profile_unknown_name)
        }
        val email = sessionManager.getEmail().ifBlank {
            getString(R.string.profile_unknown_email)
        }

        binding.fullNameText.text = fullName
        binding.emailText.text = email
        binding.avatarText.text = fullName.firstOrNull()?.uppercase() ?: "N"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
