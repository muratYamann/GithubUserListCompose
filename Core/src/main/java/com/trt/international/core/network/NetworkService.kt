package com.trt.international.core.network

import com.trt.international.core.responses.SearchUserResponse
import com.trt.international.core.responses.UserDetailResponse
import com.trt.international.core.responses.users.DiscoverUsersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    /**
     *  https://api.github.com/users
     *  Discover Users
     */
    @GET("users")
    suspend fun getUsers(): DiscoverUsersResponse

    /**
     *  Detail User
     */
    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): UserDetailResponse

    /**
     *  search User
     *  https://api.github.com/search/users?q=muratYamann
     */
    @GET("search/users?")
    suspend fun getSearchUser(
        @Query("q") q: String
    ): SearchUserResponse

}