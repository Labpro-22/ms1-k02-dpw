package com.dpw.nimons360.core.network

import com.dpw.nimons360.data.remote.auth.LoginRequestDto
import com.dpw.nimons360.data.remote.auth.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>
}

