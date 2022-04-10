package com.trt.international.core.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import timber.log.Timber

@Composable
fun <T> ObserveNetworkResult(
    state: State<NetworkResult<T>?>,
    onSuccess: (NetworkResult.Success<T>?) -> Unit,
    onFailed: (NetworkResult.Failure<T>) -> Unit
) {
    state.value?.let {
        when (it) {
            is NetworkResult.Progress -> {
                //onSuccess()
            }

            is NetworkResult.Success -> {
                onSuccess(it)
            }

            is NetworkResult.Failure -> {
                onFailed(it)
            }
        }
    } ?: Timber.e("state.value is NULL !!")
}

