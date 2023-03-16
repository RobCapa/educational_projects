package com.example.myunsplash.fragments.login

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.R
import com.example.myunsplash.contracts.AuthActivityContract
import com.example.myunsplash.contracts.LogoutContract
import com.example.myunsplash.databinding.FragLoginBinding
import com.example.myunsplash.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.frag_login) {
    private val binding: FragLoginBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModels()
    @Inject lateinit var authActivityContract: AuthActivityContract
    private lateinit var authLauncher: ActivityResultLauncher<String?>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeToLiveData()

        authLauncher = registerForActivityResult(
            authActivityContract,
            viewModel::processResultAuthRequest
        )

        binding.fragLogButtonLog.setOnClickListener { authLauncher.launch(null) }
    }

    private fun observeToLiveData() {
        viewModel.authResult.observe(viewLifecycleOwner, ::processAuthResult)
        viewModel.exception.observe(viewLifecycleOwner, ::handlerViewModel)
    }

    private fun processAuthResult(isLoggingSucceeded: Boolean) {
        if (isLoggingSucceeded) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        } else {
            binding.root.showSnackbar(R.string.notification_failedLogin)
        }
    }

    private fun handlerViewModel(ex: Throwable) {
        when (ex) {
            is SocketTimeoutException, is UnknownHostException -> {
                binding.root.showSnackbar(R.string.notification_noConnection)
            }
        }
    }
}


