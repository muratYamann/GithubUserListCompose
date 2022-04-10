package com.trt.international.githubuserlistcompose.screen.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trt.international.core.model.UserFavorite
import com.trt.international.core.model.UserSearchItem
import com.trt.international.core.state.ResultState
import com.trt.international.core.userusecase.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: IUserRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _resultUserApi = MutableLiveData<List<UserSearchItem>>()
    val resultUserApi: LiveData<List<UserSearchItem>>
        get() = _resultUserApi

    private val _resultDiscoverUserApi = MutableLiveData<List<UserSearchItem>>()
    val resultDiscoverUserApi: LiveData<List<UserSearchItem>>
        get() = _resultDiscoverUserApi

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

    fun getDiscoverUserFromApi() {
        _resultDiscoverUserApi.value = emptyList()
        _isLoading.value = true
        viewModelScope.launch {
            searchUseCase.getDiscoverUsersFromApi().collect {
                when (it) {
                    is ResultState.Success -> {
                        _resultDiscoverUserApi.postValue(it.data!!)
                        _isLoading.value = false
                    }

                    is ResultState.Error -> {
                        _error.postValue(it.error!!)
                        _isLoading.value = false
                    }

                    is ResultState.NetworkError -> {
                        _error.postValue("Networ Error")
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun getUserFromApi(query: String) {
        _resultUserApi.value = emptyList()
        _isLoading.value = true
        viewModelScope.launch {
            searchUseCase.getUserFromApi(query).collect {
                when (it) {
                    is ResultState.Success -> {
                        _resultUserApi.postValue(it.data!!)
                        _isLoading.value = false
                    }

                    is ResultState.Error -> {
                        _error.postValue(it.error!!)
                        _isLoading.value = false
                    }

                    is ResultState.NetworkError -> {
                        _error.postValue("Networ Error")
                        _isLoading.value = false
                    }
                }
            }
        }
    }

}
