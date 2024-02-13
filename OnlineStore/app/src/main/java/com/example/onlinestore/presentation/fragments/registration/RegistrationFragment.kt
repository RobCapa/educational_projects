package com.example.onlinestore.presentation.fragments.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.models.User
import com.example.onlinestore.R
import com.example.onlinestore.app.OnlineStoreApp
import com.example.onlinestore.databinding.FragRegistrationBinding
import com.example.onlinestore.presentation.fragments.registration.utils.NameValidator
import com.example.onlinestore.presentation.fragments.registration.utils.PhoneNumberWatcher
import com.example.onlinestore.presentation.utils.DEFAULT_USER_ID
import com.example.onlinestore.presentation.utils.ViewModelFactory
import javax.inject.Inject

class RegistrationFragment : Fragment(R.layout.frag_registration) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<RegistrationViewModel>

    private val binding: FragRegistrationBinding by viewBinding()
    private val viewModel: RegistrationViewModel by viewModels { viewModelFactory }

    private val requiredDigitsNumberToValidatePhone = lazy {
        binding.fragRegistrationEditTextPhoneNumber
            .mask
            .split("#")
            .lastIndex
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as OnlineStoreApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToViewModelResults()
        setOnClickListeners()
        setTextChangedListeners()
    }

    private fun observeToViewModelResults() {
        viewModel.userWasSavedLive.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                RegistrationFragmentDirections
                    .actionRegistrationFragmentToMainFragment()
                    .let(findNavController()::navigate)
            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            fragRegistrationButtonLog.setOnClickListener {
                val firstName = fragRegistrationEditTextFirstName.text.toString()
                val lastName = fragRegistrationEditTextLastName.text.toString()
                val phoneNumber = fragRegistrationEditTextPhoneNumber.text.toString()

                val user = User(
                    id = DEFAULT_USER_ID,
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )

                viewModel.saveUser(user)
            }
        }
    }

    private fun setTextChangedListeners() {
        with(binding) {
            val firstNameValidator = getFirstNameValidator()
            val lastNameValidator = getLastNameValidator()
            val phoneNumberWatcher = getPhoneNumberWatcher()

            fragRegistrationEditTextFirstName.addTextChangedListener(firstNameValidator)
            fragRegistrationEditTextLastName.addTextChangedListener(lastNameValidator)
            fragRegistrationEditTextPhoneNumber.addTextChangedListener(phoneNumberWatcher)
        }
    }

    private fun getFirstNameValidator(): NameValidator {
        return with(binding) {
            NameValidator(
                onValidCallback = {
                    fragRegistrationTextInputLayoutFirstName.error = null
                    fragRegistrationButtonLog.isEnabled = areAllFieldsFilled()
                },
                onErrorCallback = {
                    fragRegistrationTextInputLayoutFirstName.error =
                        getString(R.string.fragRegistration_errorHint_cyrillicOnly)
                    fragRegistrationButtonLog.isEnabled = false
                },
            )
        }
    }

    private fun getLastNameValidator(): NameValidator {
        return with(binding) {
            NameValidator(
                onValidCallback = {
                    fragRegistrationTextInputLayoutLastName.error = null
                    fragRegistrationButtonLog.isEnabled = areAllFieldsFilled()
                },
                onErrorCallback = {
                    fragRegistrationTextInputLayoutLastName.error =
                        getString(R.string.fragRegistration_errorHint_cyrillicOnly)
                    fragRegistrationButtonLog.isEnabled = false
                },
            )
        }
    }

    private fun getPhoneNumberWatcher(): PhoneNumberWatcher {
        return PhoneNumberWatcher(
            afterTextChangedCallback = {
                binding.fragRegistrationButtonLog.isEnabled = areAllFieldsFilled()
            }
        )
    }

    private fun areAllFieldsFilled(): Boolean {
        return with(binding) {
            (fragRegistrationEditTextFirstName.text?.isNotBlank() == true
                    && fragRegistrationEditTextLastName.text?.isNotBlank() == true
                    && fragRegistrationEditTextPhoneNumber.rawText.length == requiredDigitsNumberToValidatePhone.value
                    )
        }
    }

}