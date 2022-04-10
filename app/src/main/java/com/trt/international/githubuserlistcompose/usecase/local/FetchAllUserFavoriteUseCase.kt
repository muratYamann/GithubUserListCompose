package com.trt.international.githubuserlistcompose.usecase.local

import com.trt.international.core.model.UserFavorite
import com.trt.international.core.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchAllUserFavoriteUseCase @Inject constructor(private val userRepository: UserRepository) {


    fun fetchAllUserFavorite(): Flow<List<UserFavorite>>{
        return userRepository.fetchAllUserFavorite()
    }


}
