package com.trt.international.githubuserlistcompose.screen.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trt.international.core.model.UserDetail
import com.trt.international.core.model.UserFavorite
import com.trt.international.core.state.ResultState
import com.trt.international.core.userusecase.IUserRepository
import com.trt.international.githubuserlistcompose.screen.favorite.viewmodel.FavoriteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val searchUseCase: IUserRepository
) : ViewModel() {

    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean>
        get() = _state

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _resultUserDetailApi = MutableLiveData<UserDetail>()
    val resultUserApi: LiveData<UserDetail>
        get() = _resultUserDetailApi

    /**
     * Insert to DB
     */
    private val _resultInsertUserToDb = MutableLiveData<Boolean>()
    val resultInsertUserDb: LiveData<Boolean>
        get() = _resultInsertUserToDb

    /**
     * Delete from db
     */
    private val _resultDeleteFromDb = MutableLiveData<Boolean>()
    val resultDeleteFromDb: LiveData<Boolean>
        get() = _resultDeleteFromDb


    /**
     * Local
     */
    fun addUserToFavDB(userFavoriteEntity: UserFavorite) {
        viewModelScope.launch {
            try {
                searchUseCase.addUserToFavDB(userFavoriteEntity)
                _resultInsertUserToDb.postValue(true)
            } catch (e: Exception) {
                _error.postValue(e.localizedMessage)
            }
        }
    }

    fun deleteUserFromDb(userFavoriteEntity: UserFavorite) {
        viewModelScope.launch {
            try {
                searchUseCase.deleteUserFromFavDB(userFavoriteEntity)
                _resultDeleteFromDb.postValue(true)
            } catch (e: Exception) {
                _error.postValue(e.localizedMessage)
            }
        }
    }


    fun getUserDetailFromApi(query: String) {
        _state.value = true
        viewModelScope.launch {

            searchUseCase.getDetailUserFromApi(query).collect {
                when (it) {
                    is ResultState.Success -> {
                        _resultUserDetailApi.postValue(it.data!!)
                        _state.value = false
                    }

                    is ResultState.Error -> {
                        _error.postValue(it.error!!)
                    }

                    is ResultState.NetworkError -> {
                        _error.postValue("Networ Error")
                    }
                }
            }

        }
    }

}

