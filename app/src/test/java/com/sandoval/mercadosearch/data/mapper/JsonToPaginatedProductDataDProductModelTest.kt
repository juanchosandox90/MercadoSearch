package com.sandoval.mercadosearch.data.mapper

import com.google.gson.JsonObject
import com.sandoval.mercadosearch.data.datasource.remote.mapper.JsonToPaginatedProductsData
import com.sandoval.mercadosearch.data.extensions.toJsonObject
import com.sandoval.mercadosearch.domain.base.Result
import org.junit.Assert
import org.junit.Test

class JsonToPaginatedProductDataDProductModelTest {

    @Test
    fun unsuccessfulResponseCode() {
        val httpNotFound = 404
        val result =
            JsonToPaginatedProductsData.map(responseCode = httpNotFound, json = JsonObject())
        Assert.assertTrue(
            result is Result.Failure && checkFailureResponseCode(
                result.error,
                httpNotFound
            )
        )
    }

    @Test
    fun invalidResponseFormat() {
        val httpSuccessul = 200
        val result =
            JsonToPaginatedProductsData.map(responseCode = httpSuccessul, json = JsonObject())
        Assert.assertTrue(
            result is Result.Failure && checkFailureResponseCode(result.error, httpSuccessul)
        )
    }

    @Test
    fun invalidPagingValuesResponse() {
        val httpSuccessful = 200
        val json = "{\"paging\":{},\"results\":[]}"
        val result = JsonToPaginatedProductsData.map(
            responseCode = httpSuccessful,
            json = json.toJsonObject() ?: JsonObject()
        )
        Assert.assertTrue(
            result is Result.Failure && checkFailureResponseCode(
                result.error,
                httpSuccessful
            )
        )
    }

    @Test
    fun successEmptyResponse() {
        val httpSuccessful = 200
        val json = "{\"paging\":{\"total\":1,\"offset\":0,\"limit\":25},\"results\":[]}"

        val result = JsonToPaginatedProductsData.map(
            responseCode = httpSuccessful,
            json = json.toJsonObject() ?: JsonObject()
        )
        Assert.assertTrue(result is Result.Success && result.data.isEmpty())
    }
}
