package com.groliks.cookingrecipes.data.util

sealed class LoadingStatus<T>(val message: String) {
    class None<T> : LoadingStatus<T>("")
    class Loading<T>(message: String = "") : LoadingStatus<T>(message)
    class Success<T>(val data: T, message: String = "") : LoadingStatus<T>(message)
    class Error<T>(message: String) : LoadingStatus<T>(message)
}