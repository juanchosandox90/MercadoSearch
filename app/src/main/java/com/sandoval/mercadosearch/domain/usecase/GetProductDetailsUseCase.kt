package com.sandoval.mercadosearch.domain.usecase

import com.sandoval.mercadosearch.domain.base.Result
import com.sandoval.mercadosearch.domain.base.UseCase
import com.sandoval.mercadosearch.domain.models.details_product.DProductDetailsDataModel
import com.sandoval.mercadosearch.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) : UseCase<String, DProductDetailsDataModel> {
    override suspend fun perform(params: String): Result<DProductDetailsDataModel> =
        productsRepository.getDetails(params)

}