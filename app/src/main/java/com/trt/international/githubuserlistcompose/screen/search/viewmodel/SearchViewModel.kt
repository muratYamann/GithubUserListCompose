package com.trt.international.githubuserlistcompose.screen.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getDiscoverUserFromApi() {
        _isLoading.value = true
        viewModelScope.launch {

            searchUseCase.getDiscoverUsersFromApi().collect {
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

    fun getUserFromApi(query: String) {
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
