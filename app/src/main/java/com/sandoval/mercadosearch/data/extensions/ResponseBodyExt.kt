package com.sandoval.mercadosearch.data.extensions

import com.google.gson.JsonObject
import retrofit2.Response

fun <T> Response<T>.errorBodyAsJsonObject(): JsonObject? =
    runCatching { errorBody()?.string()?.toJsonObject() }.getOrNull()