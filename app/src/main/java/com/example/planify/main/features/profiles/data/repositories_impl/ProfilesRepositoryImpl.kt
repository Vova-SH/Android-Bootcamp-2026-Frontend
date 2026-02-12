package com.example.planify.main.features.profiles.data.repositories_impl

import com.example.planify.main.features.auth.domain.utils.network.AuthenticatedApiClient
import com.example.planify.main.features.profiles.data.dto.fetch_my_profile.FetchMyProfileRequestDTO
import com.example.planify.main.features.profiles.data.dto.get_my_profile.GetMyProfileResponseDTO
import com.example.planify.main.features.profiles.data.dto.put_my_profile.PutMyProfileRequestDTO
import com.example.planify.main.features.profiles.data.dto.search.SearchProfileResponseDTO
import com.example.planify.main.features.profiles.domain.entities.Page
import com.example.planify.main.features.profiles.domain.entities.Profile
import com.example.planify.main.features.profiles.domain.repositories.ProfilesRepository
import com.example.planify.main.features.profiles.domain.schemas.PatchMyProfileSchema
import com.example.planify.main.features.profiles.domain.schemas.PutMyProfileSchema
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@Singleton
class ProfilesRepositoryImpl @Inject constructor(
    private val authenticatedApiClient: AuthenticatedApiClient
) : ProfilesRepository {
    private val _myProfile = MutableStateFlow<Profile?>(null)
    override val myProfile = _myProfile.asStateFlow()

    val profileFeaturePath = "/profiles"
    val fetchMyProfilePath = "$profileFeaturePath/my"
    val patchMyProfilePath = "$profileFeaturePath/my"
    val putMyProfilePath = "$profileFeaturePath/my"
    val searchProfilePath = "$profileFeaturePath/search"

    override suspend fun fetchMyProfile(): Result<Profile> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<GetMyProfileResponseDTO> {
                method = HttpMethod.Get
                url { path(fetchMyProfilePath) }
            }
            response.profile.toEntity()
        }
            .onSuccess { profile ->
                _myProfile.value = profile
            }
    }

    override suspend fun patchMyProfile(shema: PatchMyProfileSchema): Result<Unit> = withContext(Dispatchers.IO) {
        val requestDTO = FetchMyProfileRequestDTO(
            firstName = shema.firstName,
            lastName = shema.lastName,
            position = shema.position,
            department = shema.department,
            profileImageUrl = shema.profileImageUrl
        )

        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Patch
                url { path(patchMyProfilePath) }
                setBody(requestDTO)
            }
        }
            .onSuccess {
                _myProfile.update {
                    it?.copy(
                        firstName = shema.firstName ?: it.firstName,
                        lastName = shema.lastName ?: it.lastName,
                        position = shema.position ?: it.position,
                        department = shema.department ?: it.department,
                        profileImageUrl = shema.profileImageUrl ?: it.profileImageUrl
                    )
                }
            }
    }

    override suspend fun putMyProfile(shema: PutMyProfileSchema): Result<Unit> = withContext(Dispatchers.IO) {
        val requestDTO = PutMyProfileRequestDTO(
            firstName = shema.firstName,
            lastName = shema.lastName,
            position = shema.position,
            department = shema.department,
            profileImageUrl = shema.profileImageUrl
        )

        return@withContext runCatching {
            authenticatedApiClient.requestUnit {
                method = HttpMethod.Put
                url { path(putMyProfilePath) }
                setBody(requestDTO)
            }
        }
    }

    override suspend fun searchProfile(query: String, page: Int?, size: Int?, sort: List<String>?): Result<Page> = withContext(Dispatchers.IO) {
        return@withContext runCatching {
            val response = authenticatedApiClient.requestNotNull<SearchProfileResponseDTO> {
                method = HttpMethod.Get
                url { path(searchProfilePath) }
                parameter("query", query)

                page?.let { parameter("page", page) }
                size?.let { parameter("size", size) }
                sort?.let { parameter("sort", sort) }
            }

            response.result.toEntity()
        }
    }
}
