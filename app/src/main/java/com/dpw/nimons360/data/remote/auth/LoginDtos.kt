package com.dpw.nimons360.data.remote.auth

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val data: LoginDataDto?
)

data class LoginDataDto(
    val token: String?,
    val expiresAt: String?,
    val user: LoginUserDto?
)

data class LoginUserDto(
    val id: Int?,
    val nim: String?,
    val email: String?,
    val fullName: String?
)

