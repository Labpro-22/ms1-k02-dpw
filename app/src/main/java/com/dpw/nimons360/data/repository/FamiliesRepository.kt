package com.dpw.nimons360.data.repository

import com.dpw.nimons360.core.network.ApiService
import com.dpw.nimons360.data.remote.CreateFamilyRequestDto
import com.dpw.nimons360.data.remote.FamilyDto
import com.dpw.nimons360.data.remote.FamilySummaryDto
import com.dpw.nimons360.data.remote.JoinFamilyRequestDto
import com.dpw.nimons360.data.remote.LeaveFamilyRequestDto
import java.io.IOException

class FamiliesRepository(
    private val apiService: ApiService
) {

    suspend fun getAllFamilies(): FamiliesResult<List<FamilySummaryDto>> {
        return try {
            val response = apiService.getAllFamilies()
            if (!response.isSuccessful) {
                return FamiliesResult.Error(mapHttpError(response.code()), response.code())
            }

            val families = response.body()?.data.orEmpty()
            FamiliesResult.Success(families)
        } catch (_: IOException) {
            FamiliesResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            FamiliesResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    suspend fun getMyFamilies(): FamiliesResult<List<FamilyDto>> {
        return try {
            val response = apiService.getMyFamilies()
            if (!response.isSuccessful) {
                return FamiliesResult.Error(mapHttpError(response.code()), response.code())
            }

            val families = response.body()?.data.orEmpty()
            FamiliesResult.Success(families)
        } catch (_: IOException) {
            FamiliesResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            FamiliesResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    suspend fun discoverFamilies(): FamiliesResult<List<FamilyDto>> {
        return try {
            val response = apiService.discoverFamilies()
            if (!response.isSuccessful) {
                return FamiliesResult.Error(mapHttpError(response.code()), response.code())
            }

            val families = response.body()?.data.orEmpty()
            FamiliesResult.Success(families)
        } catch (_: IOException) {
            FamiliesResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            FamiliesResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    suspend fun getFamilyDetail(familyId: Int): FamiliesResult<FamilyDto> {
        if (familyId <= 0) {
            return FamiliesResult.Error("Family ID tidak valid.")
        }

        return try {
            val response = apiService.getFamilyDetail(familyId)
            if (!response.isSuccessful) {
                return FamiliesResult.Error(mapHttpError(response.code()), response.code())
            }

            val family = response.body()?.data
                ?: return FamiliesResult.Error("Respon detail family tidak valid. Coba lagi.")

            FamiliesResult.Success(family)
        } catch (_: IOException) {
            FamiliesResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            FamiliesResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    suspend fun createFamily(name: String, iconUrl: String): FamiliesResult<FamilyDto> {
        if (name.isBlank()) {
            return FamiliesResult.Error("Nama family tidak boleh kosong.")
        }
        if (iconUrl.isBlank()) {
            return FamiliesResult.Error("Icon URL family tidak boleh kosong.")
        }

        return try {
            val request = CreateFamilyRequestDto(
                name = name.trim(),
                iconUrl = iconUrl.trim()
            )
            val response = apiService.createFamily(request)
            if (!response.isSuccessful) {
                return FamiliesResult.Error(mapHttpError(response.code()), response.code())
            }

            val family = response.body()?.data
                ?: return FamiliesResult.Error("Respon create family tidak valid. Coba lagi.")

            FamiliesResult.Success(family)
        } catch (_: IOException) {
            FamiliesResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            FamiliesResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    suspend fun joinFamily(familyId: Int, familyCode: String): FamiliesResult<Boolean> {
        if (familyId <= 0) {
            return FamiliesResult.Error("Family ID tidak valid.")
        }
        if (familyCode.length != 6) {
            return FamiliesResult.Error("Family code harus 6 karakter.")
        }

        return try {
            val request = JoinFamilyRequestDto(
                familyId = familyId,
                familyCode = familyCode.trim()
            )
            val response = apiService.joinFamily(request)
            if (!response.isSuccessful) {
                return FamiliesResult.Error(mapHttpError(response.code()), response.code())
            }

            val joined = response.body()?.data?.joined == true
            if (!joined) {
                return FamiliesResult.Error("Gagal bergabung ke family. Coba lagi.")
            }

            FamiliesResult.Success(true)
        } catch (_: IOException) {
            FamiliesResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            FamiliesResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    suspend fun leaveFamily(familyId: Int): FamiliesResult<Boolean> {
        if (familyId <= 0) {
            return FamiliesResult.Error("Family ID tidak valid.")
        }

        return try {
            val response = apiService.leaveFamily(LeaveFamilyRequestDto(familyId = familyId))
            if (!response.isSuccessful) {
                return FamiliesResult.Error(mapHttpError(response.code()), response.code())
            }

            val left = response.body()?.data?.left == true
            if (!left) {
                return FamiliesResult.Error("Gagal keluar dari family. Coba lagi.")
            }

            FamiliesResult.Success(true)
        } catch (_: IOException) {
            FamiliesResult.Error("Tidak dapat terhubung ke server. Periksa koneksi internet.")
        } catch (_: Exception) {
            FamiliesResult.Error("Terjadi kesalahan. Coba lagi dalam beberapa saat.")
        }
    }

    private fun mapHttpError(code: Int): String {
        return when (code) {
            400 -> "Permintaan tidak valid."
            401 -> "Autentikasi gagal. Silakan login ulang."
            403 -> "Anda tidak memiliki akses untuk aksi ini."
            404 -> "Family tidak ditemukan."
            409 -> "Sesi berakhir. Silakan login ulang."
            else -> "Permintaan family gagal (HTTP $code)."
        }
    }
}

sealed interface FamiliesResult<out T> {
    data class Success<T>(val data: T) : FamiliesResult<T>
    data class Error(val message: String, val code: Int? = null) : FamiliesResult<Nothing>
}


