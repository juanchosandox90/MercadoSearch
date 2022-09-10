package com.sandoval.mercadosearch.data.extensions

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun JsonElement.getAsJsonObjectOrNull(): JsonObject? = runCatching { this.asJsonObject }.getOrNull()

fun JsonObject.getOrNull(member: String): JsonElement? = runCatching { get(member) }.getOrNull()

fun JsonObject.getAsJsonObjectOrNull(member: String): JsonObject? =
    runCatching { this.get(member).asJsonObject }.getOrNull()

fun String.toJsonObject(): JsonObject? =
    runCatching { Gson().fromJson(this, JsonObject::class.java) }.getOrNull()

fun String.toJsonArray(): JsonArray? =
    runCatching { Gson().fromJson(this, JsonArray::class.java) }.getOrNull()