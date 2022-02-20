package com.groliks.cookingrecipes.data.util

sealed class LoadingStatus {
    object Loading : LoadingStatus()
    class Success(val data: Any) : LoadingStatus()
    class Error(val message: String) : LoadingStatus()
}