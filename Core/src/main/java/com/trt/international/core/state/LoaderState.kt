package com.trt.international.core.state

sealed class LoaderState {
    object ShowLoading: LoaderState()
    object HideLoading: LoaderState()
}