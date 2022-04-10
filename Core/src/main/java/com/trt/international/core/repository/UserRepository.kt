package com.trt.international.core.repository

import com.trt.international.core.DataMapper
import com.trt.international.core.local.db.dao.UserFavoriteDao
import com.trt.international.core.model.UserDetail
import com.trt.international.core.model.UserFavorite
import com.trt.international.core.model.UserSearchItem
import com.trt.international.core.network.NetworkService
import com.trt.international.core.state.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val userFavoriteDao: UserFavoriteDao
) : IUserRepository {

    /**
     * Remote
     */
    override suspend fun getDiscoverUsersFromApi(): Flow<ResultState<List<UserSearchItem>>> {
        return flow {
            try {
                val discoverUserResponse = networkService.getUsers()
                emit(
                    ResultState.Success(
                        DataMapper.mapDiscoverUsersResponseToDomain(
                            discoverUserResponse
                        )
                    )
                )

            } catch (e: Exception) {
                emit(ResultState.Error(e.toString(), 500))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserFromApi(username: String): Flow<ResultState<List<UserSearchItem>>> {
        return flow {
            try {
                val response = networkService.getSearchUser(username)
                val userItems = response.userItems
                val dataMaped = userItems?.let { listSearchUser ->
                    DataMapper.mapUserSearchResponseToDomain(listSearchUser)
                }
                emit(ResultState.Success(dataMaped))
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString(), 500))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getDetailUserFromApi(username: String): Flow<ResultState<UserDetail>> {
        return flow {
            try {
                val response = networkService.getDetailUser(username)
                val dataMapped = DataMapper.mapUserDetailResponseToDomain(response)
                emit(ResultState.Success(dataMapped))
            } catch (e: Exception) {
                emit(ResultState.Error(e.toString(), 500))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Local
     */
    override fun fetchAllUserFavorite(): Flow<List<UserFavorite>> {
        return userFavoriteDao.fetchAllUsers().map {
            DataMapper.mapUserFavoriteEntitiesToDomain(it)
        }
    }

    override fun getFavoriteUserByUsername(username: String): Flow<List<UserFavorite>> {
        return userFavoriteDao.getFavByUsername(username).map {
            DataMapper.mapUserFavoriteEntitiesToDomain(it)
        }
    }

    override suspend fun addUserToFavDB(userFavoriteEntity: UserFavorite) {
        val data = DataMapper.mapUserFavoriteDomainToEntity(userFavoriteEntity)
        return userFavoriteDao.addUserToFavoriteDB(data)
    }

    override suspend fun deleteUserFromFavDB(userFavoriteEntity: UserFavorite) {
        val data = DataMapper.mapUserFavoriteDomainToEntity(userFavoriteEntity)
        return userFavoriteDao.deleteUserFromFavoriteDB(data)
    }

}