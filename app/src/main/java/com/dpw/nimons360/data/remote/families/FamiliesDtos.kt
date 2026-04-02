package com.dpw.nimons360.data.remote

data class FamiliesResponseDto(
    val data: List<FamilySummaryDto>?
)

data class MyFamiliesResponseDto(
    val data: List<FamilyDto>?
)

data class FamilyDetailResponseDto(
    val data: FamilyDto?
)

data class JoinFamilyResponseDto(
    val data: JoinFamilyDataDto?
)

data class LeaveFamilyResponseDto(
    val data: LeaveFamilyDataDto?
)

data class FamilySummaryDto(
    val id: Int?,
    val name: String?,
    val iconUrl: String?
)

data class FamilyDto(
    val id: Int?,
    val name: String?,
    val iconUrl: String?,
    val isMember: Boolean?,
    val familyCode: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val members: List<FamilyMemberDto>?
)

data class FamilyMemberDto(
    val id: Int?,
    val fullName: String?,
    val email: String?,
    val joinedAt: String?
)

data class JoinFamilyDataDto(
    val joined: Boolean?
)

data class LeaveFamilyDataDto(
    val left: Boolean?
)

data class CreateFamilyRequestDto(
    val name: String,
    val iconUrl: String
)

data class JoinFamilyRequestDto(
    val familyId: Int,
    val familyCode: String
)

data class LeaveFamilyRequestDto(
    val familyId: Int
)


