package dev.antasource.goling.data.networksource

sealed class ApiResult<out T>(){
    object Loading : ApiResult<Nothing>()
    data class Success<T: Any>(val data : T?): ApiResult<T>()
    data class Error(val message: String, val e: Throwable? = null) : ApiResult<Nothing>()
}