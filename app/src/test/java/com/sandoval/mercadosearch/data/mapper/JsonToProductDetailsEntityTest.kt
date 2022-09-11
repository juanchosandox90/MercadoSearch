package com.sandoval.mercadosearch.data.mapper

import com.google.gson.JsonArray
import com.sandoval.mercadosearch.data.datasource.remote.mapper.JsonToProductDetails
import com.sandoval.mercadosearch.data.extensions.toJsonArray
import com.sandoval.mercadosearch.domain.base.Result
import org.junit.Assert
import org.junit.Test

class JsonToProductDetailsEntityTest {

    @Test
    fun unsuccessfulResponseCode() {
        val httpNotFound = 404
        val result =
            JsonToProductDetails.map(responseCode = httpNotFound, json = JsonArray())
        Assert.assertTrue(
            result is Result.Failure && checkFailureResponseCode(
                result.error,
                httpNotFound
            )
        )
    }

    @Test
    fun invalidResponseFormat() {
        val httpSuccessful = 200
        val result =
            JsonToProductDetails.map(responseCode = httpSuccessful, json = JsonArray())
        Assert.assertTrue(
            result is Result.Failure && checkFailureResponseCode(result.error, httpSuccessful)
        )
    }

    @Test
    fun invalidPagingValuesResponse() {
        val httpSuccessful = 200
        val json = "[{\"code\":200,\"body\":null}]"
        val result =
            JsonToProductDetails.map(
                responseCode = httpSuccessful,
                json = json.toJsonArray() ?: JsonArray()
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
        val json =
            StringBuilder("[{\"code\":200,\"body\":{\"available_quantity\":1,\"sold_quantity\":50")
                .append(",\"attributes\":[{\"name\":\"Accesorios incluidos\",\"value_name\":\"1 cable ")
                .append("AV,1 cable USB,1 cable de alimentación CA\"}],\"warranty\":\"Garantía del ")
                .append("vendedor: 6 meses\",\"pictures\":[{\"secure_url\":\"https://http2.mlstatic.")
                .append("com/D_930365-MLA32732578056_112019-O.jpg\"}]}}]")
        val result =
            JsonToProductDetails.map(
                responseCode = httpSuccessful,
                json = json.toString().toJsonArray() ?: JsonArray()
            )
        Assert.assertTrue(result is Result.Success)
    }
}