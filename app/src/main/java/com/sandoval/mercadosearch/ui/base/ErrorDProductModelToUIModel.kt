package com.sandoval.mercadosearch.ui.base

import android.content.Context
import com.sandoval.mercadosearch.domain.base.ErrorEntity
import com.sandoval.mercadosearch.domain.base.Mapper
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class ErrorDProductModelToUIModel @Inject constructor(@ApplicationContext private val context: Context) :
    Mapper<ErrorEntity, ErrorUIModel> {

    override fun map(input: ErrorEntity): ErrorUIModel {
        val generalPurposeMessage = "Oops! Algo salio mal!"
        var criticalMessage: String? = null
        when (input) {
            is ErrorEntity.FailedOperationError -> {
                Timber.e(input.exception, "FailedOperationError")
            }
            is ErrorEntity.RemoteResponseError -> {
                Timber.e("RemoteResponseError - Code [%d] %s", input.code, input.message)
            }
            is ErrorEntity.TimedOutOperationError -> {
                Timber.e(input.exception, "TimedOutOperationError")
                criticalMessage = "Tu conexion a internet es muy baja, intenta mas tarde"
            }
            ErrorEntity.NoNetworkConnectionError -> {
                Timber.e("NoNetworkConnectionError")
                criticalMessage = "No tienes conexion a internet en este momento!"
            }
        }
        return ErrorUIModel(
            generalPurposeMessage,
            criticalMessage,
            suggestedAction = "Reintentar"
        )
    }

}