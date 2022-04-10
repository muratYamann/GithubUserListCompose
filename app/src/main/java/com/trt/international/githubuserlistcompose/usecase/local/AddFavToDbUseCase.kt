package com.trt.international.githubuserlistcompose.usecase.local

import com.trt.international.core.model.UserFavorite
import com.trt.international.core.repository.UserRepository
import javax.inject.Inject

class AddFavToDbUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun addUserToFavDB(userFavoriteEntity: UserFavorite) {
        return userRepository.addUserToFavDB(userFavoriteEntity)
    }

}
