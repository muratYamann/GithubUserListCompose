package com.trt.international.githubuserlistcompose.usecase.remote

import com.trt.international.core.model.UserDetail
import com.trt.international.core.repository.UserRepository
import com.trt.international.core.state.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetailFromApiUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun getDetailUserFromApi(username: String): Flow<ResultState<UserDetail>> {
        return userRepository.getDetailUserFromApi(username)
    }
}
