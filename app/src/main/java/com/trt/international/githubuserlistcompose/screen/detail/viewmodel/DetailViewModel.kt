package com.trt.international.githubuserlistcompose.screen.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trt.international.core.model.UserDetail
import com.trt.international.core.model.UserFavorite
import com.trt.international.core.state.ResultState
import com.trt.international.githubuserlistcompose.usecase.local.AddFavToDbUseCase
import com.trt.international.githubuserlistcompose.usecase.local.DeleteFavFromDbUseCase
import com.trt.international.githubuserlistcompose.usecase.local.GetFavoriteByUserNameFromFavDbUseCase
import com.trt.international.githubuserlistcompose.usecase.remote.GetUserDetailFromApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val deleteFavFromDbUseCase: DeleteFavFromDbUseCase,
    private val addUserToDbUseCase: AddFavToDbUseCase,
    private val getDetailUserFromApi: GetUserDetailFromApiUseCase,
    private val getFavoriteByUserNameFromFavDbUseCase: GetFavoriteByUserNameFromFavDbUseCase,
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

    private val _resultUserApi = MutableLiveData<List<UserFavorite>>()
    val resultUserDB: LiveData<List<UserFavorite>>
        get() = _resultUserApi

    private val _resultInsertUserToDb = MutableLiveData<Boolean>()
    val resultInsertUserDb: LiveData<Boolean>
        get() = _resultInsertUserToDb

    private val _resultDeleteFromDb = MutableLiveData<Boolean>()
    val resultDeleteFromDb: LiveData<Boolean>
        get() = _resultDeleteFromDb

    fun addUserToFavDB(userFavoriteEntity: UserFavorite) {
        viewModelScope.launch {
            try {
                addUserToDbUseCase.addUserToFavDB(userFavoriteEntity)
                _resultInsertUserToDb.postValue(true)
            } catch (e: Exception) {
                _error.postValue(e.localizedMessage)
            }
        }
    }

    fun deleteUserFromDb(userFavoriteEntity: UserFavorite) {
        viewModelScope.launch {
            try {
                deleteFavFromDbUseCase.deleteUserFromFavDB(userFavoriteEntity)
                _resultDeleteFromDb.postValue(true)
            } catch (e: Exception) {
                _error.postValue(e.localizedMessage)
            }
        }
    }

    fun getUserDetailFromApi(query: String) {
        _state.value = true
        viewModelScope.launch {

            getDetailUserFromApi.getDetailUserFromApi(query).collect {
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

    fun getUserDetailFromDB(username: String) {
        _state.value = true
        viewModelScope.launch {
            getFavoriteByUserNameFromFavDbUseCase.getFavoriteUserByUsername(username).collect {
                _resultUserApi.postValue(it)
                _state.value = false
            }
        }
    }

}

