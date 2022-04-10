package com.trt.international.githubuserlistcompose.screen.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trt.international.core.model.UserFavorite
import com.trt.international.githubuserlistcompose.usecase.local.AddFavToDbUseCase
import com.trt.international.githubuserlistcompose.usecase.local.DeleteFavFromDbUseCase
import com.trt.international.githubuserlistcompose.usecase.local.FetchAllUserFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val deleteFavFromDbUseCase: DeleteFavFromDbUseCase,
    private val fetchAllUserFavoriteUseCase: FetchAllUserFavoriteUseCase,
    private val addUserToDbUseCase: AddFavToDbUseCase
) : ViewModel() {

    private val _isLoadingState = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoadingState

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _resultUserApi = MutableLiveData<List<UserFavorite>>()
    val resultUserApi: LiveData<List<UserFavorite>>
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

    fun getFavoriteUserList() {
        _isLoadingState.value = true
        viewModelScope.launch {
            fetchAllUserFavoriteUseCase.fetchAllUserFavorite().collect {
                _resultUserApi.postValue(it)
                _isLoadingState.value = false
            }
        }
    }

}
