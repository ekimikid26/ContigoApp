package com.example.proyectotesismovil.data.remote

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.proyectotesismovil.BuildConfig
import com.example.proyectotesismovil.MainActivity
import com.example.proyectotesismovil.data.local.TokenManager
import com.example.proyectotesismovil.data.remote.api.AuthApi
import com.example.proyectotesismovil.data.remote.api.RefreshRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val context: Context
) : Interceptor {
    private val TAG = "AuthInterceptor"
    private val BASE_URL = BuildConfig.BASE_URL

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getAccessToken().firstOrNull()
        }

        val request = chain.request().newBuilder()
        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }

        var response: Response
        try {
            response = chain.proceed(request.build())
        } catch (e: Exception) {
            Log.e(TAG, "Error proceeding request", e)
            throw e
        }

        if (response.code == 401) {
            val path = chain.request().url.encodedPath
            if (path.contains("auth/login")) {
                return response
            }

            response.close()
            val refreshToken = runBlocking {
                tokenManager.getRefreshToken().firstOrNull()
            }

            if (refreshToken != null) {
                Log.d(TAG, "Attempting token refresh")
                val refreshResponse = runBlocking {
                    try {
                        val retrofitRefresh = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(AuthApi::class.java)
                        retrofitRefresh.refreshToken(RefreshRequest(refreshToken))
                    } catch (e: Exception) {
                        Log.e(TAG, "Refresh token request failed", e)
                        null
                    }
                }

                if (refreshResponse?.isSuccessful == true) {
                    val authBody = refreshResponse.body()
                    val newToken = authBody?.access_token
                    if (newToken != null) {
                        Log.d(TAG, "Token refreshed successfully")
                        runBlocking {
                            tokenManager.updateAccessToken(newToken)
                        }
                        val newRequest = chain.request().newBuilder()
                            .header("Authorization", "Bearer $newToken")
                            .build()
                        return chain.proceed(newRequest)
                    }
                } else {
                    Log.w(TAG, "Refresh failed or body null, clearing session")
                    runBlocking { tokenManager.clearTokens() }
                    try {
                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra("SESSION_EXPIRED", true)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error starting login activity", e)
                    }
                }
            }
        }
        return response
    }
}
