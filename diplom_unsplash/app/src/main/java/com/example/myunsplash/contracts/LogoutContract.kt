package com.example.myunsplash.contracts

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.myunsplash.di.interfaces.AuthRepository
import javax.inject.Inject

class LogoutContract @Inject constructor(
    private val authRepository: AuthRepository,
) : ActivityResultContract<String?, Intent?>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return authRepository.getLogoutIntent()
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): Intent? = intent
}