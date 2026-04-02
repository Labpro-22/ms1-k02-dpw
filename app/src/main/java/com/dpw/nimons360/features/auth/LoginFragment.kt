package com.dpw.nimons360.features.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dpw.nimons360.R
import com.dpw.nimons360.core.network.NetworkModule
import com.dpw.nimons360.core.utils.SessionManager
import com.dpw.nimons360.data.repository.AuthRepository
import com.dpw.nimons360.data.repository.LoginResult
import com.dpw.nimons360.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContext = requireContext().applicationContext
        sessionManager = SessionManager(appContext)
        authRepository = AuthRepository(
            apiService = NetworkModule.createApiService(appContext),
            sessionManager = sessionManager
        )

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text?.toString().orEmpty().trim()
            val password = binding.passwordInput.text?.toString().orEmpty().trim()

            if (email.isBlank() || password.isBlank()) {
                showMessage(getString(R.string.error_login_required_fields))
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                setLoadingState(true)
                when (val result = authRepository.login(email = email, password = password)) {
                    is LoginResult.Success -> {
                        findNavController().navigate(
                            R.id.homeFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.loginFragment, true)
                                .build()
                        )
                    }

                    is LoginResult.Error -> {
                        showMessage(result.message)
                    }
                }
                setLoadingState(false)
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.loginButton.isEnabled = !isLoading
        binding.emailInput.isEnabled = !isLoading
        binding.passwordInput.isEnabled = !isLoading
        binding.loginButton.text = if (isLoading) {
            "Signing in..."
        } else {
            getString(R.string.action_sign_in)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

