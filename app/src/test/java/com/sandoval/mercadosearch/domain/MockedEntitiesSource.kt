package com.sandoval.mercadosearch.domain

import com.sandoval.mercadosearch.domain.base.ErrorEntity
import com.sandoval.mercadosearch.domain.models.details_product.DProductDetailsDataModel

val productDetailsEntity = DProductDetailsDataModel(
    availableQuantity = 1,
    soldQuantity = 2,
    warranty = "No warranty"
)

val errorEntity = ErrorEntity.NoNetworkConnectionError