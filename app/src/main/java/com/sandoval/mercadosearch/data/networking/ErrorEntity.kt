package com.sandoval.mercadosearch.data.networking

sealed class ErrorEntity {
    class RemoteResponseError(val code: Int, val message: String? = null) : ErrorEntity()
    class TimedOutOperationError(val exception: Exception) : ErrorEntity()
    class FailedOperationError(val exception: Exception) : ErrorEntity()
    object NoNetworkConnectionError : ErrorEntity()
}