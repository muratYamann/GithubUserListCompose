package com.trt.international.core.repository

import com.trt.international.core.model.UserDetail
import com.trt.international.core.model.UserFavorite
import com.trt.international.core.model.UserSearchItem
import com.trt.international.core.state.ResultState
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    /**
     * Remote
     */
    suspend fun getDiscoverUsersFromApi(): Flow<ResultState<List<UserSearchItem>>>

    suspend fun getUserFromApi(username: String): Flow<ResultState<List<UserSearchItem>>>

    suspend fun getDetailUserFromApi(username: String): Flow<ResultState<UserDetail>>

    /**
     * Local
     */
    fun fetchAllUserFavorite(): Flow<List<UserFavorite>>

    fun getFavoriteUserByUsername(username: String): Flow<List<UserFavorite>>

    suspend fun addUserToFavDB(userFavoriteEntity: UserFavorite)

    suspend fun deleteUserFromFavDB(userFavoriteEntity: UserFavorite)

}