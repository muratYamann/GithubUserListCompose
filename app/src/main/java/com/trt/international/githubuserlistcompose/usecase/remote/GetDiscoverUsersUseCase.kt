package com.trt.international.githubuserlistcompose.usecase.remote

import com.trt.international.core.model.UserSearchItem
import com.trt.international.core.repository.UserRepository
import com.trt.international.core.state.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscoverUsersUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun getDiscoverUsersFromApi(): Flow<ResultState<List<UserSearchItem>>> {
        return userRepository.getDiscoverUsersFromApi()
    }

}
