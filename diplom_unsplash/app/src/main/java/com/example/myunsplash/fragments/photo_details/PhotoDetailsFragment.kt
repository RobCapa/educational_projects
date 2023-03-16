package com.example.myunsplash.fragments.photo_details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.myunsplash.MainActivity
import com.example.myunsplash.R
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.PhotoDetails
import com.example.myunsplash.databinding.FragPhotoDetailsBinding
import com.example.myunsplash.exceptions.WorkAlreadyExistsException
import com.example.myunsplash.utils.getIntFromTheme
import com.example.myunsplash.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment(R.layout.frag_photo_details) {
    private val binding: FragPhotoDetailsBinding by viewBinding()
    private val viewModel: PhotoDetailsViewModel by viewModels()
    private val args: PhotoDetailsFragmentArgs by navArgs()
    private lateinit var photoDetails: PhotoDetails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransitionName()
        observeToLiveData()
        viewModel.getPhotoDetails(args.photoId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater
            .from(requireContext())
            .inflateTransition(android.R.transition.move)
    }

    private fun setTransitionName() {
        ViewCompat.setTransitionName(
            binding.fragPhotoDetailsIncludePhoto.itemPhotoImageViewPhoto,
            "photo_${args.photoId}"
        )
    }

    private fun configureToolbar() {
        with(binding.fragPhotoDetailsIncludeToolbar.toolbar.menu) {
            setGroupVisible(R.id.menuToolbar_group_all, false)
            findItem(R.id.menuToolbar_share).isVisible = true
            findItem(R.id.menuToolbar_download).isVisible = true
        }

        with(binding.fragPhotoDetailsIncludeToolbar.toolbar) {
            title = getString(R.string.page_photoDetails)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuToolbar_share -> {
                        shareImageUrl()
                        true
                    }
                    R.id.menuToolbar_download -> {
                        downloadPhoto(photoDetails.photo.downloadUrl)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun shareImageUrl() {
        viewModel.shareImageUrl(args.photoId, requireContext())
    }

    private fun bind() {
        configureToolbar()
        bindLike()

        Glide.with(requireContext())
            .load(photoDetails.photo.url)
            .into(binding.fragPhotoDetailsIncludePhoto.itemPhotoImageViewPhoto)

        with(photoDetails) {
            val notSpecified = getString(R.string.fragPhotoDetails_notSpecified)

            binding.fragPhotoDetailsTextViewMadeWith.text =
                getString(R.string.fragPhotoDetails_madeWith).format(makeWith ?: notSpecified)
            binding.fragPhotoDetailsTextViewModel.text =
                getString(R.string.fragPhotoDetails_model).format(model ?: notSpecified)
            binding.fragPhotoDetailsTextViewExposure.text =
                getString(R.string.fragPhotoDetails_exposure).format(exposure ?: notSpecified)
            binding.fragPhotoDetailsTextViewAperture.text =
                getString(R.string.fragPhotoDetails_aperture).format(aperture ?: notSpecified)
            binding.fragPhotoDetailsTextViewFocalLength.text =
                getString(R.string.fragPhotoDetails_focalLength).format(focalLength ?: notSpecified)
            binding.fragPhotoDetailsTextViewIso.text =
                getString(R.string.fragPhotoDetails_iso).format(iso ?: notSpecified)
            binding.fragPhotoDetailsTextViewDescription.text =
                getString(R.string.fragPhotoDetails_description).format(description ?: notSpecified)

            if (location != null) {
                binding.fragPhotoDetailsImageViewLocationIcon.isVisible = true
                binding.fragPhotoDetailsTextViewLocation.isVisible = true
                binding.fragPhotoDetailsTextViewLocation.text = location
            }

            if (tags.isNotEmpty()) {
                binding.fragPhotoDetailsTextViewTags.isVisible = true
                binding.fragPhotoDetailsTextViewTags.text = tags.joinToString(" #", "#")
            }
        }

        with(binding.fragPhotoDetailsIncludePhoto.itemPhotoIncludeInformationPlate) {
            informationPlateTextViewName.text = photoDetails.photo.author.name
            informationPlateTextViewUserName.text = "@${photoDetails.photo.author.username}"

            informationPlateImageViewLike.setOnClickListener {
                viewModel.likeOrUnlikePhoto(
                    photoDetails.photo.id,
                    !photoDetails.photo.likedByUser
                )
            }

            Glide.with(requireContext())
                .load(photoDetails.photo.author.avatarUrl)
                .into(informationPlateImageViewAuthorAvatar)

            informationPlateImageViewLike.isVisible = true
        }

        binding.fragPhotoDetailsProgressBar.isVisible = false
    }

    private fun bindLike() {
        with(photoDetails.photo) {
            binding
                .fragPhotoDetailsIncludePhoto
                .itemPhotoIncludeInformationPlate
                .informationPlateTextViewLikesNumber.text =
                "$likes"

            val likeDrawable = requireContext().getDrawable(
                if (likedByUser) R.drawable.ic_baseline_like
                else R.drawable.ic_baseline_like_border
            )

            val likeColor =
                if (likedByUser) requireContext().getColor(R.color.red)
                else requireContext().getIntFromTheme(R.attr.mainTextColor)

            binding.fragPhotoDetailsIncludePhoto
                .itemPhotoIncludeInformationPlate
                .informationPlateImageViewLike.apply {
                    setImageDrawable(likeDrawable)
                    setColorFilter(likeColor)
                }
        }
    }

    private fun observeToLiveData() {
        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            photoDetails = it
            bind()
        }
        viewModel.downloadingPhotoInfoLiveData.observe(viewLifecycleOwner) { workInfoLiveData ->
            (requireActivity() as? MainActivity)?.let {
                it.observeToPhotoDownloadInfo(
                    workInfoLiveData
                )
            }
        }
        viewModel.likeInfoLiveData.observe(viewLifecycleOwner, ::updateLikeInfoForPhoto)
        viewModel.exception.observe(viewLifecycleOwner, ::handleException)
    }

    private fun updateLikeInfoForPhoto(likeInfo: LikeInfo) {
        with(photoDetails.photo) {
            likes = likeInfo.likesNumber
            likedByUser = likeInfo.likedByUser
        }
        bindLike()
    }

    private fun handleException(ex: Throwable) {
        when (ex) {
            is SocketTimeoutException, is UnknownHostException -> {
                binding.root.showSnackbar(R.string.notification_noConnection)
            }
            is WorkAlreadyExistsException -> {
                binding.root.showSnackbar(R.string.notification_photoIsAlreadyBeingDownloaded)
            }
            is HttpException -> {
                binding.fragPhotoDetailsGroupImageNotFound.isVisible = true
                binding.fragPhotoDetailsProgressBar.isVisible = false
            }
        }
    }

    private fun downloadPhoto(url: String) {
        viewModel.downloadPhoto(url)
    }
}