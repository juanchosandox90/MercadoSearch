package com.sandoval.mercadosearch.ui.mapper

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sandoval.mercadosearch.domain.base.ErrorEntity
import com.sandoval.mercadosearch.ui.base.ErrorDProductModelToUIModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.NullPointerException

@RunWith(AndroidJUnit4::class)
class ErrorEntityToUIModelTest {

    private lateinit var context: Context
    private lateinit var mapper: ErrorDProductModelToUIModel

    @Before
    fun startupDependencies() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        mapper = ErrorDProductModelToUIModel(context)
    }

    @Test
    fun mapFailedRemoteResponseError() {
        val expectedGeneralMessage = "Oops! Algo salio mal!"

        val error = mapper.map(ErrorEntity.RemoteResponseError(code = 200))

        Assert.assertTrue(error.generalPurposeMessage == expectedGeneralMessage)
    }

    @Test
    fun mapFailedOperationError() {
        val expectedGeneralMessage = "Oops! Algo salio mal!"

        val error = mapper.map(ErrorEntity.FailedOperationError(NullPointerException()))

        Assert.assertTrue(error.generalPurposeMessage == expectedGeneralMessage)
    }

    @Test
    fun mapFailedTimedOutOperationError() {

        val expectedGeneralMessage = "Oops! Algo salio mal!"
        val expectedCriticalMessage = "Tu conexion a internet es muy baja, intenta mas tarde"

        val error = mapper.map(ErrorEntity.TimedOutOperationError(NullPointerException()))

        Assert.assertTrue(
            error.generalPurposeMessage == expectedGeneralMessage
                    && error.criticalMessage == expectedCriticalMessage
        )
    }

    @Test
    fun mapNoNetworkConnectionError() {

        val expectedGeneralMessage = "Oops! Algo salio mal!"
        val expectedCriticalMessage = "No tienes conexion a internet en este momento!"

        val error = mapper.map(ErrorEntity.NoNetworkConnectionError)

        Assert.assertTrue(
            error.generalPurposeMessage == expectedGeneralMessage
                    && error.criticalMessage == expectedCriticalMessage
        )
    }

}