package com.sandoval.mercadosearch.domain.base

sealed class Result<out T> {
    class Success<T>(val data: T) : Result<T>()
    class Failure(val error: ErrorEntity) : Result<Nothing>()
}