package com.example.myunsplash.fragments.entry_point

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myunsplash.R
import com.example.myunsplash.UnsplashApp
import com.example.myunsplash.di.interfaces.AuthRepository
import com.example.myunsplash.repositories.SharedPreferencesRepository
import com.example.myunsplash.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EntryPointViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _directionLiveData = MutableLiveData<Int>()
    val directionLiveData: LiveData<Int>
        get() = _directionLiveData

    private val _notificationNoConnection = SingleLiveEvent<Unit>()
    val notificationNoConnection: LiveData<Unit>
        get() = _notificationNoConnection

    fun defineFragmentForNavigate() {
        viewModelScope.launch {
            if (!checkForInternet()) {
                _notificationNoConnection.postValue(Unit)
                do delay(1000) while (!checkForInternet())
            }
            _directionLiveData.postValue(
                if (needToShowPresentation()) R.id.action_authDistributorFragment_to_presentationFragment
                else if (needToLogin()) R.id.action_authDistributorFragment_to_loginFragment
                else R.id.action_authDistributorFragment_to_mainFragment
            )
        }
    }

    private fun checkForInternet(): Boolean {
        val connectivityManager =
            UnsplashApp
                .getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private suspend fun needToShowPresentation(): Boolean {
        return withContext(Dispatchers.IO) {
            SharedPreferencesRepository.sharedPreferences.getBoolean(
                SharedPreferencesRepository.Keys.IS_FIRST_RUN_KEY,
                true
            )
        }
    }

    private fun needToLogin(): Boolean {
        return !authRepository.checkIfAccessTokenExists()
    }
}