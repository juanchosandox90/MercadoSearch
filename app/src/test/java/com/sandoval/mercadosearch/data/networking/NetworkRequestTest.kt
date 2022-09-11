package com.sandoval.mercadosearch.data.networking

import com.sandoval.mercadosearch.domain.base.ErrorEntity
import com.sandoval.mercadosearch.domain.base.Result
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException

class NetworkRequestTest {

    @MockK
    lateinit var connectionChecker: NetworkConnectionChecker

    @Before
    fun setupDependencies() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun noNetworkConnectionError() {
        every { connectionChecker.isAvailable() } returns false

        val result = networkRequest(connectionChecker) { Result.Success(Unit) }
        Assert.assertTrue(
            result is Result.Failure && result.error is ErrorEntity.NoNetworkConnectionError
        )
    }

    @Test
    fun timedOutOperation() {
        every { connectionChecker.isAvailable() } returns true

        val result = networkRequest<Nothing>(connectionChecker) {
            throw SocketTimeoutException()
        }
        Assert.assertTrue(
            result is Result.Failure && result.error is ErrorEntity.TimedOutOperationError
        )
    }

    @Test
    fun failedOperation() {

        every { connectionChecker.isAvailable() } returns true

        val result = networkRequest<Nothing>(connectionChecker) {
            throw IllegalStateException()
        }
        Assert.assertTrue(
            result is Result.Failure && result.error is ErrorEntity.FailedOperationError
        )
    }




}