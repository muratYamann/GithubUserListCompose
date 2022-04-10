package com.trt.international.githubuserlistcompose.di

import com.trt.international.core.repository.IUserRepository
import com.trt.international.core.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideUserUseCase(userUseCaseImpl: UserRepository): IUserRepository

}