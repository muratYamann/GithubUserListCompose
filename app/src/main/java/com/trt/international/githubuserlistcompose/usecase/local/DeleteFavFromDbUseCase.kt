package com.trt.international.githubuserlistcompose.usecase.local

import com.trt.international.core.model.UserFavorite
import com.trt.international.core.repository.UserRepository
import javax.inject.Inject

class DeleteFavFromDbUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun deleteUserFromFavDB(userFavoriteEntity: UserFavorite){
        return userRepository.deleteUserFromFavDB(userFavoriteEntity)
    }

}
