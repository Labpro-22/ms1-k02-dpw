package com.dpw.nimons360.data.remote

data class ProfileResponseDto(
    val data: UserProfileDto?
)

data class UserProfileDto(
    val id: Int?,
    val nim: String?,
    val email: String?,
    val fullName: String?,
    val createdAt: String?,
    val updatedAt: String?
)

data class UpdateProfileRequestDto(
    val fullName: String
)


