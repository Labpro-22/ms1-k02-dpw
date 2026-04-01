package com.dpw.nimons360.core.network

import com.dpw.nimons360.core.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request()

        if (currentRequest.header(HEADER_AUTHORIZATION) != null) {
            return chain.proceed(currentRequest)
        }

        val token = sessionManager.getToken()
        if (token.isBlank()) {
            return chain.proceed(currentRequest)
        }

        val authorizedRequest = currentRequest.newBuilder()
            .addHeader(HEADER_AUTHORIZATION, "$BEARER_PREFIX $token")
            .build()

        return chain.proceed(authorizedRequest)
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val BEARER_PREFIX = "Bearer"
    }
}

