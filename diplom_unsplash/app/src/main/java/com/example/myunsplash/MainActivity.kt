package com.example.myunsplash

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.databinding.ActivityMainBinding
import com.example.myunsplash.interfaces.PressBackButton
import com.example.myunsplash.utils.notifications.IssuerDownloadPhotoNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding()
    @Inject
    lateinit var issuerDownloadPhotoNotification: IssuerDownloadPhotoNotification

    override fun onBackPressed() {
        val frag = supportFragmentManager
            .findFragmentById(binding.fragmentContainerView.id)
            ?.childFragmentManager
            ?.fragments
            ?.first { it != null && it.isVisible }
        if (frag != null && frag is PressBackButton && frag.pressBackButton()) return
        super.onBackPressed()
    }

    fun observeToPhotoDownloadInfo(liveData: LiveData<WorkInfo>) {
        issuerDownloadPhotoNotification.observe(
            liveData,
            lifecycle,
            binding.root,
            this
        )
    }
}
