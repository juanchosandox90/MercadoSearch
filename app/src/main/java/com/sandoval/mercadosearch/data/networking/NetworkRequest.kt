package com.sandoval.mercadosearch.data.networking

import java.net.SocketTimeoutException

inline fun <T> networkRequest(
    connectionChecker: NetworkConnectionChecker,
    body: () -> Result<T>
): Result<T> =
    try {
        if (connectionChecker.isAvailable()) {
            body()
        } else {
            Result.Failure(ErrorEntity.NoNetworkConnectionError)
        }
    } catch (exception: Exception) {
        if (exception is SocketTimeoutException) {
            Result.Failure(ErrorEntity.TimedOutOperationError(exception))
        } else {
            Result.Failure(ErrorEntity.FailedOperationError(exception))
        }
    }