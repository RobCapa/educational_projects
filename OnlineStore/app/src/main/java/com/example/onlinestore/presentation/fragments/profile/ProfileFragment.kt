package com.example.onlinestore.presentation.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.models.User
import com.example.onlinestore.R
import com.example.onlinestore.app.OnlineStoreApp
import com.example.onlinestore.databinding.FragProfileBinding
import com.example.onlinestore.databinding.ViewProfileActionBinding
import com.example.onlinestore.presentation.interfaces.NavigateToStartScreen
import com.example.onlinestore.presentation.utils.CorrectFormWordHelper
import com.example.onlinestore.presentation.utils.ViewModelFactory
import javax.inject.Inject

class ProfileFragment : Fragment(R.layout.frag_profile) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ProfileViewModel>

    private val binding: FragProfileBinding by viewBinding()
    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as OnlineStoreApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToViewModelResults()
        setOnClickListeners()
        createActions()

        viewModel.getSavedUser()
        viewModel.getCountFavoriteProducts()
    }

    private fun setOnClickListeners() {
        binding.fragProfileButtonExit.setOnClickListener {
            viewModel.userLive.value?.let { user ->
                viewModel.removeUser(user)
                (requireActivity() as NavigateToStartScreen).navToStartDestination()
            }
        }
    }

    private fun observeToViewModelResults() {
        with(viewModel) {
            userLive.observe(viewLifecycleOwner, ::bindUser)
            countFavoriteProducts.observe(viewLifecycleOwner, ::bindFavoriteAction)
        }
    }

    private fun bindUser(user: User) {
        with(binding) {
            with(user) {
                fragProfileTextViewUserName.text = "$firstName $lastName"
                fragProfileTextViewPhoneNumber.text = phoneNumber
            }
        }
    }

    private fun bindFavoriteAction(productCount: Int) {
        val viewId = actionIds[ProfileAction.FAVORITES] ?: return
        val view = binding.root.findViewById<View>(viewId) ?: return

        with(ViewProfileActionBinding.bind(view)) {
            val productCounterPattern = when (CorrectFormWordHelper.identifyCase(productCount)) {
                CorrectFormWordHelper.Case.FIRST -> R.string.fragProfile_pattern1_productCounter
                CorrectFormWordHelper.Case.SECOND -> R.string.fragProfile_pattern2_productCounter
                CorrectFormWordHelper.Case.THIRD -> R.string.fragProfile_pattern3_productCounter
            }

            viewProfileActionTextViewAdditionalInfo.text =
                getString(productCounterPattern).format(productCount)
            viewProfileActionTextViewAdditionalInfo.isVisible = true
        }
    }

    private val actionIds = mutableMapOf<ProfileAction, Int>()

    private fun createActions() {
        ProfileAction.entries.forEach { action ->
            val view = ViewProfileActionBinding.inflate(layoutInflater).apply {
                val context = requireContext()

                val id = View.generateViewId()
                val title = context.getString(action.title)
                val icon = context.getDrawable(action.icon)
                val iconColor = context.getColor(action.iconColor)
                var onClick: () -> Unit = { }

                when (action) {

                    ProfileAction.FAVORITES -> {
                        onClick = {
                            ProfileFragmentDirections
                                .actionProfileFragmentToFavoritesFragment()
                                .let(findNavController()::navigate)
                        }
                    }

                    else -> {}
                }

                root.id = id
                viewProfileActionTextViewTitle.text = title
                viewProfileActionImageViewIcon.setImageDrawable(icon)
                viewProfileActionImageViewIcon.setColorFilter(iconColor)
                root.setOnClickListener { onClick() }

                actionIds[action] = id

            }.root


            binding.fragProfileLinearLayoutActions.addView(view)

        }
    }

}