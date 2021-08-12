package xyz.katiedotson.gqlk

import retrofit2.http.Body
import retrofit2.http.POST
import xyz.katiedotson.gqlk.request.*

interface ApiService {
    companion object {
        const val GRAPHQL = "graphql/"
        const val ROOT = "/"
    }

    @POST(GRAPHQL)
    suspend fun getCapsuleById(@Body getCapsuleRequest: GqlKRequest): GqlKResponse<GetCapsuleRequest.GetCapsuleResponse>

    @POST(GRAPHQL)
    suspend fun getCoreById(@Body getCoreRequest: GqlKRequest): GqlKResponse<GetCoreRequest.GetCoreResponse>

    @POST(GRAPHQL)
    suspend fun getPastLaunches(@Body getPastLaunchesRequest: GqlKRequest): GqlKResponse<GetPastLaunchesRequest.GetPastLaunchesResponse>

    @POST(GRAPHQL)
    suspend fun getLaunchpad(@Body getLaunchpadRequest: GqlKRequest): GqlKResponse<GetLaunchpadRequest.GetLaunchpadResponse>

    @POST(GRAPHQL)
    suspend fun insertUsers(@Body insertUsersRequest: GqlKRequest): GqlKResponse<InsertUsersRequest.InsertUsersResponse>

    @POST(GRAPHQL)
    suspend fun deleteUser(@Body deleteUserRequest: GqlKRequest): GqlKResponse<DeleteUserRequest.DeleteUserResponse>

    @POST(ROOT)
    suspend fun getCountries(@Body getCountriesRequest: GqlKRequest): GqlKResponse<GetCountriesRequest.GetCountriesResponse>

    @POST(ROOT)
    suspend fun getLanguage(@Body getLanguageRequest: GqlKRequest): GqlKResponse<GetLanguageRequest.LanguageResponse>
}