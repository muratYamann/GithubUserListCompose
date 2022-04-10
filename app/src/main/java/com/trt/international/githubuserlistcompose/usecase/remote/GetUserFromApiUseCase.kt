package com.trt.international.githubuserlistcompose.usecase.remote

import com.trt.international.core.model.UserSearchItem
import com.trt.international.core.repository.UserRepository
import com.trt.international.core.state.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserFromApiUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun getUserFromApi(username: String): Flow<ResultState<List<UserSearchItem>>> {
        return userRepository.getUserFromApi(username)
    }
}
