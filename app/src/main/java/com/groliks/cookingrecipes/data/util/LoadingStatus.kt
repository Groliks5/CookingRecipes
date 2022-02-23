package com.groliks.cookingrecipes.data.util

sealed class LoadingStatus(val message: String) {
    object None : LoadingStatus("")
    class Loading(message: String = "") : LoadingStatus(message)
    class Success(val data: Any = Any(), message: String = "") : LoadingStatus(message)
    class Error(message: String) : LoadingStatus(message)
}