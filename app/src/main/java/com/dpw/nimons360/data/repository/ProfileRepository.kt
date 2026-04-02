package com.dpw.nimons360.data.repository

import com.dpw.nimons360.core.network.ApiService
import com.dpw.nimons360.data.remote.UpdateProfileRequestDto
import com.dpw.nimons360.data.remote.UserProfileDto
import java.io.IOException

class ProfileRepository(
    private val apiService: ApiService
) {

    suspend fun getMyProfile(): ProfileResult {
        return try {
            val response = apiService.getMyProfile()
            if (!response.isSuccessful) {
                return ProfileResult.Error(mapHttpError(response.code()))
            }

            val profile = response.body()?.data
            if (profile?.id == null) {
                return ProfileResult.Error("Respon profil tidak valid. Coba lagi.")
            }

            ProfileResult.Success(profile)
        } catch (_: IOException) {
            ProfileResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            ProfileResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    suspend fun updateMyProfile(fullName: String): ProfileResult {
        if (fullName.isBlank()) {
            return ProfileResult.Error("Nama tidak boleh kosong.")
        }

        return try {
            val response = apiService.updateMyProfile(UpdateProfileRequestDto(fullName = fullName.trim()))
            if (!response.isSuccessful) {
                return ProfileResult.Error(mapHttpError(response.code()))
            }

            val profile = response.body()?.data
            if (profile?.id == null) {
                return ProfileResult.Error("Respon update profil tidak valid. Coba lagi.")
            }

            ProfileResult.Success(profile)
        } catch (_: IOException) {
            ProfileResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            ProfileResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    private fun mapHttpError(code: Int): String {
        return when (code) {
            400 -> "Data profil tidak valid."
            401 -> "Autentikasi gagal. Silakan login ulang."
            404 -> "Data profil tidak ditemukan."
            409 -> "Sesi berakhir. Silakan login ulang."
            else -> "Permintaan profil gagal (HTTP $code)."
        }
    }
}

sealed interface ProfileResult {
    data class Success(val profile: UserProfileDto) : ProfileResult
    data class Error(val message: String) : ProfileResult
}


