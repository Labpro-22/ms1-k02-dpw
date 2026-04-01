package com.dpw.nimons360.data.repository

import com.dpw.nimons360.core.network.ApiService
import com.dpw.nimons360.core.utils.SessionManager
import com.dpw.nimons360.data.remote.auth.LoginRequestDto
import java.io.IOException

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, password: String): LoginResult {
        return try {
            val request = LoginRequestDto(email = email, password = password)
            val response = apiService.login(request)

            if (!response.isSuccessful) {
                return LoginResult.Error(mapHttpError(response.code()))
            }

            val loginData = response.body()?.data
            val token = loginData?.token.orEmpty()
            if (token.isBlank()) {
                return LoginResult.Error("Respon login tidak valid. Coba lagi.")
            }

            sessionManager.saveSession(
                token = token,
                expiresAt = loginData?.expiresAt,
                fullName = loginData?.user?.fullName,
                email = loginData?.user?.email
            )

            LoginResult.Success
        } catch (_: IOException) {
            LoginResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            LoginResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }


    private fun mapHttpError(code: Int): String {
        return when (code) {
            400 -> "Format email atau password tidak valid."
            401 -> "Email atau password salah."
            409 -> "Sesi berakhir. Silakan login ulang."
            else -> "Login gagal (HTTP $code)."
        }
    }
}


sealed interface LoginResult {
    data object Success : LoginResult
    data class Error(val message: String) : LoginResult
}

