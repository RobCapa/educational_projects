package com.example.myunsplash.repositories

import android.content.Intent
import android.net.Uri
import com.example.myunsplash.AuthConfig
import com.example.myunsplash.UnsplashApp
import com.example.myunsplash.di.interfaces.AuthRepository
import net.openid.appauth.*
import javax.inject.Inject

class UnsplashAuthRepository @Inject constructor(): AuthRepository {
    private val authService = AuthorizationService(UnsplashApp.getAppContext())
    private var authState: AuthState
    private val authConfig = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.auth_uri),
        Uri.parse(AuthConfig.token_uri),
        null,
        Uri.parse(AuthConfig.logout_uri),
    )

    init {
        val authStateJson = SharedPreferencesRepository.sharedPreferences.getString(
            SharedPreferencesRepository.Keys.AUTH_STATE_KEY,
            null
        )
        authState = if (authStateJson != null) {
            AuthState.jsonDeserialize(authStateJson)
        } else {
            AuthState()
        }
    }

    override fun getAuthIntent(): Intent {
        val authRequest = AuthorizationRequest.Builder(
            authConfig,
            AuthConfig.client_id,
            ResponseTypeValues.CODE,
            Uri.parse(AuthConfig.redirect_uri)
        )
            .setScope("public read_user write_user write_likes write_followers")
            .setCodeVerifier(null)
            .build()
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    override fun getLogoutIntent(): Intent {
        val endSessionRequest = EndSessionRequest.Builder(authConfig)
            .setIdTokenHint(authState.idToken)
            .setPostLogoutRedirectUri(Uri.parse(AuthConfig.redirect_uri))
            .build()
        return authService.getEndSessionRequestIntent(endSessionRequest)
    }

    override fun processResultAuthRequest(
        intent: Intent,
        callback: (Boolean) -> Unit,
    ) {
        val resp = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)

        ex?.let {
            callback(false)
            return
        }

        authState.update(resp, ex)
        resp?.let { requestToken(it, callback) }
    }

    override fun checkIfAccessTokenExists(): Boolean {
        return authState.accessToken != null
    }

    override fun getAccessToken(): String? = authState.accessToken

    override fun removeToken() {
        authState = AuthState()
        SharedPreferencesRepository.sharedPreferences
            .edit()
            .remove(SharedPreferencesRepository.Keys.AUTH_STATE_KEY)
            .apply()
    }

    private fun requestToken(
        authResp: AuthorizationResponse,
        callback: (Boolean) -> Unit,
    ) {
        val additionalParams = mapOf("client_secret" to AuthConfig.secretKey)
        authService.performTokenRequest(
            authResp.createTokenExchangeRequest(additionalParams),
        ) { resp, ex -> processResultTokenRequest(resp, ex, callback) }
    }

    private fun processResultTokenRequest(
        resp: TokenResponse?,
        ex: AuthorizationException?,
        callback: (Boolean) -> Unit,
    ) {
        authState.update(resp, ex)
        ex?.let {
            callback(false)
            return
        }
        saveAuthState()
        callback(true)
    }

    private fun saveAuthState() {
        SharedPreferencesRepository.sharedPreferences
            .edit()
            .putString(
                SharedPreferencesRepository.Keys.AUTH_STATE_KEY,
                authState.jsonSerializeString()
            )
            .apply()
    }
}
