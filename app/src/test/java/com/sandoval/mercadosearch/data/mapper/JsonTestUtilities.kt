package com.sandoval.mercadosearch.data.mapper

import com.sandoval.mercadosearch.domain.base.ErrorEntity

fun checkFailureResponseCode(errorEntity: ErrorEntity, originCode: Int): Boolean {
    return if (errorEntity is ErrorEntity.RemoteResponseError) {
        errorEntity.code == originCode
    } else {
        false
    }
}
