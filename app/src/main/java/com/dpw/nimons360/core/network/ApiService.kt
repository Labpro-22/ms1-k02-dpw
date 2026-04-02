package com.dpw.nimons360.core.network

import com.dpw.nimons360.data.remote.auth.LoginRequestDto
import com.dpw.nimons360.data.remote.auth.LoginResponseDto
import com.dpw.nimons360.data.remote.CreateFamilyRequestDto
import com.dpw.nimons360.data.remote.FamiliesResponseDto
import com.dpw.nimons360.data.remote.FamilyDetailResponseDto
import com.dpw.nimons360.data.remote.JoinFamilyRequestDto
import com.dpw.nimons360.data.remote.JoinFamilyResponseDto
import com.dpw.nimons360.data.remote.LeaveFamilyRequestDto
import com.dpw.nimons360.data.remote.LeaveFamilyResponseDto
import com.dpw.nimons360.data.remote.MyFamiliesResponseDto
import com.dpw.nimons360.data.remote.ProfileResponseDto
import com.dpw.nimons360.data.remote.UpdateProfileRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @GET("/api/me")
    suspend fun getMyProfile(): Response<ProfileResponseDto>

    @PATCH("/api/me")
    suspend fun updateMyProfile(@Body request: UpdateProfileRequestDto): Response<ProfileResponseDto>

    @GET("/api/families")
    suspend fun getAllFamilies(): Response<FamiliesResponseDto>

    @GET("/api/me/families")
    suspend fun getMyFamilies(): Response<MyFamiliesResponseDto>

    @GET("/api/families/discover")
    suspend fun discoverFamilies(): Response<MyFamiliesResponseDto>

    @GET("/api/families/{familyId}")
    suspend fun getFamilyDetail(@Path("familyId") familyId: Int): Response<FamilyDetailResponseDto>

    @POST("/api/families")
    suspend fun createFamily(@Body request: CreateFamilyRequestDto): Response<FamilyDetailResponseDto>

    @POST("/api/families/join")
    suspend fun joinFamily(@Body request: JoinFamilyRequestDto): Response<JoinFamilyResponseDto>

    @POST("/api/families/leave")
    suspend fun leaveFamily(@Body request: LeaveFamilyRequestDto): Response<LeaveFamilyResponseDto>
}

