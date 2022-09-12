package com.sandoval.mercadosearch.data.json

import com.google.gson.JsonElement
import com.sandoval.mercadosearch.data.extensions.errorBodyAsJsonObject
import com.sandoval.mercadosearch.data.extensions.getOrNull
import com.sandoval.mercadosearch.domain.base.ErrorEntity
import com.sandoval.mercadosearch.domain.base.Mapper
import com.sandoval.mercadosearch.domain.base.Result
import retrofit2.Response

abstract class JsonResponseMapper<I : JsonElement, T> : Mapper<Response<I>, Result<T>> {

    final override fun map(input: Response<I>): Result<T> {
        val rawBody = input.body()
        return if (input.isSuccessful && rawBody != null && rawBody.isJsonNull.not()) {
            map(responseCode = input.code(), json = rawBody)
        } else {
            val error = input.errorBodyAsJsonObject()?.getOrNull(ERROR_OBJECT_KEY)?.asString
            Result.Failure(
                ErrorEntity.RemoteResponseError(
                    code = input.code(),
                    message = error
                )
            )
        }
    }

    abstract fun map(responseCode: Int, json: I): Result<T>

    companion object {
        private const val ERROR_OBJECT_KEY = ""
    }

}