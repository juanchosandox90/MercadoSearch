package com.sandoval.mercadosearch.domain.base

interface UseCase<P, R> {
    suspend fun perform(params: P): Result<R>
}
