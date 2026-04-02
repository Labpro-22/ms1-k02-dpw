package com.dpw.nimons360.core.network

import com.dpw.nimons360.data.remote.auth.LoginRequestDto
import com.dpw.nimons360.data.remote.auth.LoginResponseDto
import com.dpw.nimons360.data.remote.ProfileResponseDto
import com.dpw.nimons360.data.remote.UpdateProfileRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @GET("/api/me")
    suspend fun getMyProfile(): Response<ProfileResponseDto>

    @PATCH("/api/me")
    suspend fun updateMyProfile(@Body request: UpdateProfileRequestDto): Response<ProfileResponseDto>
}

